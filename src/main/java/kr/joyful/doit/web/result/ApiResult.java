package kr.joyful.doit.web.result;

import org.springframework.util.Assert;

import java.util.HashMap;

public class ApiResult extends HashMap<String, Object> {


    public static ApiResult blank() {
        return new ApiResult();
    }

    public static ApiResult message(String message) {
        Assert.hasText(message, "Parameter `message` must not be blank");

        ApiResult apiResult = new ApiResult();
        apiResult.put("message", message);
        return apiResult;
    }
}
