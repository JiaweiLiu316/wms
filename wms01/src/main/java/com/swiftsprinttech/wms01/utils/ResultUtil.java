package com.swiftsprinttech.wms01.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author jiawe
 */
public class ResultUtil {

    // 返回成功的 ResponseEntity
    public static <T> ResponseEntity<Result<T>> success(String message, T data) {
        Result<T> result = Result.success(message, data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 返回成功的 ResponseEntity（无数据）
    public static <T> ResponseEntity<Result<T>> success(String message) {
        Result<T> result = Result.success(message);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 返回失败的 ResponseEntity
    public static <T> ResponseEntity<Result<T>> error(String message, T data) {
        Result<T> result = Result.error(message, data);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // 返回失败的 ResponseEntity（无数据）
    public static <T> ResponseEntity<Result<T>> error(String message) {
        Result<T> result = Result.error(message);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // 返回内部服务器错误的 ResponseEntity
    public static <T> ResponseEntity<Result<T>> errorInternalServerError(String message) {
        Result<T> result = Result.error(message);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

