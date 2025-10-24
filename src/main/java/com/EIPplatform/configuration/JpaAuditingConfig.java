package com.EIPplatform.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl", dateTimeProviderRef = "dateTimeProvider")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorAwareImpl() {
        return new AuditorAwareImpl();
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
    }
}