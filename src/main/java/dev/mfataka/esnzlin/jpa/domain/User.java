package dev.mfataka.esnzlin.jpa.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import reactor.core.publisher.Mono;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.mfataka.esnzlin.jpa.enums.Faculty;
import dev.mfataka.esnzlin.jpa.enums.Interest;
import dev.mfataka.esnzlin.jpa.enums.UserRole;
import dev.mfataka.esnzlin.models.UserDetailsResponse;
import dev.mfataka.esnzlin.service.ImageService;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 19:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    @Id
    private long id;
    private String email;
    private String firstname;
    private String lastname;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String phone;
    private Faculty faculty;
    private UserRole role;
    private String gender;
    private String nationality;
    private LocalDateTime dateOfBirth;
    private LocalDateTime lastLogin;
    private boolean verified;
    private String description;
    @Column(value = "interest")
    private String interests;
    @Column(value = "profile_picture_id")
    private long profilePictureId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public Set<Interest> getInterests() {
        if (this.interests == null || this.interests.isEmpty()) {
            return Set.of();
        }
        return Arrays.stream(this.interests.split(","))
                .map(ordinal -> Interest.values()[Integer.parseInt(ordinal)])
                .collect(Collectors.toSet());
    }

    public void setInterests(final Set<Interest> interests) {
        this.interests = interests.stream()
                .map(interest -> String.valueOf(interest.ordinal()))
                .collect(Collectors.joining(","));
    }

    public Mono<UserDetailsResponse> asUserDetailsResponse(final ImageService imageService) {
        final var builder = UserDetailsResponse.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .phone(phone)
                .gender(gender)
                .nationality(nationality)
                .faculty(faculty)
                .role(role)
                .dateOfBirth(dateOfBirth);
        return imageService.getImageById(profilePictureId)
                .map(image -> builder.profilePic(image.asBase64(imageService))
                        .build())
                .defaultIfEmpty(builder.build());
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }
}
