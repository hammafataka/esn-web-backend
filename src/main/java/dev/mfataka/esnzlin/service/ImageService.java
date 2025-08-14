package dev.mfataka.esnzlin.service;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import dev.mfataka.esnzlin.jpa.domain.Image;
import dev.mfataka.esnzlin.jpa.repository.ImageRepository;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 20.08.2024 13:43
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ImageService {
    private final ImageRepository imageRepository;

    public Mono<Image> saveImage(final MultipartFile file) {
        final var image = Image.fromMultipartFile(file);
        return imageRepository.save(image);
    }

    public Mono<Image> saveImage(final Image image) {
        return imageRepository.save(image);
    }

    @NotNull
    public Mono<Image> getImageById(final Long id) {
        return imageRepository.findById(id);
    }

    public Mono<Image> imageUsed(final Image image) {
        image.setLastUsedAt(LocalDateTime.now());
        return imageRepository.save(image);
    }
}
