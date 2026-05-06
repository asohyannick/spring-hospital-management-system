package com.medicalSolutionsInc.controller.pharmacyController;

import com.medicalSolutionsInc.service.pharmacy.PharmacyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/pharmacy")
@Tag ( name = "Pharmacy Management Endpoints", description = "")
public class PharmacyController {
  private final PharmacyService pharmacyService;
}
