package com.medicalSolutionsInc.service.staffProfile;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.mappers.staffProfileMapper.StaffProfileMapper;
import com.medicalSolutionsInc.repository.staffProfile.StaffProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class StaffProfileService {
 private final StaffProfileRepository staffProfileRepository;
 private final StaffProfileMapper staffProfileMapper;
 private final CloudinaryConfig cloudinaryConfig;
}
