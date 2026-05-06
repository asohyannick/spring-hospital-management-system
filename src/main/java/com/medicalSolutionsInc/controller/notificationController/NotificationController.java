package com.medicalSolutionsInc.controller.notificationController;

import com.medicalSolutionsInc.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/notify")
@Tag ( name = "Notification Management Endpoints", description = "")
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationService  notificationService;
}
