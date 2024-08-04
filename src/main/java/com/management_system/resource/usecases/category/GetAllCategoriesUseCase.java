package com.management_system.resource.usecases.category;

import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GetAllCategoriesUseCase extends UseCase<GetAllCategoriesUseCase.InputValue, ApiResponse> {
    @Autowired
    CategoryRepository categoryRepo;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            return ApiResponse.builder()
                    .result("success")
                    .content(categoryRepo.findAll())
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .content(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public record InputValue() implements UseCase.InputValue {
    }
}
