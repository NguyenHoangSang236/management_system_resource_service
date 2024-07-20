package com.management_system.resource.usecases.category;

import com.management_system.ingredient.entities.database.ingredient.Category;
import com.management_system.ingredient.entities.database.ingredient.SubCategory;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddNewCategoriesUseCase extends UseCase<AddNewCategoriesUseCase.InputValue, ApiResponse> {
    @Autowired
    DbUtils dbUtils;

    @Autowired
    ValueParsingUtils valueParsingUtils;

    @Autowired
    CategoryRepository categoryRepo;


    @Override
    public ApiResponse execute(InputValue input) {
        for(Category category: input.categories()) {
            String id = valueParsingUtils.parseStringToId(category.getName(), "-", false);
            category.setId(id);

            if(category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
                for(SubCategory subCategory: category.getSubCategories()) {
                    id = valueParsingUtils.parseStringToId(subCategory.getName(), "-", false);
                    subCategory.setId(id);
                }
            }

            categoryRepo.save(category);
        }

        return ApiResponse.builder()
                .result("success")
                .content("Add new categories successfully")
                .status(HttpStatus.OK)
                .build();
    }


    public record InputValue(HttpServletRequest request, List<Category> categories) implements UseCase.InputValue{}
}
