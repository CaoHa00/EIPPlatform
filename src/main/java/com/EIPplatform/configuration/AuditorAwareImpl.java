package com.EIPplatform.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        try {
            Authentication authentication = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            if (authentication == null ||
                    !authentication.isAuthenticated() ||
                    authentication instanceof AnonymousAuthenticationToken) {

                return Optional.of("ADMIN");
            }

            if (authentication.getPrincipal() instanceof Jwt jwt) {

                String fullName = jwt.getClaim("fullName");

                if (fullName != null && !fullName.trim().isEmpty()) {
                    return Optional.of(fullName);
                }

                String subject = jwt.getSubject();
                return Optional.ofNullable(subject);
            }

            String principalName = authentication.getName();
            return Optional.of(principalName);

        } catch (Exception e) {
            log.error("Error getting current auditor, using SYSTEM", e);
            return Optional.of("SYSTEM");
        }
    }
}