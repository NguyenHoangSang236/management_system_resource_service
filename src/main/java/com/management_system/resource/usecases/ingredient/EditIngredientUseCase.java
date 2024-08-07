package com.management_system.resource.usecases.ingredient;

import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.request_dto.IngredientRequest;
import com.management_system.resource.infrastucture.feign.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.IngredientRepository;
import com.management_system.utilities.constant.enumuration.FilterType;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class EditIngredientUseCase extends UseCase<EditIngredientUseCase.InputValue, ApiResponse> {
    @Autowired
    IngredientRepository ingredientRepo;

    @Autowired
    DbUtils dbUtils;

    @Autowired
    RedisServiceClient redisServiceClient;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            if (input.ingredientRequest().getId() == null) {
                return ApiResponse.builder()
                        .result("failed")
                        .message("Can not find id field")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Optional<Ingredient> ingredientOptional = ingredientRepo.findById(input.ingredientRequest().getId());

            if (ingredientOptional.isPresent()) {
                ingredientRepo.save(dbUtils.mergeMongoEntityFromRequest(ingredientOptional.get(), input.ingredientRequest()));

                CompletableFuture.runAsync(() -> redisServiceClient.deleteByKey(
                                FilterType.INGREDIENT.name(), input.ingredientRequest().getId()))
                        .exceptionally(
                                ex -> {
                                    ex.printStackTrace();
                                    return null;
                                }
                        );

                return ApiResponse.builder()
                        .result("success")
                        .message("Edit resource ID " + input.ingredientRequest().getId() + " successfully")
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ApiResponse.builder()
                        .result("failed")
                        .message("Category with id " + input.ingredientRequest().getId() + " does not exist")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .content(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public record InputValue(HttpServletRequest request,
                             IngredientRequest ingredientRequest) implements UseCase.InputValue {
    }
}
