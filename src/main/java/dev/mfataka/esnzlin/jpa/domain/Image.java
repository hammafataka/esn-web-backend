package dev.mfataka.esnzlin.jpa.domain;

import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.multipart.MultipartFile;

import io.vavr.control.Try;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.mfataka.esnzlin.exceptions.UserDisplayableException;
import dev.mfataka.esnzlin.service.ImageService;
import dev.mfataka.esnzlin.utils.ImageUtils;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 20.08.2024 13:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    private Long id;
    private String name;
    private String type;
    private byte[] data;

    @Column(value = "last_used_at")
    private LocalDateTime lastUsedAt;


    public String asBase64(final ImageService imageService) {
        final var bytes = ImageUtils.decompressImage(this.getData());
        return Try.of(() -> {
                    final var encoded = Base64.getEncoder().encodeToString(bytes);
                    return String.format("data:%s;base64,%s", this.getType(), encoded);
                })
                .andThen(() -> imageService.imageUsed(this).subscribe())
                .getOrElseThrow(() -> new UserDisplayableException("Failed to convert image to Base64"));
    }

    public static Image fromMultipartFile(final MultipartFile file) {
        return Try.of(() -> Image.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .data(ImageUtils.compressImage(file.getBytes()))
                        .lastUsedAt(LocalDateTime.now())
                        .build()
                )
                .getOrElseThrow(() -> new UserDisplayableException("Failed to create image from Base64"));
    }


    public static Image fromBase64(final String imageBase64) {
        return Try.of(() -> {
                            // Decode the Base64 image string
                            final var imageBytes = Base64.getDecoder().decode(imageBase64.split(",")[1]);
                            final var mimeType = ImageUtils.getMimeType(imageBase64);
                            return Image.builder()
                                    .name("")
                                    .type(mimeType)
                                    .data(ImageUtils.compressImage(imageBytes))
                                    .lastUsedAt(LocalDateTime.now())
                                    .build();
                        }
                )
                .getOrElseThrow(() -> new UserDisplayableException("Failed to create image from Base64"));
    }

}
