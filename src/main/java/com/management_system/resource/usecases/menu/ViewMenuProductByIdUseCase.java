package com.management_system.resource.usecases.menu;

import com.management_system.resource.common.caching.CachingProcessHandler;
import com.management_system.resource.common.caching.MongoRedisClientServiceImpl;
import com.management_system.resource.entities.database.menu.Menu;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewMenuProductByIdUseCase extends UseCase<ViewMenuProductByIdUseCase.InputValue, ApiResponse>{
    final MongoRedisClientServiceImpl mongoRedisClientService;

    @Override
    public ApiResponse execute(InputValue input) {
        String menuId = input.menuId;

        Menu menuProduct = (Menu) mongoRedisClientService.getAndCacheDataFromOneTable(
                TableName.MENU,
                menuId,
                null
        );

        return ApiResponse.builder()
                .result("success")
                .content(menuProduct)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(String menuId) implements UseCase.InputValue {
    }
}
