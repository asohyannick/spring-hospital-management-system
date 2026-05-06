package com.medicalSolutionsInc.controller.staffProfileController;

import com.medicalSolutionsInc.service.staffProfile.StaffProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/staff-profile")
@RequiredArgsConstructor
@Tag ( name = "Staff Profile Management Endpoints", description = "")
public class StaffProfileController {

    private final StaffProfileService staffProfileService;
	
}
