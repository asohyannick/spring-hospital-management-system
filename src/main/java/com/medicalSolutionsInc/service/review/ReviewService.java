package com.medicalSolutionsInc.service.review;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.dto.reviewDTO.CreateReviewRequestDTO;
import com.medicalSolutionsInc.dto.reviewDTO.CreateReviewResponseDTO;
import com.medicalSolutionsInc.entity.review.Review;
import com.medicalSolutionsInc.enumerations.reviewStatus.ReviewStatus;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;
import com.medicalSolutionsInc.exceptions.notFoundException.NotFoundException;
import com.medicalSolutionsInc.mappers.reviewMapper.ReviewMapper;
import com.medicalSolutionsInc.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

			private final ReviewRepository reviewRepository;
			private final ReviewMapper reviewMapper;
			private final CloudinaryConfig cloudinaryConfig;
			
			public CreateReviewResponseDTO addReview(
					CreateReviewRequestDTO request,
					List<MultipartFile> attachmentFiles
			) throws Exception {
				log.info("Creating review by patient: {} for target: {}", request.patientId(), request.targetId());
				
				Review review = reviewMapper.toEntity(request);
				
				if (request.patientImageUrl() == null && attachmentFiles != null && !attachmentFiles.isEmpty()) {
					List<Review.Attachment> attachments = new ArrayList<>();
					for (MultipartFile file : attachmentFiles) {
						if (file != null && !file.isEmpty()) {
							String fileUrl = cloudinaryConfig.uploadImage(
									file,
									"reviews/attachments",
									"review_attachment_" + UUID.randomUUID(),
									"Review attachment"
							);
							attachments.add(Review.Attachment.builder()
									                .fileName(file.getOriginalFilename())
									                .fileUrl(fileUrl)
									                .fileType(file.getContentType())
									                .fileSizeBytes(file.getSize())
									                .uploadedAt(Instant.now())
									                .build()
							);
						}
					}
					review.setAttachments(attachments);
				}
				
				if (request.patientImageUrl() != null && !request.patientImageUrl().isBlank()) {
					review.setPatientImageUrl(request.patientImageUrl());
				}
				
				Review saved = reviewRepository.save(review);
				log.info("Review created with ID: {}", saved.getId());
				return reviewMapper.toResponseDTO(saved);
			}
			
			
			public Page<CreateReviewResponseDTO> fetchReviews(Pageable pageable) {
				log.info("Fetching all reviews — page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
				return reviewRepository.findByDeletedAtIsNull(pageable)
						       .map(reviewMapper::toResponseDTO);
			}
			
			public CreateReviewResponseDTO fetchReview(String id) throws  Exception{
				log.info("Fetching review with ID: {}", id);
				Review review = reviewRepository.findByIdAndDeletedAtIsNull(id)
						                .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
				return reviewMapper.toResponseDTO(review);
			}
			
			public CreateReviewResponseDTO updateReview(
					String id,
					CreateReviewRequestDTO request,
					List<MultipartFile> newAttachmentFiles
			) throws Exception {
				log.info("Updating review with ID: {}", id);
				
				Review review = reviewRepository.findByIdAndDeletedAtIsNull(id)
						                .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
				
				if (newAttachmentFiles != null && !newAttachmentFiles.isEmpty()) {
					if (review.getAttachments() != null) {
						review.getAttachments().forEach(att -> cloudinaryConfig.deleteByUrl(att.getFileUrl()));
					}
					
					List<Review.Attachment> updatedAttachments = new ArrayList<>();
					for (MultipartFile file : newAttachmentFiles) {
						if (file != null && !file.isEmpty()) {
							String fileUrl = cloudinaryConfig.uploadImage(
									file,
									"reviews/attachments",
									"review_attachment_" + UUID.randomUUID(),
									"Review attachment"
							);
							updatedAttachments.add(Review.Attachment.builder()
									                       .fileName(file.getOriginalFilename())
									                       .fileUrl(fileUrl)
									                       .fileType(file.getContentType())
									                       .fileSizeBytes(file.getSize())
									                       .uploadedAt(Instant.now())
									                       .build()
							);
						}
					}
					review.setAttachments(updatedAttachments);
				}
				
				review.setTitle(request.title());
				review.setComment(request.comment());
				review.setRating(request.rating());
				review.setTags(request.tags());
				review.setEdited(true);
				review.setEditedAt(Instant.now());
				
				Review updated = reviewRepository.save(review);
				log.info("Review updated successfully: {}", id);
				return reviewMapper.toResponseDTO(updated);
			}
			
			public void deleteReview(String id) throws Exception {
				log.info("Soft-deleting review with ID: {}", id);
				Review review = reviewRepository.findByIdAndDeletedAtIsNull(id)
						                .orElseThrow(() -> new NotFoundException ("Review not found with ID: " + id));
				
				review.setDeletedAt(Instant.now());
				reviewRepository.save(review);
				log.info("Review soft-deleted successfully: {}", id);
			}
			
			public long countReviews() {
				log.info("Counting all active reviews");
				return reviewRepository.countByDeletedAtIsNull();
			}
			
			public Page<CreateReviewResponseDTO> searchReviews(
					String keyword,
					ReviewTargetType targetType,
					String targetId,
					String patientId,
					ReviewStatus status,
					Integer minRating,
					Integer maxRating,
					Boolean verifiedVisit,
					Boolean featured,
					Instant from,
					Instant to,
					Pageable pageable
			) {
				log.info("Searching reviews — keyword: '{}', targetType: {}, status: {}", keyword, targetType, status);
				return reviewRepository.search(
						keyword, targetType, targetId, patientId,
						status, minRating, maxRating, verifiedVisit,
						featured, from, to, pageable
				).map(reviewMapper::toResponseDTO);
			}
}