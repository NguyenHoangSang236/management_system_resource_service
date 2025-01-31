package com.management_system.resource.usecases.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.database.category.Category;
import com.management_system.resource.entities.database.category.SubCategory;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.redis.RedisClientService;
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
public class AddNewCategoriesUseCase extends UseCase<AddNewCategoriesUseCase.InputValue, ApiResponse> {
    private final ValueParsingUtils valueParsingUtils;
    private final CategoryRepository categoryRepo;
    private final RedisServiceClient redisServiceClient;

    public AddNewCategoriesUseCase(ValueParsingUtils valueParsingUtils, CategoryRepository categoryRepo, RedisServiceClient redisServiceClient) {
        this.valueParsingUtils = valueParsingUtils;
        this.categoryRepo = categoryRepo;
        this.redisServiceClient = redisServiceClient;
    }


    @Override
    public ApiResponse execute(InputValue input) {
        List<String> errors = new ArrayList<>();
        List<Category> categories = input.categories();
        Set<TableName> successTableNames = new HashSet<>();
        int successCount = 0;

        Set<String> categoryIds = categories.stream()
                .map(category -> valueParsingUtils.parseStringToId("-", false, category.getName()))
                .collect(Collectors.toSet());

        Set<String> existingIds = categoryRepo.findAllById(categoryIds)
                .stream().map(Category::getId)
                .collect(Collectors.toSet());

        for (Category category : categories) {
            String id = valueParsingUtils.parseStringToId("-", false, category.getName());

            if (existingIds.contains(id)) {
                errors.add("Category with ID " + id + " exists");
            }
            else {
                category.setId(id);
                category.setCreationDate(new Date());

                if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
                    for (SubCategory subCategory : category.getSubCategories()) {
                        String type = valueParsingUtils.parseStringToId("-", false, category.getType().name());
                        String subCategoryId = valueParsingUtils.parseStringToId("-", false, subCategory.getName());
                        subCategory.setId(type + "_" + subCategoryId);
                    }
                }

                categoryRepo.save(category);
                successCount++;
                successTableNames.add(category.getType());
            }
        }

        if (!successTableNames.isEmpty()) {
            updateCache(successTableNames);
        }

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .errors(errors)
                .message(errors.size() != input.categories().size()
                        ? "Add " + successCount + "/" + categories.size() + " new categories successfully"
                        : "Add new categories failed"
                )
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


    public record InputValue(HttpServletRequest request, List<Category> categories) implements UseCase.InputValue {
    }
}
