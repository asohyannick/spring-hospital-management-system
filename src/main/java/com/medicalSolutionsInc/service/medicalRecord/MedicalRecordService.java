package com.medicalSolutionsInc.service.medicalRecord;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.mappers.medicalRecordMapper.MedicalRecordMapper;
import com.medicalSolutionsInc.repository.medicalRecord.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {
  private  final MedicalRecordRepository medicalRecordRepository;
  private final MedicalRecordMapper medicalRecordMapper;
  private final CloudinaryConfig cloudinaryConfig;
}
