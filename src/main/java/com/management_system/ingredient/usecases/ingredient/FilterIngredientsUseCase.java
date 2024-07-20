package com.management_system.resource.usecases.resource;

import com.management_system.ingredient.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.request_dto.IngredientFilterOptions;
import com.management_system.resource.infrastucture.feign.RedisServiceClient;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.FilterRequest;
import com.management_system.utilities.utils.DbUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterIngredientsUseCase extends UseCase<FilterIngredientsUseCase.InputValue, ApiResponse>{
    @Autowired
    DbUtils dbUtils;

    @Autowired
    RedisServiceClient redisServiceClient;

//    @Autowired
//    RedisService redisService;

    @SneakyThrows
    @Override
    public ApiResponse execute(InputValue input) {
        IngredientFilterOptions options = (IngredientFilterOptions) input.filterRequest().getFilterOption();

        List<Ingredient> resultList = dbUtils.filterData(options, input.filterRequest.getPagination(), Ingredient.class);
//        IngredientRedisData redisData = new IngredientRedisData(resultList.get(0).getId(), resultList.get(0).getName());
//        redisService.save(redisData, "resource");
//        redisService.findAll("resource");
//        redisService.deleteAll();

        return ApiResponse.builder()
                .result("success")
                .content(resultList)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(FilterRequest filterRequest) implements UseCase.InputValue {}
}
