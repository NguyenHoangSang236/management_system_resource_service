package com.management_system.resource.usecases.ingredient;

import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.request_dto.filter_requests.options.IngredientFilterOptions;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterIngredientsUseCase extends UseCase<FilterIngredientsUseCase.InputValue, ApiResponse> {
    @Autowired
    DbUtils dbUtils;


    @SneakyThrows
    @Override
    public ApiResponse execute(InputValue input) {
        List<Ingredient> resultList = dbUtils.filterData(input.filterRequest(), Ingredient.class);

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .content(resultList)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(FilterRequest<IngredientFilterOptions> filterRequest) implements UseCase.InputValue {
    }
}
