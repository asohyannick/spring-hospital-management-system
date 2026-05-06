package com.medicalSolutionsInc.service.pharmacy;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.mappers.pharmacyMapper.PharmacyMapper;
import com.medicalSolutionsInc.repository.pharmacy.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PharmacyService {
private final PharmacyRepository pharmacyRepository;
private final PharmacyMapper pharmacyMapper;
private final CloudinaryConfig cloudinaryConfig;
}
