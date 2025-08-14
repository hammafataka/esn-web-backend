package dev.mfataka.esnzlin.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.exceptions.UserDisplayableException;
import dev.mfataka.esnzlin.service.ImageService;
import dev.mfataka.esnzlin.service.UserService;
import dev.mfataka.esnzlin.utils.BaseResponse;
import dev.mfataka.esnzlin.utils.ImageUtils;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 20.08.2024 15:24
 */

@Slf4j
@RestController
@RequestMapping(path = "${endpoint.path.external}/image")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Image Resource")
public class ImageResource {
    private final ImageService imageService;
    private final UserService userService;

    @GetMapping(path = "/user/{email}")
    public Mono<ResponseEntity<byte[]>> retrieveImageUserName(@PathVariable(name = "email") final String email) {
        return userService.retrieveUserImageByEmail(email)
                .map(image -> {
                    final var bytes = ImageUtils.decompressImage(image.getData());
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(image.getType()))
                            .contentLength(bytes.length)
                            .body(bytes);
                });

    }

    @GetMapping(path = "/byid/{id}")
    public Mono<ResponseEntity<byte[]>> retrieveImageById(@PathVariable(name = "id") final Long id) {
        return imageService.getImageById(id)
                .map(image -> {
                    final var imageData = image.getData();
                    final var bytes = ImageUtils.decompressImage(imageData);
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(image.getType()))
                            .contentLength(bytes.length)
                            .body(bytes);
                })
                .switchIfEmpty(Mono.error(new UserDisplayableException("Image not found")));
    }

    @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<BaseResponse<String>> uploadImage(@RequestPart("image") final MultipartFile image) {
        return imageService.saveImage(image)
                .map(savedImage -> BaseResponse.ok("Image uploaded successfully"));
    }
}
