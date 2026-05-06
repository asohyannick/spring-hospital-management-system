package com.medicalSolutionsInc.controller.reviewController;

import com.medicalSolutionsInc.service.review.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/review")
@RequiredArgsConstructor
@Tag ( name = "Review Management Endpoints", description = "")
public class ReviewController {

 private final ReviewService reviewService;
 
}
