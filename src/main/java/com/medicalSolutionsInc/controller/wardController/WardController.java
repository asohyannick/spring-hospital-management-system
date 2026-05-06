package com.medicalSolutionsInc.controller.wardController;

import com.medicalSolutionsInc.service.ward.WardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/ward")
@RequiredArgsConstructor
@Tag ( name = "Ward and Bed Management Endpoints", description = "")
public class WardController {
 private final WardService wardService;
 
}
