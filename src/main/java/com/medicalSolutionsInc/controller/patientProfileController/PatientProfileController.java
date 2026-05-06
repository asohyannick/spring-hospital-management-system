package com.medicalSolutionsInc.controller.patientProfileController;

import com.medicalSolutionsInc.service.patientProfile.PatientProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/patient")
@Tag ( name = "Patient Profile Management Endpoints", description = "")
public class PatientProfileController {
 private final PatientProfileService patientProfileService;
 
}
