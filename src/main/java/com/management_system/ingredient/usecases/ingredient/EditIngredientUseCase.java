package com.management_system.ingredient.usecases.ingredient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.entities.request_dto.IngredientRequest;
import com.management_system.ingredient.infrastucture.feign.RedisServiceClient;
import com.management_system.ingredient.infrastucture.repository.IngredientRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.TokenInfo;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.JwtUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class EditIngredientUseCase extends UseCase<EditIngredientUseCase.InputValue, ApiResponse>{
    @Autowired
    IngredientRepository ingredientRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    DbUtils dbUtils;

    @Autowired
    ValueParsingUtils valueParsingUtils;

    @Autowired
    RedisServiceClient redisServiceClient;


    @Override
    public ApiResponse execute(InputValue input) {
        String id = input.id();
        IngredientRequest ingredientReq = input.ingredientRequest();
        TokenInfo tokenInfo = jwtUtils.getTokenInfoFromHttpRequest(input.request());

        ingredientReq.setLastUpdateUsername(tokenInfo.getUserName());
        ingredientReq.setLastUpdateTime(new Date());

        Map<String, Object> map = valueParsingUtils.parseMongoDbMap(ingredientReq);

        dbUtils.updateSpecificFields("_id", id, map, Ingredient.class);

        CompletableFuture.runAsync(() -> redisServiceClient.deleteByKey(id)).exceptionally(
                ex -> {
                ex.printStackTrace();
                return null;
            }
        );

        return ApiResponse.builder()
                .result("success")
                .content("Edit ingredient ID " + id + " successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(HttpServletRequest request, String id, IngredientRequest ingredientRequest) implements UseCase.InputValue {}
}
