package com.management_system.ingredient.usecases.ingredient;

import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.entities.request_dto.IngredientFilterOptions;
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

    @SneakyThrows
    @Override
    public ApiResponse execute(InputValue input) {
        IngredientFilterOptions options = (IngredientFilterOptions) input.filterRequest().getFilterOption();

        System.out.println(input.filterRequest.getPagination().getPage());
        System.out.println(input.filterRequest.getPagination().getSize());

        List<Ingredient> resultList = dbUtils.filterData(options, input.filterRequest.getPagination(), Ingredient.class);

        return ApiResponse.builder()
                .result("success")
                .content(resultList)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(FilterRequest filterRequest) implements UseCase.InputValue {}
}
