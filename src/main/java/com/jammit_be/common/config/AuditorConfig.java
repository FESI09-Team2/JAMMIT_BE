package com.jammit_be.common.config;

import com.jammit_be.auth.entity.CustomUserDetail;
import com.jammit_be.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class AuditorConfig {

    @Bean
    public AuditorAwareImpl auditorAware() {
        return new AuditorAwareImpl();
    }

    public class AuditorAwareImpl implements AuditorAware<User> {
        @Override
        public @NotNull Optional<User> getCurrentAuditor() {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (
                    authentication == null
                            || !authentication.isAuthenticated()
                            || authentication.getPrincipal().equals("anonymousUser")
            ) return Optional.empty();
            var userDetail = (CustomUserDetail) authentication.getPrincipal();
            return Optional.of(userDetail.getUser());
        }
    }
}
