package dev.mfataka.esnzlin.utils;

import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.mfataka.esnzlin.exceptions.UserDisplayableException;
import dev.mfataka.esnzlin.jpa.enums.ResponseResult;


@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponse<T> {

    @JsonIgnore
    private ResponseResult status;

    private String message;

    private T data;

    private boolean isUserDisplayable;

    public static <T> BaseResponse<T> ok(T data) {
        return ok(data, "Operation Succeeded");
    }

    public static <T> BaseResponse<T> ok(T data, final String message) {
        return new BaseResponse<>(ResponseResult.OK, message, data, true);
    }


    public static <T> BaseResponse<T> nonUserDisplayableError(final String ex) {
        return error(ex, false);
    }

    public static <T> BaseResponse<T> userDisplayableError(final Throwable ex) {
        return userDisplayableError(ex.getMessage());
    }

    public static <T> BaseResponse<T> userDisplayableError(final String ex) {
        return error(ex, true);
    }


    public static <T> BaseResponse<T> error(Throwable ex) {
        if ((ex instanceof UserDisplayableException) || (ex instanceof MethodArgumentNotValidException)) {
            return userDisplayableError(ex.getMessage());
        }
        return error(ex.getMessage(), false);
    }

    // private method to create error response based on userDisplayable flag
    private static <T> BaseResponse<T> error(final String ex, boolean isUserDisplayable) {
        return new BaseResponse<>(ResponseResult.ERROR, ex, null, isUserDisplayable);
    }


    public boolean isOk() {
        return status == ResponseResult.OK;
    }

    public boolean isError() {
        return status == ResponseResult.ERROR;
    }

}
