package com.management_system.resource.usecases.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.database.category.Category;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.request.RedisRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.ValueParsingUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DeleteCategoriesUseCase extends UseCase<DeleteCategoriesUseCase.InputValue, ApiResponse> {
    private final CategoryRepository categoryRepo;
    private final ValueParsingUtils valueParsingUtils;
    private final RedisServiceClient redisServiceClient;

    public DeleteCategoriesUseCase(CategoryRepository categoryRepo, ValueParsingUtils valueParsingUtils, RedisServiceClient redisServiceClient) {
        this.categoryRepo = categoryRepo;
        this.valueParsingUtils = valueParsingUtils;
        this.redisServiceClient = redisServiceClient;
    }


    @Override
    public ApiResponse execute(InputValue input) {
        Set<String> categoryIdList = input.idList();
        List<String> success = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<TableName> successTypes = new HashSet<>();
        List<Category> existingCategories = categoryRepo.findAllById(categoryIdList);
        int successCount = 0;
        int totalIds = categoryIdList.size();

        if (existingCategories.isEmpty()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Can not delete because these IDs do not exist in database")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        for (Category category : existingCategories) {
            String id = category.getId();
            TableName type = category.getType();

            categoryRepo.deleteById(id);
            success.add("Delete category with ID " + category.getId() + " successfully");
            successTypes.add(type);
            successCount++;
            categoryIdList.remove(id);
        }

        updateCache(successTypes);

        errors = categoryIdList.stream()
                .map(id -> "Category with ID " + id + " does not exist")
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .content(success)
                .errors(errors)
                .message("Delete " + successCount + "/" + totalIds + " categories successfully")
                .status(HttpStatus.OK)
                .build();
    }

    private void updateCache(Set<TableName> successTableNames) {
        CompletableFuture.runAsync(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                for (TableName tableName : successTableNames) {
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
                }
            } catch (Exception e) {
                log.error("Failed to update Redis cache: {}", e.getMessage(), e);
            }
        });
    }

    public record InputValue(HttpServletRequest request, Set<String> idList) implements UseCase.InputValue {
    }
}
