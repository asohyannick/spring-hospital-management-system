package com.medicalSolutionsInc.config.swaggerConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hospital Management System Made with 💖 By Asoh Yannick, Java Backend Developer")
                        .version("v1.0.0")
                        .description("""
                                Welcome to the NewLife Hospital Management System API

                                This backend powers:
                                • User & role management  
                                • Patient records  
                                • Appointments & scheduling  
                                • Billing & payments  
                                • Medical inventory management  

                                Built with Java, Spring Boot, Spring Security, JWT, Hibernate, and MongoDB
                                by Java Backend Developer **Asoh Yannick**.

                                Use this documentation to explore, test, and integrate all endpoints confidently
                                into your healthcare solutions.
                                """)
                        .contact(new Contact()
                                .name("Asoh Yannick, JavaBoy")
                                .email("keepcoding200@gmail.com")
                                .url("https://www.linkedin.com/in/asohyannick/"))
                        .license(new License()
                                .name("MIT License ❤️")
                                .url("https://opensource.org/licenses/MIT")))
                .addTagsItem(new Tag()
                        .name("Authentication & User Management Endpoints")
                        .description("APIs for managing users, roles, and authentication"))
                .addTagsItem(new Tag()
                        .name("Patient Records")
                        .description("APIs for managing patient profiles and medical records"))
                .addTagsItem(new Tag()
                        .name("Appointments")
                        .description("APIs for scheduling and managing appointments"))
                .addTagsItem(new Tag()
                        .name("Billing")
                        .description("APIs for handling invoices, billing, and payments"))
                .addTagsItem(new Tag()
                        .name("Inventory")
                        .description("APIs for managing medical inventory and supplies"));
    }
}

