package com.management_system.resource.usecases.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.database.category.Category;
import com.management_system.resource.entities.request_dto.category.CategoryRequest;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.request.RedisRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class EditCategoryUseCase extends UseCase<EditCategoryUseCase.InputValue, ApiResponse> {
    private final DbUtils dbUtils;
    private final CategoryRepository categoryRepo;
    private final RedisServiceClient redisServiceClient;
    private final ValueParsingUtils valueParsingUtils;

    public EditCategoryUseCase(DbUtils dbUtils, CategoryRepository categoryRepo, RedisServiceClient redisServiceClient, ValueParsingUtils valueParsingUtils) {
        this.dbUtils = dbUtils;
        this.categoryRepo = categoryRepo;
        this.redisServiceClient = redisServiceClient;
        this.valueParsingUtils = valueParsingUtils;
    }


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            CategoryRequest categoryReq = input.categoryRequest();
            String categoryId = categoryReq.getId();

            if (categoryId == null) {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("Field 'id' must not be null")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Optional<Category> optionalCategory = categoryRepo.findById(categoryId);

            if (optionalCategory.isPresent()) {
                Category updatedCategory = dbUtils.mergeMongoEntityFromRequest(optionalCategory.get(), categoryReq);
                categoryRepo.save(updatedCategory);

                updateCache(updatedCategory.getType());

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


    private void updateCache(TableName tableName) {
        CompletableFuture.runAsync(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<Map<String, Object>> dataList = valueParsingUtils.parseObjectListToHashMapList(
                        categoryRepo.findByType(tableName)
                );

                redisServiceClient.saveList(
                        objectMapper.writeValueAsString(
                                RedisRequest.builder()
                                        .type(tableName)
                                        .customKey(TableName.CATEGORY.name() + ".type:" + tableName)
                                        .dataList(dataList)
                                        .build()
                        )
                );
            } catch (Exception e) {
                log.error("Failed to update Redis cache: {}", e.getMessage(), e);
            }
        });
    }


    public record InputValue(CategoryRequest categoryRequest) implements UseCase.InputValue {
    }
}
