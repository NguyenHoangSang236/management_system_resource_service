package com.management_system.ingredient.usecases.ingredient;

import com.management_system.ingredient.entities.request_dto.IngredientFilterOptions;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.FilterRequest;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FilterIngredientsUseCase extends UseCase<FilterIngredientsUseCase.InputValue, ApiResponse>{
    @SneakyThrows
    @Override
    public ApiResponse execute(InputValue input) {
        IngredientFilterOptions options = (IngredientFilterOptions) input.filterRequest().getFilterOption();

        System.out.println(options.getName());

        return ApiResponse.builder()
                .result("success")
                .content("aaaaaaa")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(FilterRequest filterRequest) implements UseCase.InputValue {}
}
