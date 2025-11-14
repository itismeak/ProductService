package com.microservice.product_service.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.product_service.common.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String SECRET;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Log the presence and type of Authorization header
        if (header == null) {
            log.warn("No Authorization header found in request.");
            handleException(response,
                    "You need to log in to access this page.",
                    HttpServletResponse.SC_UNAUTHORIZED);
            return;  // Prevent further filtering
        }

        if (!header.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header format. Expected 'Bearer <token>' but got: {}", header);
            handleException(response,
                    "Invalid Authorization header format.",
                    HttpServletResponse.SC_UNAUTHORIZED);
            return;  // Prevent further filtering
        }

        String token = header.substring(7); // Extract the token from the Authorization header
        log.info("Processing token from Authorization header.");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String role = (String) claims.get("role");

            // Log the successful token parsing
            log.info("Successfully parsed JWT. User: {}, Role: {}", email, role);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null,
                            List.of(new SimpleGrantedAuthority(role)));

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            // Log the error if token parsing fails
            log.error("Invalid JWT token. Error: {}", e.getMessage(), e);
            // Send an error response and stop further processing
            handleException(response,
                    "Invalid or expired token.",
                    HttpServletResponse.SC_UNAUTHORIZED);
            return;  // Prevent further filtering
        }

        filterChain.doFilter(request, response); // Continue the filter chain if everything is okay
    }

    private void handleException(HttpServletResponse response, String userFriendlyMessage, int status)
            throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(
                userFriendlyMessage,
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", status,
                        "error", HttpStatus.valueOf(status).getReasonPhrase(),
                        "message", userFriendlyMessage
                ),
                false
        );

        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}