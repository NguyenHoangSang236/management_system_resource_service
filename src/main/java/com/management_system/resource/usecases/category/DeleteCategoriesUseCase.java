package com.management_system.resource.usecases.category;

import com.management_system.resource.entities.database.ingredient.Category;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DeleteCategoriesUseCase extends UseCase<DeleteCategoriesUseCase.InputValue, ApiResponse> {
    @Autowired
    CategoryRepository categoryRepo;


    @Override
    public ApiResponse execute(InputValue input) {
        List<String> categoryList = input.idList();
        StringBuilder resBuilder = new StringBuilder();

        for (String cateId : categoryList) {
            if (!cateId.isBlank()) {
                Optional<Category> categoryOptional = categoryRepo.findById(cateId);

                if (categoryOptional.isPresent()) {
                    categoryRepo.deleteById(cateId);
                    resBuilder.append(cateId + ", ");
                }
            }
        }

        if (resBuilder.toString().isBlank()) {
            return ApiResponse.builder()
                    .result("failed")
                    .content("")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result("success")
                    .content("Delete category ID " + resBuilder.substring(0, resBuilder.lastIndexOf(",")) + " successfully")
                    .status(HttpStatus.OK)
                    .build();
        }
    }

    public record InputValue(HttpServletRequest request, List<String> idList) implements UseCase.InputValue {
    }
}
