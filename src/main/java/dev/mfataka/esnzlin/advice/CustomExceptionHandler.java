package dev.mfataka.esnzlin.advice;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.utils.BaseResponse;

//RestControllerAdvice annotation is a combination of ControllerAdvice and Responsebody.
//Used in Java to create global exception handlers for RESTful APIs.
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidArgument(final Throwable ex) {
        log.error("Error ",ex);
        if (ex instanceof MethodArgumentNotValidException notValidException) {
            final var defaultMessage = notValidException.getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining());
            final var error = BaseResponse.userDisplayableError(defaultMessage);
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.badRequest().body(BaseResponse.error(ex));
    }

}
