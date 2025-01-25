package com.management_system.resource.usecases.category;

import com.management_system.resource.entities.database.category.Category;
import com.management_system.resource.entities.request_dto.filter_requests.CategoryFilterRequest;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilterCategoriesUseCase extends UseCase<FilterCategoriesUseCase.InputValue, ApiResponse> {
    final DbUtils dbUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        CategoryFilterRequest request = new CategoryFilterRequest(input.name(), input.type());

        List<Category> categories = dbUtils.filterData(request, Category.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set("result_size", String.valueOf(categories.size()));

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .content(categories)
                .status(HttpStatus.OK)
                .headers(headers)
                .build();
    }

    public record InputValue(String name, TableName type) implements UseCase.InputValue {
    }
}