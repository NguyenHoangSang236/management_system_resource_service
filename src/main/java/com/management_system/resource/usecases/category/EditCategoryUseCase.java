package com.management_system.resource.usecases.category;

import com.management_system.resource.entities.database.ingredient.Category;
import com.management_system.resource.infrastucture.feign.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.constant.enumuration.FilterType;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class EditCategoryUseCase extends UseCase<EditCategoryUseCase.InputValue, ApiResponse> {
    @Autowired
    ValueParsingUtils valueParsingUtils;

    @Autowired
    DbUtils dbUtils;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    RedisServiceClient redisServiceClient;


    @Override
    public ApiResponse execute(InputValue input) {
        if (input.category().getId() == null) {
            return ApiResponse.builder()
                    .result("failed")
                    .content("Can not find id field")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Optional<Category> optionalCategory = categoryRepo.findById(input.category().getId());

        if (optionalCategory.isPresent()) {
            categoryRepo.save(dbUtils.mergeMongoEntityFromRequest(optionalCategory.get(), input.category()));

            CompletableFuture.runAsync(() -> redisServiceClient.deleteByKey(
                            FilterType.CATEGORY.name(),
                            input.category().getId()))
                    .exceptionally(
                            ex -> {
                                ex.printStackTrace();
                                return null;
                            }
                    );

            return ApiResponse.builder()
                    .result("success")
                    .content("Edit category successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result("failed")
                    .content("Category with id " + input.category().getId() + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public record InputValue(HttpServletRequest request, Category category) implements UseCase.InputValue {
    }
}
