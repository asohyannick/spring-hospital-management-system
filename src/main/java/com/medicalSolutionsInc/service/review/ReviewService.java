package com.medicalSolutionsInc.service.review;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.mappers.reviewMapper.ReviewMapper;
import com.medicalSolutionsInc.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
private final ReviewRepository reviewRepository;
private final ReviewMapper reviewMapper;
private final CloudinaryConfig cloudinaryConfig;
}
