package com.medicalSolutionsInc.service.payment;

import com.medicalSolutionsInc.mappers.paymentMapper.PaymentMapper;
import com.medicalSolutionsInc.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
private final PaymentRepository paymentRepository;
private final PaymentMapper paymentMapper;

}
