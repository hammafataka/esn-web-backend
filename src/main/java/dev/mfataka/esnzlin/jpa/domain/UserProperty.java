package dev.mfataka.esnzlin.jpa.domain;


import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.mfataka.esnzlin.jpa.enums.UserProperties;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 07.10.2024 3:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_property")
public class UserProperty {
    @Id
    private String id;
    @Column(value = "user_id")
    private long userId;
    @Column(value = "key_name")
    private UserProperties key;

    @Column(value = "value")
    private String value;


    public static UserProperty newOne(final long userId, final UserProperties key, final String value) {
        return UserProperty.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .key(key)
                .value(value)
                .build();
    }
}
