package dev.mfataka.esnzlin.jpa.keys;

import java.io.Serializable;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.mfataka.esnzlin.jpa.enums.UserProperties;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 07.10.2024 3:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPropertyKey implements Serializable {
    @Column(value = "user_id")
    private long userId;
    @Column(value = "key_name")
    private UserProperties key;
}
