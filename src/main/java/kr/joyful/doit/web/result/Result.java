package kr.joyful.doit.web.result;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Result {
    private Result(){}


    public static ResponseEntity<ApiResult> created() {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public static ResponseEntity<ApiResult> ok() {
        return ResponseEntity.ok().build();
    }

    public static ResponseEntity<ApiResult> failure(String message) {
        return ResponseEntity.badRequest().body(ApiResult.message(message));
    }
}
