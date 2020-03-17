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

    public ApiResult add(String key, Object value) {
        Assert.hasText(key, "Parameter `key` must not be blank");
        Assert.notNull(value, "Parameter `value` must not be null");

        this.put(key, value);
        return this;
    }
}
