package com.microservice.product_service.common.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImp implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //If you have Spring Security, you can replace "system"
        // with SecurityContextHolder.getContext().getAuthentication().getName().

        // In real apps, fetch the logged-in username from Spring Security
        return Optional.of("system"); // default/fallback
    }
}