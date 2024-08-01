package com.management_system.resource.infrastucture.feign;

import com.management_system.utilities.entities.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RedisServiceClientFallback implements RedisServiceClient {
    @Override
    public ApiResponse findByKey(String hashKey, String key) {
        return ApiResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .content("Not found error")
                .result("failed")
                .build();
    }

    @Override
    public ApiResponse deleteByKey(String hashKey, String key) {
        return ApiResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .content("Not found error")
                .result("failed")
                .build();
    }

    @Override
    public ApiResponse save(String json) {
        return ApiResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .content("Not found error")
                .result("failed")
                .build();
    }
}
