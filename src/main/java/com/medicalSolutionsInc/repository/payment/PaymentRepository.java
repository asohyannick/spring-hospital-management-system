package com.medicalSolutionsInc.repository.payment;

import com.medicalSolutionsInc.entity.payment.Payment;
import com.medicalSolutionsInc.enumerations.paymentStatus.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
	
		@Query("{ 'deleted_at': null }")
		Page<Payment> findAllActive(Pageable pageable);
		
		/** Non-deleted payments for a specific patient, paged. */
		@Query("{ 'patient_id': ?0, 'deleted_at': null }")
		Page<Payment> findAllByPatientId(String patientId, Pageable pageable);
		
		/** Non-deleted payments filtered by status, paged. */
		@Query("{ 'status': ?0, 'deleted_at': null }")
		Page<Payment> findAllByStatus(PaymentStatus status, Pageable pageable);
		
		/** Non-deleted payments in a date range, paged. */
		@Query("{ 'created_at': { $gte: ?0, $lte: ?1 }, 'deleted_at': null }")
		Page<Payment> findAllByCreatedAtBetween(Instant from, Instant to, Pageable pageable);
		
		/** Non-deleted payments for a patient filtered by status, paged. */
		@Query("{ 'patient_id': ?0, 'status': ?1, 'deleted_at': null }")
		Page<Payment> findAllByPatientIdAndStatus(String patientId, PaymentStatus status, Pageable pageable);
		
		// ── Single-record finders ────────────────────────────────────────────────
		
		Optional<Payment> findByIdAndDeletedAtIsNull(String id);
		
		Optional<Payment> findByPaymentNumberAndDeletedAtIsNull(String paymentNumber);
		
		Optional<Payment> findByTransactionIdAndDeletedAtIsNull(String transactionId);
		
		Optional<Payment> findByInvoiceNumberAndDeletedAtIsNull(String invoiceNumber);
		
		// ── Existence checks ─────────────────────────────────────────────────────
		
		boolean existsByInvoiceNumberAndDeletedAtIsNull(String invoiceNumber);
		
		boolean existsByTransactionIdAndDeletedAtIsNull(String transactionId);
}