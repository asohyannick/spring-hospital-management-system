package com.medicalSolutionsInc.mappers.paymentMapper;

import com.medicalSolutionsInc.dto.paymentDTO.*;
import com.medicalSolutionsInc.entity.payment.Payment;
import org.mapstruct.*;

import java.util.List;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PaymentMapper {
		
		@Mapping(target = "id",                    ignore = true)
		@Mapping(target = "paymentNumber",         ignore = true)
		@Mapping(target = "status",                ignore = true)
		@Mapping(target = "transactionId",         ignore = true)
		@Mapping(target = "gatewayReference",      ignore = true)
		@Mapping(target = "gatewayResponseCode",   ignore = true)
		@Mapping(target = "gatewayResponseMessage",ignore = true)
		@Mapping(target = "refundedAmount",        ignore = true)
		@Mapping(target = "refunds",               ignore = true)
		@Mapping(target = "paidAt",                ignore = true)
		@Mapping(target = "refundedAt",            ignore = true)
		@Mapping(target = "receiptSent",           ignore = true)
		@Mapping(target = "createdAt",             ignore = true)
		@Mapping(target = "updatedAt",             ignore = true)
		@Mapping(target = "deletedAt",             ignore = true)
		@Mapping(target = "paymentMethodDetails",  source = "paymentMethodDetails")
		Payment toEntity(PaymentRequestDTO dto);
		
		@Mapping(target = "paymentMethodDetails", source = "paymentMethodDetails")
		@Mapping(target = "refunds",              source = "refunds")
		PaymentResponseDTO toResponse(Payment payment);
		
		List<PaymentResponseDTO> toResponseList(List<Payment> payments);
		
		PaymentMethodDetailsResponseDTO toPaymentMethodDetailsResponse(
				Payment.PaymentMethodDetails details
		);
		
		Payment.PaymentMethodDetails toPaymentMethodDetailsEntity(
				PaymentMethodDetailsDTO dto
		);
		
		RefundResponseDTO toRefundResponse( Payment.Refund refund);
		
		@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
		@Mapping(target = "id",                    ignore = true)
		@Mapping(target = "paymentNumber",         ignore = true)
		@Mapping(target = "transactionId",         ignore = true)
		@Mapping(target = "gatewayReference",      ignore = true)
		@Mapping(target = "gatewayResponseCode",   ignore = true)
		@Mapping(target = "gatewayResponseMessage",ignore = true)
		@Mapping(target = "refundedAmount",        ignore = true)
		@Mapping(target = "refunds",               ignore = true)
		@Mapping(target = "paidAt",                ignore = true)
		@Mapping(target = "refundedAt",            ignore = true)
		@Mapping(target = "receiptSent",           ignore = true)
		@Mapping(target = "createdAt",             ignore = true)
		@Mapping(target = "updatedAt",             ignore = true)
		@Mapping(target = "deletedAt",             ignore = true)
		void updateEntityFromDto(PaymentRequestDTO dto, @MappingTarget Payment payment);
}