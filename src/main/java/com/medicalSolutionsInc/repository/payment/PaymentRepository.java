package com.medicalSolutionsInc.repository.payment;

import com.medicalSolutionsInc.entity.payment.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

		

}