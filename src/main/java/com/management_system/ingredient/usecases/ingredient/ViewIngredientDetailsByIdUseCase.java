package com.management_system.ingredient.usecases.ingredient;

import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.infrastucture.feign.RedisServiceClient;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.FilterRequest;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ViewIngredientDetailsByIdUseCase extends UseCase<ViewIngredientDetailsByIdUseCase.InputValue, ApiResponse>{
    @Autowired
    DbUtils dbUtils;

    @Autowired
    RedisServiceClient redisServiceClient;


    @Override
    public ApiResponse execute(InputValue input) {
        String ingredientId = input.id();

        ApiResponse redisRes = redisServiceClient.findByKey("INGREDIENT:" + input.id());
        Ingredient ingredient = dbUtils.getDataById(input.id(), Ingredient.class);

        return ApiResponse.builder()
                .result("success")
                .content(ingredient)
                .status(HttpStatus.OK)
                .build();
    }


    public record InputValue(String id) implements UseCase.InputValue {}
}
