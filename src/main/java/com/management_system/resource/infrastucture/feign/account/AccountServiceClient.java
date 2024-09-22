package com.management_system.resource.infrastucture.feign.account;

import com.management_system.utilities.config.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "authentication-service", url = "https://localhost:8082", fallback = AccountServiceClientFallback.class, configuration = FeignClientConfig.class)
public interface AccountServiceClient {

}
