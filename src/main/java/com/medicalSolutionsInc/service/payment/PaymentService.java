package com.medicalSolutionsInc.service.payment;

import com.medicalSolutionsInc.dto.paymentDTO.PaymentRequestDTO;
import com.medicalSolutionsInc.dto.paymentDTO.PaymentResponseDTO;
import com.medicalSolutionsInc.dto.paymentDTO.RefundRequestDTO;
import com.medicalSolutionsInc.entity.payment.Payment;
import com.medicalSolutionsInc.enumerations.paymentStatus.PaymentStatus;
import com.medicalSolutionsInc.enumerations.refundStatus.RefundStatus;
import com.medicalSolutionsInc.exceptions.notFoundException.NotFoundException;
import com.medicalSolutionsInc.mappers.paymentMapper.PaymentMapper;
import com.medicalSolutionsInc.repository.payment.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

		private final PaymentRepository paymentRepository;
		private final PaymentMapper     paymentMapper;
		
		public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
			
			log.info("Processing payment for patient={} invoice={}", dto.patientId(), dto.invoiceNumber());
			
			if (paymentRepository.existsByInvoiceNumberAndDeletedAtIsNull(dto.invoiceNumber())) {
				throw new ResponseStatusException(
						HttpStatus.CONFLICT,
						"A payment record for invoice [%s] already exists".formatted(dto.invoiceNumber())
				);
			}
			
			Payment payment = paymentMapper.toEntity(dto);
			payment.setPaymentNumber(generatePaymentNumber());
			payment.setStatus(PaymentStatus.PROCESSING);
			payment.setAmountDue(dto.amount());
			
			try {
				long amountInCents = dto.amount()
						                     .multiply(BigDecimal.valueOf(100))
						                     .setScale(0, RoundingMode.HALF_UP)
						                     .longValueExact();
				
				PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
						                                   .setAmount(amountInCents)
						                                   .setCurrency(dto.currency() != null ? dto.currency().toLowerCase() : "usd")
						                                   .setPaymentMethod(dto.stripePaymentMethodId())
						                                   .setConfirm(true)
						                                   .setReturnUrl("https://medicalSolutionsInc.com/payment/return")
						                                   .setDescription(dto.description())
						                                   .putMetadata("invoice_number", dto.invoiceNumber())
						                                   .putMetadata("patient_id",     dto.patientId())
						                                   .putMetadata("payment_number", payment.getPaymentNumber())
						                                   .build();
				
				PaymentIntent intent = PaymentIntent.create(params);
				
				payment.setTransactionId(intent.getId());
				payment.setGatewayReference(intent.getClientSecret());
				payment.setGatewayResponseCode(intent.getStatus());
				payment.setGatewayResponseMessage("Stripe PaymentIntent: " + intent.getStatus());
				
				if ("succeeded".equalsIgnoreCase(intent.getStatus())) {
					payment.setStatus(PaymentStatus.COMPLETED);
					payment.setAmountPaid(dto.amount());
					payment.setAmountDue(BigDecimal.ZERO);
					payment.setPaidAt(Instant.now());
					log.info("Payment succeeded for invoice={}", dto.invoiceNumber());
				} else if ("requires_action".equalsIgnoreCase(intent.getStatus())) {
					payment.setStatus(PaymentStatus.PROCESSING);
					log.warn("Payment requires further action for invoice={}", dto.invoiceNumber());
				} else {
					payment.setStatus(PaymentStatus.FAILED);
					log.error("Payment failed for invoice={} status={}", dto.invoiceNumber(), intent.getStatus());
				}
				
			} catch (StripeException ex) {
				log.error("Stripe error processing payment for invoice={}: {}", dto.invoiceNumber(), ex.getMessage());
				payment.setStatus(PaymentStatus.FAILED);
				payment.setGatewayResponseCode(ex.getCode());
				payment.setGatewayResponseMessage(ex.getMessage());
				Payment saved = paymentRepository.save(payment);
				throw new ResponseStatusException(
						HttpStatus.PAYMENT_REQUIRED,
						"Payment processing failed: " + ex.getMessage()
				);
			}
			
			Payment saved = paymentRepository.save(payment);
			log.info("Payment record persisted id={} status={}", saved.getId(), saved.getStatus());
			return paymentMapper.toResponse(saved);
		}
		
		public Page<PaymentResponseDTO> fetchPaymentHistories(
				String patientId,
				PaymentStatus status,
				Pageable pageable
		) {
			Page<Payment> page;
			
			if (patientId != null && status != null) {
				page = paymentRepository.findAllByPatientIdAndStatus(patientId, status, pageable);
			} else if (patientId != null) {
				page = paymentRepository.findAllByPatientId(patientId, pageable);
			} else if (status != null) {
				page = paymentRepository.findAllByStatus(status, pageable);
			} else {
				page = paymentRepository.findAllActive(pageable);
			}
			
			return page.map(paymentMapper::toResponse);
		}
		
	
		public PaymentResponseDTO fetchPaymentHistory(String id) throws Exception {
			Payment payment = findActiveOrThrow(id);
			return paymentMapper.toResponse(payment);
		}
		
		public void deletePaymentHistory(String id) throws Exception {
			Payment payment = findActiveOrThrow(id);
			
			if (payment.getStatus() == PaymentStatus.COMPLETED ||
					    payment.getStatus() == PaymentStatus.REFUNDED   ||
					    payment.getStatus() == PaymentStatus.PARTIALLY_REFUNDED) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Cannot delete a payment with status [%s]. Only FAILED or CANCELLED payments can be removed."
								.formatted(payment.getStatus())
				);
			}
			
			payment.setDeletedAt(Instant.now());
			paymentRepository.save(payment);
			log.info("Payment soft-deleted id={}", id);
		}
		
		public PaymentResponseDTO refundPayment(String id, RefundRequestDTO dto) throws Exception {
			
			Payment payment = findActiveOrThrow(id);
			
			if (payment.getStatus() != PaymentStatus.COMPLETED &&
					    payment.getStatus() != PaymentStatus.PARTIALLY_REFUNDED) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Only COMPLETED or PARTIALLY_REFUNDED payments can be refunded. Current status: " + payment.getStatus()
				);
			}
			
			BigDecimal refundAmount = dto.amount() != null ? dto.amount() : payment.getAmountPaid();
			BigDecimal alreadyRefunded = payment.getRefundedAmount() != null
					                             ? payment.getRefundedAmount()
					                             : BigDecimal.ZERO;
			BigDecimal maxRefundable = payment.getAmountPaid().subtract(alreadyRefunded);
			
			if (refundAmount.compareTo(maxRefundable) > 0) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Refund amount [%s] exceeds refundable balance [%s]"
								.formatted(refundAmount, maxRefundable)
				);
			}
			
			Payment.Refund refundEntry;
			try {
				long refundCents = refundAmount
						                   .multiply(BigDecimal.valueOf(100))
						                   .setScale(0, RoundingMode.HALF_UP)
						                   .longValueExact();
				
				RefundCreateParams params = RefundCreateParams.builder()
						                            .setPaymentIntent(payment.getTransactionId())
						                            .setAmount(refundCents)
						                            .setReason(mapRefundReason(dto.reason()))
						                            .putMetadata("payment_number", payment.getPaymentNumber())
						                            .putMetadata("reason",         dto.reason())
						                            .build();
				
				Refund stripeRefund = Refund.create(params);
				
				refundEntry = Payment.Refund.builder()
						              .refundId(stripeRefund.getId())
						              .amount(refundAmount)
						              .reason(dto.reason())
						              .status("succeeded".equalsIgnoreCase(stripeRefund.getStatus())
								                      ? RefundStatus.SUCCEEDED : RefundStatus.PROCESSING)
						              .initiatedAt(Instant.now())
						              .processedAt("succeeded".equalsIgnoreCase(stripeRefund.getStatus())
								                           ? Instant.now() : null)
						              .build();
				
				log.info("Stripe refund created id={} for payment={}", stripeRefund.getId(), payment.getId());
				
			} catch (StripeException ex) {
				log.error("Stripe refund error for payment={}: {}", id, ex.getMessage());
				throw new ResponseStatusException(
						HttpStatus.INTERNAL_SERVER_ERROR,
						"Refund processing failed: " + ex.getMessage()
				);
			}
			
			payment.getRefunds().add(refundEntry);
			
			BigDecimal newRefundedTotal = alreadyRefunded.add(refundAmount);
			payment.setRefundedAmount(newRefundedTotal);
			payment.setRefundedAt(Instant.now());
			
			boolean fullyRefunded = newRefundedTotal.compareTo(payment.getAmountPaid()) >= 0;
			payment.setStatus(fullyRefunded ? PaymentStatus.REFUNDED : PaymentStatus.PARTIALLY_REFUNDED);
			
			Payment saved = paymentRepository.save(payment);
			log.info("Payment updated after refund id={} newStatus={}", saved.getId(), saved.getStatus());
			return paymentMapper.toResponse(saved);
		}
		
		
		private Payment findActiveOrThrow(String id) throws Exception {
			return paymentRepository.findByIdAndDeletedAtIsNull(id)
					       .orElseThrow(() -> new NotFoundException (
							       "Payment not found with id: " + id
					       ));
		}
		
		private String generatePaymentNumber() {
			String datePart = java.time.LocalDate.now()
					                  .toString()
					                  .replace("-", "");
			String uniquePart = UUID.randomUUID().toString()
					                    .replace("-", "")
					                    .substring(0, 8)
					                    .toUpperCase();
			return "PAY-" + datePart + "-" + uniquePart;
		}
		
		private RefundCreateParams.Reason mapRefundReason(String reason) {
			if (reason == null) return RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER;
			String lower = reason.toLowerCase();
			if (lower.contains("duplicate"))                    return RefundCreateParams.Reason.DUPLICATE;
			if (lower.contains("fraud") || lower.contains("unauthorized")) return RefundCreateParams.Reason.FRAUDULENT;
			return RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER;
		}
}