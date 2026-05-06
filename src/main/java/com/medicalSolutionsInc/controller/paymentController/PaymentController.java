package com.medicalSolutionsInc.controller.paymentController;

import com.medicalSolutionsInc.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/payment")
@RequiredArgsConstructor
@Tag ( name = "", description = "")
public class PaymentController {
 private final PaymentService paymentService;
 
}
