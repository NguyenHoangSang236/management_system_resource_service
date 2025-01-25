package com.management_system.resource.usecases.category;

import com.management_system.resource.entities.database.category.Category;
import com.management_system.resource.entities.database.category.SubCategory;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.ValueParsingUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AddNewCategoriesUseCase extends UseCase<AddNewCategoriesUseCase.InputValue, ApiResponse> {
    @Autowired
    ValueParsingUtils valueParsingUtils;

    @Autowired
    CategoryRepository categoryRepo;


    @Override
    public ApiResponse execute(InputValue input) {
        for (Category category : input.categories()) {
            String id = valueParsingUtils.parseStringToId("-", false, category.getName());

            category.setId(id);
            category.setCreationDate(new Date());

            if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
                for (SubCategory subCategory : category.getSubCategories()) {
                    String type = valueParsingUtils.parseStringToId("-", false, category.getType().name());
                    id = valueParsingUtils.parseStringToId("-", false, subCategory.getName());
                    subCategory.setId(type + "_" + id);
                }
            }

            categoryRepo.save(category);
        }

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Add new categories successfully")
                .status(HttpStatus.OK)
                .build();
    }


    public record InputValue(HttpServletRequest request, List<Category> categories) implements UseCase.InputValue {
    }
}
