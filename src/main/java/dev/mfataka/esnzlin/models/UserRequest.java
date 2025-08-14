package dev.mfataka.esnzlin.models;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import reactor.core.publisher.Mono;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import dev.mfataka.esnzlin.jpa.domain.Image;
import dev.mfataka.esnzlin.jpa.domain.User;
import dev.mfataka.esnzlin.jpa.enums.Faculty;
import dev.mfataka.esnzlin.jpa.enums.UserRole;
import dev.mfataka.esnzlin.service.ImageService;


/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 19:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstname;
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phone;

    @NotBlank(message = "Faculty is required")
    private String faculty;

    @NotBlank(message = "Role is required")
    private String role;
    @NotBlank(message = "gender is required")
    private String gender;
    @NotBlank(message = "nationality is required")
    private String nationality;
    @NotNull(message = "Date of birth is required")
    private LocalDateTime dateOfBirth;

    @ToString.Exclude
    @NotNull(message = "Profile image is required")
    private String profilePic;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AboutRequest {
        @Size(max = 500, message = "Description must not exceed 500 characters")
        private String description;
        private Set<String> interests;
    }

    private void encodePassword() {
        password = passwordEncoder.encode(password);
    }


    public Mono<User> asUser(final ImageService imageService) {
        encodePassword();
        final var image = Image.fromBase64(profilePic);
        final var builder = User.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .password(password)
                .phone(phone)
                .faculty(Faculty.fromString(faculty))
                .role(UserRole.fromString(role))
                .gender(gender)
                .nationality(nationality)
                .dateOfBirth(dateOfBirth)
                .lastLogin(LocalDateTime.now());
        return imageService.saveImage(image)
                .map(savedImage -> builder.profilePictureId(savedImage.getId()).build());
    }
}

