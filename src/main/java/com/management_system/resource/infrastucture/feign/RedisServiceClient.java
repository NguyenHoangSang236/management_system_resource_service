package com.management_system.resource.infrastucture.feign;

import com.management_system.utilities.config.feign.FeignClientConfig;
import com.management_system.utilities.entities.api.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "redis-service", url = "https://localhost:8079", fallback = RedisServiceClientFallback.class, configuration = FeignClientConfig.class)
public interface RedisServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/redis/unauthen/action/findByKey-{redisHashKey}:{key}")
    ApiResponse findByKey(@PathVariable("redisHashKey") String hashKey, @PathVariable("key") String key);

    @RequestMapping(method = RequestMethod.GET, value = "/redis/unauthen/action/deleteByKey-{redisHashKey}:{key}")
    ApiResponse deleteByKey(@PathVariable("redisHashKey") String hashKey, @PathVariable("key") String key);

    @RequestMapping(method = RequestMethod.POST, value = "/redis/unauthen/action/save")
    ApiResponse save(@RequestBody String json);
}


