package dev.mfataka.esnzlin.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.mfataka.esnzlin.jpa.enums.Faculty;
import dev.mfataka.esnzlin.jpa.enums.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String gender;
    private String nationality;
    private Faculty faculty;
    private UserRole role;
    private LocalDateTime dateOfBirth;
    private String profilePic;
}