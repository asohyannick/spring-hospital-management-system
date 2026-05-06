package com.medicalSolutionsInc.entity.review;

import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import com.medicalSolutionsInc.enumerations.reviewStatus.ReviewStatus;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review {

		@Id
		private String id;
		
		@Indexed(unique = true)
		@Field("review_number")
		private String reviewNumber;
		
		@Field("title")
		private String title;
		
		@Field("comment")
		private String comment;
		
		@Field("rating")
		private int rating;
		
		@Field("status")
		@Builder.Default
		private ReviewStatus status = ReviewStatus.PENDING;
		
		@Indexed
		@Field("patient_id")
		private String patientId;
		
		@Field("patient_name")
		private String patientName;
		
		@Field("patient_image_url")
		private String patientImageUrl;
		
		@Indexed
		@Field("target_id")
		private String targetId;
		
		@Field("target_name")
		private String targetName;
		
		@Field("target_type")
		private ReviewTargetType targetType;
		
		@Field("reference_id")
		private String referenceId;
		
		@Field("reference_type")
		private ReferenceType referenceType;
		
		@Field("helpful_votes")
		@Builder.Default
		private int helpfulVotes = 0;
		
		@Field("reported_count")
		@Builder.Default
		private int reportedCount = 0;
		
		@Field("tags")
		@Builder.Default
		private List<String> tags = new ArrayList<>();
		
		@Field("attachments")
		@Builder.Default
		private List<Attachment> attachments = new ArrayList<>();
		
		@Field("response")
		private ReviewResponse response;
		
		@Field("is_verified_visit")
		@Builder.Default
		private boolean verifiedVisit = false;
		
		@Field("is_anonymous")
		@Builder.Default
		private boolean anonymous = false;
		
		@Field("is_featured")
		@Builder.Default
		private boolean featured = false;
		
		@Field("is_edited")
		@Builder.Default
		private boolean edited = false;
		
		@Field("edited_at")
		private Instant editedAt;
		
		@CreatedDate
		@Field("created_at")
		private Instant createdAt;
		
		@LastModifiedDate
		@Field("updated_at")
		private Instant updatedAt;
		
		@Field("deleted_at")
		private Instant deletedAt;
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class Attachment {
			private String fileName;
			private String fileUrl;
			private String fileType;
			private long fileSizeBytes;
			private Instant uploadedAt;
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class ReviewResponse {
			private String responderId;
			private String responderName;
			private String responderRole;
			private String message;
			private Instant respondedAt;
			private boolean edited;
			private Instant editedAt;
		}


}