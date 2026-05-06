package com.medicalSolutionsInc.repository.payment;

import com.medicalSolutionsInc.entity.payment.Payment;
import com.medicalSolutionsInc.enumerations.paymentStatus.PaymentStatus;
import com.medicalSolutionsInc.enumerations.paymentType.PaymentType;
import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

		Optional<Payment> findByPaymentNumber(String paymentNumber);
		Optional<Payment> findByTransactionId(String transactionId);
		Optional<Payment> findByInvoiceNumber(String invoiceNumber);
		
		List<Payment> findByPatientId(String patientId);
		Page<Payment> findByPatientId(String patientId, Pageable pageable);
		List<Payment> findByPatientEmail(String patientEmail);
		
		List<Payment> findByStatus(PaymentStatus status);
		Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
		
		List<Payment> findByPaymentType(PaymentType paymentType);
		Page<Payment> findByPaymentType(PaymentType paymentType, Pageable pageable);
		
		List<Payment> findByReferenceId(String referenceId);
		List<Payment> findByReferenceType(ReferenceType referenceType);
		List<Payment> findByReferenceIdAndReferenceType(String referenceId, ReferenceType referenceType);
		
		List<Payment> findByPatientIdAndStatus(String patientId, PaymentStatus status);
		Page<Payment> findByPatientIdAndStatus(String patientId, PaymentStatus status, Pageable pageable);
		
		List<Payment> findByInsuranceClaim(boolean insuranceClaim);
		List<Payment> findByInsuranceClaimId(String insuranceClaimId);
		List<Payment> findByInsuranceProvider(String insuranceProvider);
		
		List<Payment> findByPartialPayment(boolean partialPayment);
		
		List<Payment> findByCreatedAtBetween(Instant from, Instant to);
		List<Payment> findByPaidAtBetween(Instant from, Instant to);
		Page<Payment> findByCreatedAtBetween(Instant from, Instant to, Pageable pageable);
		
		List<Payment> findByDueDateBefore(Instant date);
		List<Payment> findByDueDateBeforeAndStatus(Instant date, PaymentStatus status);
		
		List<Payment> findByAmountBetween(BigDecimal min, BigDecimal max);
		
		List<Payment> findByDeletedAtIsNull();
		Page<Payment> findByDeletedAtIsNull(Pageable pageable);
		
		List<Payment> findByDeletedAtIsNotNull();
		
		long countByStatus(PaymentStatus status);
		long countByPatientId(String patientId);
		long countByPaymentType(PaymentType paymentType);
		
		boolean existsByPaymentNumber(String paymentNumber);
		boolean existsByTransactionId(String transactionId);
		boolean existsByInvoiceNumber(String invoiceNumber);
		
		@Query("{ 'patient_id': ?0, 'deleted_at': null }")
		List<Payment> findActivePaymentsByPatientId(String patientId);
		
		@Query("{ 'status': ?0, 'deleted_at': null }")
		Page<Payment> findActivePaymentsByStatus(String status, Pageable pageable);
		
		@Query("{ 'patient_id': ?0, 'status': { $in: ?1 } }")
		List<Payment> findByPatientIdAndStatusIn(String patientId, List<PaymentStatus> statuses);
		
		@Query(value = "{ 'status': 'PENDING', 'due_date': { $lt: ?0 } }")
		List<Payment> findOverduePayments(Instant now);
		
		@Query("{ 'is_insurance_claim': true, 'status': ?0 }")
		List<Payment> findInsurancePaymentsByStatus(String status);
		
		@Query(value = "{ 'deleted_at': null }", count = true)
		long countActivePayments();
}