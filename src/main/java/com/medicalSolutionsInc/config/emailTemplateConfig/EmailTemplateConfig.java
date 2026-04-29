package com.medicalSolutionsInc.config.emailTemplateConfig;

import com.medicalSolutionsInc.exceptions.badRequestException.BadRequestException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailTemplateConfig {

			private final JavaMailSender mailSender;
			
			@Value("${mailtrap.from-email}")
			private String fromEmail;
			
			@Value("${mail.from-name}")
			private String fromName;
			
			@Value("${app.frontend-url}")
			private String frontendUrl;
			
			public void sendInvitationEmail(String toEmail, String inviteUrl) throws Exception {
				try {
					MimeMessage message = mailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
					
					helper.setFrom(fromEmail, fromName);
					helper.setTo(toEmail);
					helper.setSubject("You're invited to join NewLife Hospital Platform");
					helper.setText(buildInvitationHtml(inviteUrl), true);
					
					mailSender.send(message);
					log.info("Invitation email sent to {}", toEmail);
					
				} catch (Exception ex) {
					log.error("Failed to send invitation email to {}: {}", toEmail, ex.getMessage());
					throw new BadRequestException("Failed to send invitation to: " + toEmail, ex);
				}
			}
			
			public void sendMagicLinkEmail(String toEmail, String userName, String token) throws Exception {
				try {
					String magicLinkUrl = frontendUrl + "/auth/magic-link/verify?token=" + token;
					
					MimeMessage message = mailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
					
					helper.setFrom(fromEmail, fromName);
					helper.setTo(toEmail);
					helper.setSubject("✨ Your Magic Link Login – NewLife Hospital Platform");
					helper.setText(buildMagicLinkEmailBody(userName, magicLinkUrl), true);
					
					mailSender.send(message);
					log.info("Magic link email sent to {}", toEmail);
					
				} catch (Exception e) {
					log.error("Failed to send magic link email to {}: {}", toEmail, e.getMessage());
					throw new BadRequestException("Failed to send magic link email", e);
				}
			}
			
			public void sendSetPasswordEmail(String toEmail, String userName, String token) throws Exception {
				try {
					String setupUrl = frontendUrl + "/auth/create-password?token=" + token;
					
					MimeMessage message = mailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
					
					helper.setFrom(fromEmail, fromName);
					helper.setTo(toEmail);
					helper.setSubject("🔐 Set Your Password – NewLife Hospital Platform");
					helper.setText(buildSetPasswordEmailBody(userName, setupUrl), true);
					
					mailSender.send(message);
					log.info("Set-password email sent to {}", toEmail);
					
				} catch (Exception e) {
					log.error("Failed to send set-password email to {}: {}", toEmail, e.getMessage());
					throw new BadRequestException("Failed to send set-password email", e);
				}
			}
			
			public void sendPasswordResetEmail(String toEmail, String userName, String token) throws Exception {
				try {
					String resetUrl = frontendUrl + "/reset-password?token=" + token;
					
					MimeMessage message = mailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
					
					helper.setFrom(fromEmail, fromName);
					helper.setTo(toEmail);
					helper.setSubject("🔐 Reset Your Password – NewLife Hospital Platform");
					helper.setText(buildPasswordResetLinkEmailBody(userName, resetUrl), true);
					
					mailSender.send(message);
					log.info("Password reset email sent to {}", toEmail);
					
				} catch (Exception e) {
					log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
					throw new BadRequestException("Failed to send password reset email", e);
				}
			}
			
			public void sendAccountCreatedEmail(String toEmail, String userName, String token) throws Exception {
				try {
					String setupUrl = frontendUrl + "/auth/create-password?token=" + token;
					
					MimeMessage message = mailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
					
					helper.setFrom(fromEmail, fromName);
					helper.setTo(toEmail);
					helper.setSubject("🏥 Welcome to NewLife Hospital Platform – Set Up Your Account");
					helper.setText(buildAccountCreatedEmailBody(userName, setupUrl), true);
					
					mailSender.send(message);
					log.info("Account creation email sent to {}", toEmail);
					
				} catch (Exception e) {
					log.error("Failed to send account creation email to {}: {}", toEmail, e.getMessage());
					throw new BadRequestException("Failed to send account creation email", e);
				}
			}
			
			private String buildInvitationHtml(String inviteUrl) {
				return """
			                <!DOCTYPE html>
			                <html lang="en">
			                <head>
			                  <meta charset="UTF-8"/>
			                  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
			                  <title>NewLife Hospital Platform – Invitation</title>
			                </head>
			                <body style="margin:0;padding:0;background:#f4f6f8;font-family:Arial,Helvetica,sans-serif;">
			                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f4f6f8;padding:40px 0;">
			                    <tr>
			                      <td align="center">
			                        <table width="560" cellpadding="0" cellspacing="0"
			                               style="background:#ffffff;border-radius:10px;
			                                      box-shadow:0 2px 8px rgba(0,0,0,.08);overflow:hidden;">
			                          <tr>
			                            <td align="center" style="background:#0d6e4f;padding:32px 40px;">
			                              <h1 style="margin:0;color:#ffffff;font-size:24px;letter-spacing:.5px;">
			                                🏥 NewLife Hospital Platform
			                              </h1>
			                              <p style="margin:6px 0 0;color:#a7f3d0;font-size:13px;">
			                                Healthcare Management System
			                              </p>
			                            </td>
			                          </tr>
			                          <tr>
			                            <td style="padding:40px;">
			                              <h2 style="margin:0 0 12px;color:#1f2937;font-size:20px;">You've been invited!</h2>
			                              <p style="margin:0 0 24px;color:#4b5563;font-size:15px;line-height:1.6;">
			                                You have been invited to join <strong>NewLife Hospital Platform</strong>.
			                                Click the button below to set up your account and get started.
			                              </p>
			                              <table cellpadding="0" cellspacing="0">
			                                <tr>
			                                  <td align="center" style="background:#0d6e4f;border-radius:6px;">
			                                    <a href="%s"
			                                       style="display:inline-block;padding:14px 32px;color:#ffffff;
			                                              font-size:15px;font-weight:600;text-decoration:none;">
			                                      Accept Invitation
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			                              <p style="margin:24px 0 0;color:#9ca3af;font-size:13px;">
			                                This link expires in <strong>15 minutes</strong>. If you were not expecting
			                                this email, you can safely ignore it.
			                              </p>
			                            </td>
			                          </tr>
			                          <tr>
			                            <td align="center"
			                                style="background:#f9fafb;padding:20px 40px;border-top:1px solid #e5e7eb;">
			                              <p style="margin:0;color:#9ca3af;font-size:12px;">
			                                © 2026 NewLife Hospital Platform · All rights reserved
			                              </p>
			                            </td>
			                          </tr>
			                        </table>
			                      </td>
			                    </tr>
			                  </table>
			                </body>
			                </html>
			                """.formatted(inviteUrl);
			}
			
			private String buildAccountCreatedEmailBody(String userName, String setupUrl) {
				return """
			                <!DOCTYPE html>
			                <html lang="en">
			                <head>
			                  <meta charset="UTF-8"/>
			                  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
			                  <title>Welcome to NewLife Hospital Platform</title>
			                </head>
			                <body style="margin:0;padding:0;background:#f0f4f8;
			                             font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;">
			                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f0f4f8;padding:48px 20px;">
			                    <tr>
			                      <td align="center">
			                        <table width="620" cellpadding="0" cellspacing="0"
			                               style="background:#ffffff;border-radius:20px;
			                                      box-shadow:0 12px 40px rgba(0,0,0,0.10);overflow:hidden;">
			
			                          <!-- HEADER -->
			                          <tr>
			                            <td align="center"
			                                style="background:linear-gradient(135deg,#0d6e4f 0%%,#065f46 100%%);padding:44px 40px;">
			                              <p style="margin:0;font-size:26px;font-weight:900;color:#ffffff;letter-spacing:1px;">
			                                🏥 NewLife Hospital Platform
			                              </p>
			                              <p style="margin:8px 0 0;font-size:13px;color:#a7f3d0;">
			                                Healthcare Management System
			                              </p>
			                              <p style="margin:16px 0 0;font-size:48px;">🎉</p>
			                            </td>
			                          </tr>
			
			                          <!-- BODY -->
			                          <tr>
			                            <td style="padding:44px 40px;">
			                              <p style="margin:0 0 14px;font-size:22px;font-weight:700;color:#0d1b2a;">
			                                Welcome, %s 👋
			                              </p>
			                              <p style="margin:0 0 24px;font-size:15px;color:#555555;line-height:1.75;">
			                                Your account has been successfully created on
			                                <strong>NewLife Hospital Platform</strong>.
			                                To activate your account and get started, please set your password.
			                                This link is valid for <strong>15 minutes</strong>.
			                              </p>
			
			                              <!-- STEPS -->
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#ecfdf5;border-radius:12px;margin-bottom:32px;">
			                                <tr>
			                                  <td style="padding:24px 28px;">
			                                    <p style="margin:0 0 16px;font-size:13px;font-weight:700;color:#065f46;
			                                              text-transform:uppercase;letter-spacing:1.5px;">Getting Started</p>
			                                    <p style="margin:0 0 10px;font-size:14px;color:#374151;">
			                                      ① &nbsp; Click <strong>Activate My Account</strong> below
			                                    </p>
			                                    <p style="margin:0 0 10px;font-size:14px;color:#374151;">
			                                      ② &nbsp; Create a strong password for your account
			                                    </p>
			                                    <p style="margin:0;font-size:14px;color:#374151;">
			                                      ③ &nbsp; Log in and access the NewLife Hospital dashboard
			                                    </p>
			                                  </td>
			                                </tr>
			                              </table>
			
			                              <!-- CTA BUTTON -->
			                              <table cellpadding="0" cellspacing="0" style="margin:0 auto 32px;">
			                                <tr>
			                                  <td align="center"
			                                      style="background:linear-gradient(135deg,#0d6e4f 0%%,#065f46 100%%);
			                                             border-radius:50px;box-shadow:0 6px 20px rgba(13,110,79,0.38);">
			                                    <a href="%s"
			                                       style="display:inline-block;padding:16px 48px;color:#ffffff;
			                                              font-size:16px;font-weight:700;text-decoration:none;
			                                              border-radius:50px;letter-spacing:0.4px;">
			                                      🔐 Activate My Account
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			
			                              <!-- FALLBACK URL -->
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#f8f9fa;border:1px solid #e0e0e0;
			                                            border-radius:10px;margin-bottom:28px;">
			                                <tr>
			                                  <td style="padding:16px 20px;">
			                                    <p style="margin:0 0 8px;font-size:11px;font-weight:700;color:#9e9e9e;
			                                              text-transform:uppercase;letter-spacing:1.5px;">
			                                      Button not working? Copy and paste this link:
			                                    </p>
			                                    <a href="%s"
			                                       style="font-size:12px;color:#0d6e4f;text-decoration:none;word-break:break-all;">
			                                      %s
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			
			                              <!-- SECURITY NOTICE -->
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#fff8e1;border-left:4px solid #f59e0b;border-radius:8px;">
			                                <tr>
			                                  <td style="padding:16px 20px;font-size:13px;color:#6d4c41;line-height:1.7;">
			                                    ⚠️ <strong>Security Notice:</strong> This link is unique to your account and
			                                    expires in 15 minutes. Do not share it with anyone. If you did not expect
			                                    this email, please contact your system administrator immediately.
			                                  </td>
			                                </tr>
			                              </table>
			                            </td>
			                          </tr>
			
			                          <!-- FOOTER -->
			                          <tr>
			                            <td align="center"
			                                style="background:#f8f9fa;padding:26px 40px;border-top:1px solid #eeeeee;">
			                              <p style="margin:0;font-size:12px;color:#9e9e9e;line-height:1.9;">
			                                © 2026 <strong style="color:#0d6e4f;">NewLife Hospital Platform</strong>
			                                — Healthcare Management System<br/>
			                                This is an automated message. Please do not reply to this email.
			                              </p>
			                            </td>
			                          </tr>
			
			                        </table>
			                      </td>
			                    </tr>
			                  </table>
			                </body>
			                </html>
			                """.formatted(userName, setupUrl, setupUrl, setupUrl);
			}
			
			private String buildSetPasswordEmailBody(String userName, String setupUrl) {
				return """
			                <!DOCTYPE html>
			                <html lang="en">
			                <head>
			                  <meta charset="UTF-8"/>
			                  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
			                  <title>Set Your Password – NewLife Hospital Platform</title>
			                </head>
			                <body style="margin:0;padding:0;background:#f0f4f8;
			                             font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;">
			                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f0f4f8;padding:48px 20px;">
			                    <tr>
			                      <td align="center">
			                        <table width="620" cellpadding="0" cellspacing="0"
			                               style="background:#ffffff;border-radius:20px;
			                                      box-shadow:0 12px 40px rgba(0,0,0,0.10);overflow:hidden;">
			                          <tr>
			                            <td align="center"
			                                style="background:linear-gradient(135deg,#0d6e4f 0%%,#065f46 100%%);padding:44px 40px;">
			                              <p style="margin:0;font-size:26px;font-weight:900;color:#ffffff;">
			                                🏥 NewLife Hospital Platform
			                              </p>
			                              <p style="margin:8px 0 0;font-size:13px;color:#a7f3d0;">Secure Account Setup</p>
			                              <p style="margin:16px 0 0;font-size:48px;">🔐</p>
			                            </td>
			                          </tr>
			                          <tr>
			                            <td style="padding:44px 40px;">
			                              <p style="margin:0 0 14px;font-size:22px;font-weight:700;color:#0d1b2a;">
			                                Hello, %s 👋
			                              </p>
			                              <p style="margin:0 0 32px;font-size:15px;color:#555;line-height:1.75;">
			                                Your account setup on <strong>NewLife Hospital Platform</strong> is almost complete.
			                                Click below to set your password. This link is valid for
			                                <strong>15 minutes</strong> and can only be used once.
			                              </p>
			                              <table cellpadding="0" cellspacing="0" style="margin:0 auto 32px;">
			                                <tr>
			                                  <td align="center"
			                                      style="background:linear-gradient(135deg,#0d6e4f 0%%,#065f46 100%%);
			                                             border-radius:50px;box-shadow:0 6px 20px rgba(13,110,79,0.38);">
			                                    <a href="%s"
			                                       style="display:inline-block;padding:16px 48px;color:#ffffff;
			                                              font-size:16px;font-weight:700;text-decoration:none;border-radius:50px;">
			                                      Set My Password
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#f8f9fa;border:1px solid #e0e0e0;
			                                            border-radius:10px;margin-bottom:28px;">
			                                <tr>
			                                  <td style="padding:16px 20px;">
			                                    <p style="margin:0 0 8px;font-size:11px;font-weight:700;
			                                              color:#9e9e9e;text-transform:uppercase;letter-spacing:1.5px;">
			                                      Button not working? Copy and paste this link:
			                                    </p>
			                                    <a href="%s"
			                                       style="font-size:12px;color:#0d6e4f;text-decoration:none;word-break:break-all;">
			                                      %s
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#fff8e1;border-left:4px solid #f59e0b;border-radius:8px;">
			                                <tr>
			                                  <td style="padding:16px 20px;font-size:13px;color:#6d4c41;line-height:1.7;">
			                                    ⚠️ <strong>Security Notice:</strong> This link is unique to your account.
			                                    Do not share it with anyone. If you did not expect this email,
			                                    contact your administrator immediately.
			                                  </td>
			                                </tr>
			                              </table>
			                            </td>
			                          </tr>
			                          <tr>
			                            <td align="center"
			                                style="background:#f8f9fa;padding:26px 40px;border-top:1px solid #eeeeee;">
			                              <p style="margin:0;font-size:12px;color:#9e9e9e;">
			                                © 2026 <strong style="color:#0d6e4f;">NewLife Hospital Platform</strong>
			                                · All rights reserved
			                              </p>
			                            </td>
			                          </tr>
			                        </table>
			                      </td>
			                    </tr>
			                  </table>
			                </body>
			                </html>
			                """.formatted(userName, setupUrl, setupUrl, setupUrl);
			}
			
			private String buildMagicLinkEmailBody(String userName, String magicLinkUrl) {
				return """
			                <!DOCTYPE html>
			                <html lang="en">
			                <head>
			                  <meta charset="UTF-8"/>
			                  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
			                  <title>Magic Link Login – NewLife Hospital Platform</title>
			                </head>
			                <body style="margin:0;padding:0;background:#f0f4f8;
			                             font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;">
			                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f0f4f8;padding:48px 20px;">
			                    <tr>
			                      <td align="center">
			                        <table width="620" cellpadding="0" cellspacing="0"
			                               style="background:#ffffff;border-radius:20px;
			                                      box-shadow:0 8px 30px rgba(0,0,0,0.08);overflow:hidden;">
			                          <tr>
			                            <td align="center"
			                                style="background:linear-gradient(135deg,#0d6e4f 0%%,#065f46 100%%);padding:40px;">
			                              <p style="margin:0;font-size:26px;font-weight:800;color:#ffffff;">
			                                🏥 NewLife Hospital Platform
			                              </p>
			                              <p style="margin:6px 0 0;font-size:13px;color:#a7f3d0;">
			                                Healthcare Management System
			                              </p>
			                              <p style="margin:16px 0 0;font-size:48px;">✨</p>
			                            </td>
			                          </tr>
			                          <tr>
			                            <td style="padding:40px;">
			                              <p style="margin:0 0 12px;font-size:22px;font-weight:700;color:#1a1a2e;">
			                                Hello, %s 👋
			                              </p>
			                              <p style="margin:0 0 32px;font-size:15px;color:#555555;line-height:1.7;">
			                                You requested a <strong>Magic Link</strong> to sign in to your
			                                <strong>NewLife Hospital Platform</strong> account.
			                                Click the button below to log in instantly — no password required.
			                                This link is valid for <strong>5 minutes</strong> and can only be used once.
			                              </p>
			                              <table cellpadding="0" cellspacing="0" style="margin:0 auto 16px;">
			                                <tr>
			                                  <td align="center"
			                                      style="background:linear-gradient(135deg,#0d6e4f 0%%,#065f46 100%%);
			                                             border-radius:50px;box-shadow:0 4px 20px rgba(13,110,79,0.35);">
			                                    <a href="%s"
			                                       style="display:inline-block;padding:16px 40px;color:#ffffff;
			                                              font-size:16px;font-weight:700;text-decoration:none;border-radius:50px;">
			                                      ✨ Login with Magic Link
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			                              <p style="text-align:center;margin:0 0 28px;">
			                                <span style="display:inline-block;background:#fce4ec;color:#c62828;
			                                             font-size:13px;font-weight:600;padding:6px 16px;border-radius:50px;">
			                                  ⏱ Expires in 5 minutes
			                                </span>
			                              </p>
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#f5f5f5;border:1px solid #e0e0e0;
			                                            border-radius:8px;margin-bottom:28px;">
			                                <tr>
			                                  <td style="padding:14px 18px;">
			                                    <p style="margin:0 0 6px;font-size:11px;color:#9e9e9e;
			                                              text-transform:uppercase;letter-spacing:1px;font-weight:600;">
			                                      Or copy and paste this link:
			                                    </p>
			                                    <a href="%s"
			                                       style="font-size:12px;color:#0d6e4f;text-decoration:none;word-break:break-all;">
			                                      %s
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#fff8e1;border-left:4px solid #f9a825;border-radius:6px;">
			                                <tr>
			                                  <td style="padding:14px 18px;font-size:13px;color:#795548;line-height:1.6;">
			                                    ⚠️ <strong>Security Notice:</strong> This link grants immediate access to
			                                    your account. Never forward or share this email. If you did not request
			                                    this, ignore this email — your account remains secure.
			                                  </td>
			                                </tr>
			                              </table>
			                            </td>
			                          </tr>
			                          <tr>
			                            <td align="center"
			                                style="background:#f8f9fa;padding:24px 40px;border-top:1px solid #eeeeee;">
			                              <p style="margin:0;font-size:12px;color:#9e9e9e;line-height:1.8;">
			                                © 2026 <strong style="color:#0d6e4f;">NewLife Hospital Platform</strong>
			                                — Healthcare Management System<br/>
			                                This is an automated message. Please do not reply.
			                              </p>
			                            </td>
			                          </tr>
			                        </table>
			                      </td>
			                    </tr>
			                  </table>
			                </body>
			                </html>
			                """.formatted(userName, magicLinkUrl, magicLinkUrl, magicLinkUrl);
			}
			
			private String buildPasswordResetLinkEmailBody(String userName, String resetUrl) {
				return """
			                <!DOCTYPE html>
			                <html lang="en">
			                <head>
			                  <meta charset="UTF-8"/>
			                  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
			                  <title>Reset Your Password – NewLife Hospital Platform</title>
			                </head>
			                <body style="margin:0;padding:0;background:#eef2f7;
			                             font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;">
			                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#eef2f7;padding:48px 20px;">
			                    <tr>
			                      <td align="center">
			                        <table width="620" cellpadding="0" cellspacing="0"
			                               style="background:#ffffff;border-radius:20px;
			                                      box-shadow:0 12px 40px rgba(0,0,0,0.10);overflow:hidden;">
			                          <tr>
			                            <td align="center"
			                                style="background:linear-gradient(135deg,#0d6e4f 0%%,#065f46 100%%);padding:44px 40px;">
			                              <p style="margin:0;font-size:26px;font-weight:900;color:#ffffff;">
			                                🏥 NewLife Hospital Platform
			                              </p>
			                              <p style="margin:8px 0 0;font-size:13px;color:#a7f3d0;">Secure Password Recovery</p>
			                              <p style="margin:20px 0 0;font-size:52px;">🛡️</p>
			                            </td>
			                          </tr>
			                          <tr>
			                            <td style="padding:44px 40px;">
			                              <p style="margin:0 0 14px;font-size:23px;font-weight:700;color:#0d1b2a;">
			                                Hello, %s 👋
			                              </p>
			                              <p style="margin:0 0 32px;font-size:15px;color:#555555;line-height:1.75;">
			                                We received a request to <strong>reset the password</strong> for your
			                                <strong>NewLife Hospital Platform</strong> account.
			                                This link is valid for <strong>5 minutes</strong>
			                                and can only be used <strong>once</strong>.
			                              </p>
			                              <table cellpadding="0" cellspacing="0" style="margin:0 auto 28px;">
			                                <tr>
			                                  <td align="center"
			                                      style="background:linear-gradient(135deg,#0d6e4f 0%%,#065f46 100%%);
			                                             border-radius:50px;box-shadow:0 6px 20px rgba(13,110,79,0.38);">
			                                    <a href="%s"
			                                       style="display:inline-block;padding:16px 48px;color:#ffffff;
			                                              font-size:16px;font-weight:700;text-decoration:none;border-radius:50px;">
			                                      Reset My Password
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#f8f9fa;border:1px solid #e0e0e0;
			                                            border-radius:10px;margin-bottom:32px;">
			                                <tr>
			                                  <td style="padding:16px 20px;">
			                                    <p style="margin:0 0 8px;font-size:11px;font-weight:700;
			                                              color:#9e9e9e;text-transform:uppercase;letter-spacing:1.5px;">
			                                      Button not working? Copy and paste this link:
			                                    </p>
			                                    <a href="%s"
			                                       style="font-size:12px;color:#0d6e4f;text-decoration:none;word-break:break-all;">
			                                      %s
			                                    </a>
			                                  </td>
			                                </tr>
			                              </table>
			                              <table width="100%%" cellpadding="0" cellspacing="0"
			                                     style="background:#fff8e1;border-left:4px solid #f59e0b;border-radius:8px;">
			                                <tr>
			                                  <td style="padding:16px 20px;font-size:13px;color:#6d4c41;line-height:1.7;">
			                                    ⚠️ <strong>Security Notice:</strong> If you did not request a password reset,
			                                    ignore this email — your account remains secure. Never share this link.
			                                    It expires automatically after 5 minutes.
			                                  </td>
			                                </tr>
			                              </table>
			                            </td>
			                          </tr>
			                          <tr>
			                            <td align="center"
			                                style="background:#f8f9fa;padding:26px 40px;border-top:1px solid #eeeeee;">
			                              <p style="margin:0;font-size:12px;color:#9e9e9e;line-height:1.9;">
			                                © 2026 <strong style="color:#0d6e4f;">NewLife Hospital Platform</strong>
			                                — Healthcare Management System<br/>
			                                This is an automated message. Please do not reply to this email.
			                              </p>
			                            </td>
			                          </tr>
			                        </table>
			                      </td>
			                    </tr>
			                  </table>
			                </body>
			                </html>
			                """.formatted(userName, resetUrl, resetUrl, resetUrl);
			}
}