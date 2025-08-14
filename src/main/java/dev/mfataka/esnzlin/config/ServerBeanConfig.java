package dev.mfataka.esnzlin.config;

import static org.zalando.logbook.core.BodyFilters.defaultValue;
import static org.zalando.logbook.json.JsonBodyFilters.replacePrimitiveJsonProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zalando.logbook.BodyFilter;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import dev.mfataka.esnzlin.jpa.repository.UserRepository;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 18:21
 */
@Configuration
@EnableR2dbcRepositories(basePackages = "iam.mfa.esnzlin.jpa")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerBeanConfig {

    private final UserRepository userRepository;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .map(user -> (UserDetails) user)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("User not found: " + username)));
    }


    @Bean
    public ReactiveAuthenticationManager authenticationManager(final ReactiveUserDetailsService reactiveUserDetailsService, final BCryptPasswordEncoder passwordEncoder) {
        // Use the UserDetailsService in a reactive manner
        final var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);

        // Set password encoder for authenticating user passwords
        authenticationManager.setPasswordEncoder(passwordEncoder);

        return authenticationManager;
    }


    @Bean
    public BodyFilter filterImage() {
        return BodyFilter.merge(defaultValue(),
                replacePrimitiveJsonProperty(jsonProperty -> jsonProperty.contains("image") || jsonProperty.contains("profilePic"), "<Binary image>"));
    }
}
