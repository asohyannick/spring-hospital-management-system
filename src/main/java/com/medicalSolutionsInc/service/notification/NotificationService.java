package com.medicalSolutionsInc.service.notification;

import com.medicalSolutionsInc.mappers.notificationMapper.NotificationMapper;
import com.medicalSolutionsInc.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
 private final NotificationRepository notificationRepository;
 private final NotificationMapper notificationMapper;
}
