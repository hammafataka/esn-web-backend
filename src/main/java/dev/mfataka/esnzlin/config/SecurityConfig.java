package dev.mfataka.esnzlin.config;

import static dev.mfataka.esnzlin.constants.JwtConstants.JWT_HEADER;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.config.security.JwtTokenFilter;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 18:21
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfig {
    @Value("${allowed.paths}")
    private String[] allowedPaths;

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    @SneakyThrows
    public SecurityWebFilterChain configure(final ServerHttpSecurity httpSecurity) {
        return httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(spec -> spec
                        .pathMatchers(allowedPaths).permitAll()
                        .anyExchange().authenticated()

                )
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .addFilterAt(jwtTokenFilter, SecurityWebFiltersOrder.HTTP_BASIC)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(
                        (exchange, authException) -> {
                            log.error("Unauthorized request", authException);
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        })
                )
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        ;
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Origin", "Authorization", "Content-Type", JWT_HEADER));
        configuration.addExposedHeader(JWT_HEADER);
        configuration.setAllowCredentials(true);
        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
