package com.medicalSolutionsInc.service.ward;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.mappers.wardMapper.WardMapper;
import com.medicalSolutionsInc.repository.ward.WardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WardService {
	 private final WardRepository wardRepository;
	 private final WardMapper wardMapper;
	 private final CloudinaryConfig cloudinaryConfig;
}
