package com.management_system.resource.usecases.category;

import com.management_system.resource.entities.database.category.Category;
import com.management_system.resource.entities.request_dto.CategoryRequest;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class EditCategoryUseCase extends UseCase<EditCategoryUseCase.InputValue, ApiResponse> {
    @Autowired
    DbUtils dbUtils;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    RedisServiceClient redisServiceClient;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            CategoryRequest categoryReq = input.categoryRequest();
            String categoryId = categoryReq.getId();

            if (categoryId == null) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Can not find ID field")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Optional<Category> optionalCategory = categoryRepo.findById(categoryId);

            if (optionalCategory.isPresent()) {
                categoryRepo.save(dbUtils.mergeMongoEntityFromRequest(optionalCategory.get(), categoryReq));

                CompletableFuture.runAsync(() -> redisServiceClient.delete(TableName.CATEGORY, categoryId))
                        .exceptionally(
                                ex -> {
                                    ex.printStackTrace();
                                    return null;
                                }
                        );

                return ApiResponse.builder()
                        .result(ResponseResult.success.name())
                        .message("Edit category successfully")
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Category with id " + categoryId + " does not exist")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public record InputValue(CategoryRequest categoryRequest) implements UseCase.InputValue {
    }
}
