package com.medicalSolutionsInc.controller.medicalRecordController;

import com.medicalSolutionsInc.service.medicalRecord.MedicalRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/medical-record")
@RequiredArgsConstructor
@Tag ( name = "Medical Record Management Endpoints", description = "")
public class MedicalRecordController {
 private  final MedicalRecordService medicalRecordService;
 
}
