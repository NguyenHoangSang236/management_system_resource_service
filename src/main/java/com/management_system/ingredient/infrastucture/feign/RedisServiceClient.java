package com.management_system.resource.infrastucture.feign;

import com.management_system.utilities.entities.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "redis-service", url = "https://localhost:8079")
public interface RedisServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/redis/unauthen/action/findByKey-INGREDIENT:{key}")
    ApiResponse findByKey(@PathVariable("key") String key);

    @RequestMapping(method = RequestMethod.GET, value = "/redis/unauthen/action/deleteByKey-INGREDIENT:{key}")
    ApiResponse deleteByKey(@PathVariable("key") String key);

    @RequestMapping(method = RequestMethod.POST, value = "/redis/unauthen/action/save")
    ApiResponse save(@RequestBody String json);
}
