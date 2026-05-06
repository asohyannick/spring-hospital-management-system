package com.medicalSolutionsInc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
		exclude = {
		   RedisRepositoriesAutoConfiguration.class,
		}
)
@EnableCaching
@EnableMongoAuditing
@EnableMongoRepositories (basePackages = "com.medicalSolutionsInc.repository")
@EnableSpringDataWebSupport (
		pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
@EnableAsync
public class HealthCareApplication {
	public static void main(String[] args) {
		SpringApplication.run(HealthCareApplication.class, args);
	}
}
