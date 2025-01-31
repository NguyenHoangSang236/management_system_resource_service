package com.management_system.resource.infrastucture.feign.redis;

import com.management_system.utilities.config.feign.FeignClientConfig;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.entities.api.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "redis-service", url = "${management-system-server.redis}", fallback = RedisServiceClientFallback.class, configuration = FeignClientConfig.class)
public interface RedisServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/redis/unauthen/action/find")
    ApiResponse find(@RequestParam("table") TableName tableName, @RequestParam("id") String id);

    @RequestMapping(method = RequestMethod.GET, value = "/api/redis/unauthen/action/findAllByTable")
    ApiResponse findAllByTable(@RequestParam("table") TableName tableName);

    @RequestMapping(method = RequestMethod.GET, value = "/api/redis/unauthen/action/delete")
    ApiResponse delete(@RequestParam("table") TableName tableName, @RequestParam("id") String id, @RequestParam("isSafe") Boolean isSafe);

    @RequestMapping(method = RequestMethod.POST, value = "/api/redis/unauthen/action/save")
    ApiResponse save(@RequestBody String json);

    @RequestMapping(method = RequestMethod.POST, value = "/api/redis/unauthen/action/saveList")
    ApiResponse saveList(@RequestBody String json);
}