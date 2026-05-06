package com.medicalSolutionsInc.service.patientProfile;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.mappers.patientProfileMapper.PatientProfileMapper;
import com.medicalSolutionsInc.repository.patientProfile.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientProfileService {
 private final PatientProfileRepository  patientProfileRepository;
 private final PatientProfileMapper patientProfileMapper;
 private final CloudinaryConfig cloudinaryConfig;
}
