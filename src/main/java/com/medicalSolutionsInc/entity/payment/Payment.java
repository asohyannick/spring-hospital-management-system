package com.medicalSolutionsInc.entity.payment;

import com.medicalSolutionsInc.enumerations.paymentStatus.PaymentStatus;
import com.medicalSolutionsInc.enumerations.paymentType.PaymentType;
import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import com.medicalSolutionsInc.enumerations.refundStatus.RefundStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {

		@Id
		private String id;
		
		@Indexed(unique = true)
		@Field("payment_number")
		private String paymentNumber;
		
		@Field("invoice_number")
		private String invoiceNumber;
		
		@Field("type")
		@Builder.Default
		private PaymentType paymentType = PaymentType.STRIPE;
		
		@Field("status")
		@Builder.Default
		private PaymentStatus status = PaymentStatus.PENDING;
		
		@Field("currency")
		@Builder.Default
		private String currency = "USD";
		
		@Field("amount")
		private BigDecimal amount;
		
		@Field("amount_paid")
		private BigDecimal amountPaid;
		
		@Field("amount_due")
		private BigDecimal amountDue;
		
		@Field("discount_amount")
		@Builder.Default
		private BigDecimal discountAmount = BigDecimal.ZERO;
		
		@Field("tax_amount")
		@Builder.Default
		private BigDecimal taxAmount = BigDecimal.ZERO;
		
		@Field("refunded_amount")
		@Builder.Default
		private BigDecimal refundedAmount = BigDecimal.ZERO;
		
		@Indexed
		@Field("patient_id")
		private String patientId;
		
		@Field("patient_name")
		private String patientName;
		
		@Field("patient_email")
		private String patientEmail;
		
		@Field("patient_phone")
		private String patientPhone;
		
		@Field("reference_id")
		private String referenceId;
		
		@Field("reference_type")
		private ReferenceType referenceType;
		
		@Field("description")
		private String description;
		
		@Field("transaction_id")
		@Indexed(unique = true)
		private String transactionId;
		
		@Field("gateway_reference")
		private String gatewayReference;
		
		@Field("gateway_response_code")
		private String gatewayResponseCode;
		
		@Field("gateway_response_message")
		private String gatewayResponseMessage;
		
		@Field("payment_method_details")
		private PaymentMethodDetails paymentMethodDetails;
		
		@Field("insurance_claim_id")
		private String insuranceClaimId;
		
		@Field("insurance_provider")
		private String insuranceProvider;
		
		@Field("insurance_covered_amount")
		@Builder.Default
		private BigDecimal insuranceCoveredAmount = BigDecimal.ZERO;
		
		@Field("refunds")
		@Builder.Default
		private List<Refund> refunds = new ArrayList<>();
		
		@Field("paid_at")
		private Instant paidAt;
		
		@Field("due_date")
		private Instant dueDate;
		
		@Field("refunded_at")
		private Instant refundedAt;
		
		@Field("is_partial_payment")
		@Builder.Default
		private boolean partialPayment = false;
		
		@Field("is_insurance_claim")
		@Builder.Default
		private boolean insuranceClaim = false;
		
		@Field("receipt_sent")
		@Builder.Default
		private boolean receiptSent = false;
		
		@CreatedDate
		@Field("created_at")
		private Instant createdAt;
		
		@LastModifiedDate
		@Field("updated_at")
		private Instant updatedAt;
		
		@Field("deleted_at")
		private Instant deletedAt;
		
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class PaymentMethodDetails {
			private String cardBrand;
			private String cardLastFour;
			private String cardHolderName;
			private String billingAddress;
			private String walletType;
			private String bankName;
			private String accountLastFour;
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class Refund {
			private String refundId;
			private BigDecimal amount;
			private String reason;
			private RefundStatus status;
			private Instant initiatedAt;
			private Instant processedAt;
		}
}