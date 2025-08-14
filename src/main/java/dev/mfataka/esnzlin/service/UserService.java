package dev.mfataka.esnzlin.service;

import static dev.mfataka.esnzlin.constants.HtmlConstants.getEmailVerificationHtml;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.config.security.JwtService;
import dev.mfataka.esnzlin.exceptions.UserDisplayableException;
import dev.mfataka.esnzlin.jpa.domain.Image;
import dev.mfataka.esnzlin.jpa.domain.User;
import dev.mfataka.esnzlin.jpa.enums.Interest;
import dev.mfataka.esnzlin.jpa.repository.UserRepository;
import dev.mfataka.esnzlin.models.SignInRequest;
import dev.mfataka.esnzlin.models.UserDetailsResponse;
import dev.mfataka.esnzlin.models.UserRequest;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 19:57
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final ServerInfoService serverInfoService;
    private final ReactiveAuthenticationManager authenticationManager;
    private final ImageService imageService;

    @Value("${endpoint.path.verification}")
    private final String verificationPath;
    @Value("${endpoint.path.verify}")
    private final String verificationEndpoint;

    public Mono<String> signUpUser(final UserRequest request) {
        log.info("Signing up user with email [{}]", request.getEmail());
        return findByEmail(request.getEmail())
                .<String>flatMap(existingUser -> Mono.error(new UserDisplayableException("Email is already used by another account")))
                .switchIfEmpty(processNonExistingUser(request))
                .onErrorResume(e -> {
                    log.error("Error signing up user", e);
                    return Mono.error(new UserDisplayableException(e));
                });
    }

    @NotNull
    private Mono<String> processNonExistingUser(final UserRequest request) {
        return request.asUser(imageService)
                .flatMap(this::saveUser)
                .flatMap(savedUser -> {
                    final var authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), savedUser.getUsername());
                    ReactiveSecurityContextHolder.withAuthentication(authentication);
                    return sendVerificationEmail(request.getEmail()).map(ignored -> "Authentication successful");

                });
    }


    public Mono<UserDetailsResponse> signIn(final SignInRequest loginRequest, final String refreshToken) {
        if (loginRequest.isFromReload() && Objects.nonNull(refreshToken)) {
            log.info("User is reloading, using refresh token to sign in");
            return findByToken(refreshToken)
                    .flatMap(user -> {
                        final var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                        ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
                        log.info("User [{}] reloaded successfully", user.getEmail());
                        return user.asUserDetailsResponse(imageService);
                    });

        }
        return authenticate(loginRequest)
                .flatMap(user -> user.asUserDetailsResponse(imageService))
                .onErrorResume(e -> {
                    log.error("Error signing in user", e);
                    return Mono.error(new UserDisplayableException(e));
                });

    }


    public Mono<Image> retrieveUserImageByEmail(final String username) {
        return findByEmail(username)
                .flatMap(user -> imageService.getImageById(user.getProfilePictureId()))
                .switchIfEmpty(Mono.error(new UserDisplayableException("User not found")));
    }

    public Mono<User> findByEmailOrThrow(String email) {
        return findByEmail(email)
                .switchIfEmpty(Mono.error(new UserDisplayableException("User not found with email: " + email)));
    }

    public Mono<User> findByToken(final String token) {
        return JwtService.extractEmail(token)
                .flatMap(this::findByEmail);
    }

    public Mono<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }


    public Mono<String> sendVerificationEmail(final String email) {
        return findByEmail(email)
                .map(user -> {
                    final var token = JwtService.generateVerificationToken(user.getEmail());
                    final var url = getFullVerificationUrl(token);
                    final var emailVerificationHtml = getEmailVerificationHtml(url);
                    mailService.sendMail(emailVerificationHtml, user.getEmail(), "Email Verification");
                    return "Verification email sent";
                })
                .switchIfEmpty(Mono.error(new UserDisplayableException("User not found")));
    }

    public Mono<String> verificationEmailResult(final String email) {
        return findByEmail(email)
                .<String>handle((user, sink) -> {
                    if (user.isVerified()) {
                        sink.next("Email verified");
                    } else {
                        sink.error(new UserDisplayableException("Email not verified"));
                    }
                })
                .switchIfEmpty(Mono.error(new UserDisplayableException("User not found")));
    }

    public Mono<User> saveUser(final User user) {
        return userRepository.save(user);
    }

    private String getFullVerificationUrl(final String token) {
        final var fullServerAddress = serverInfoService.getFullServerAddress();
        return fullServerAddress + verificationPath + verificationEndpoint + "/" + token + "/token";
    }

    private Mono<User> authenticate(final SignInRequest request) {
        final var email = request.getEmail();
        final var password = request.getPassword();

        log.info("authenticating email [{}] with pass [{}]", email, password);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))
                .flatMap(ignored -> findByEmail(email)
                        .<User>handle((user, sink) -> {
                            log.info("Signing in in user [{}]", user.getEmail());
                            if (!passwordEncoder.matches(password, user.getPassword())) {
                                log.warn("password mismatched to sign in for user {} with pass {}, details [{}]", email, password, user);
                                sink.error(new BadCredentialsException("Invalid password"));
                                return;
                            }
                            final var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                            ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
                            sink.next(user);
                        })
                        .switchIfEmpty(Mono.error(new UserDisplayableException("User not found with email: " + email)))
                );


    }

    public Mono<String> updateAbout(final UserRequest.AboutRequest request, final String refreshToken) {
        return findByToken(refreshToken)
                .flatMap(user -> saveAbout(request, user))
                .switchIfEmpty(Mono.error(new UserDisplayableException("User not found")));
    }

    @NotNull
    private Mono<String> saveAbout(UserRequest.AboutRequest request, User user) {
        final var interests = request.getInterests()
                .stream()
                .map(interest -> Interest.valueOf(interest.replace(" ", "_").toUpperCase()))
                .collect(Collectors.toSet());

        user.setDescription(request.getDescription());
        user.setInterests(interests);
        return saveUser(user)
                .map(ignored -> "User about updated");
    }
}
