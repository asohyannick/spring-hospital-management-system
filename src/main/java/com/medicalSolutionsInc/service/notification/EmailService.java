package com.medicalSolutionsInc.service.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

		private final JavaMailSender mailSender;
		
		/**
		 * Sends an HTML email asynchronously.
		 *
		 * @param to      recipient email address
		 * @param subject email subject line
		 * @param body    HTML body content
		 */
		@Async
		public void sendEmail(String to, String subject, String body) {
			try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				
				helper.setTo(to);
				helper.setSubject(subject);
				helper.setText(body, true);
				
				mailSender.send(message);
				log.info("Email sent successfully to: {}", to);
				
			} catch (MessagingException e) {
				log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
				throw new RuntimeException("Email delivery failed for: " + to, e);
			}
		}
		
		/**
		 * Builds a simple styled HTML template for notification emails.
		 */
		public String buildHtmlTemplate(String recipientName, String title, String messageBody, String actionUrl) {
			String actionButton = (actionUrl != null && !actionUrl.isBlank())
					                      ? "<a href=\"" + actionUrl + "\" style=\"display:inline-block;padding:10px 20px;" +
							                        "background:#2563EB;color:#fff;border-radius:6px;text-decoration:none;font-weight:bold;\">" +
							                        "Take Action</a>"
					                      : "";
			
			return """
		                <!DOCTYPE html>
		                <html>
		                  <body style="font-family:Arial,sans-serif;background:#f4f4f4;padding:20px;">
		                    <div style="max-width:600px;margin:auto;background:#fff;border-radius:8px;padding:30px;">
		                      <h2 style="color:#1e293b;">%s</h2>
		                      <p style="color:#475569;">Hello %s,</p>
		                      <p style="color:#475569;line-height:1.6;">%s</p>
		                      %s
		                      <hr style="border:none;border-top:1px solid #e2e8f0;margin:24px 0;"/>
		                      <p style="font-size:12px;color:#94a3b8;">
		                        This is an automated message from Medical Solutions Inc. Please do not reply.
		                      </p>
		                    </div>
		                  </body>
		                </html>
		                """.formatted(title, recipientName != null ? recipientName : "Customer", messageBody, actionButton);
		}
}
