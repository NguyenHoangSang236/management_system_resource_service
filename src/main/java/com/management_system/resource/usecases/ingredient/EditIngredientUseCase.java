package com.management_system.resource.usecases.ingredient;

import com.management_system.resource.entities.database.category.Category;
import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.request_dto.ingredient.AddIngredientRequest;
import com.management_system.resource.entities.request_dto.ingredient.EditIngredientRequest;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.resource.infrastucture.repository.IngredientRepository;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.FirebaseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class EditIngredientUseCase extends UseCase<EditIngredientUseCase.InputValue, ApiResponse> {
    private final IngredientRepository ingredientRepo;
    private final CategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;
    private final DbUtils dbUtils;
    private final RedisServiceClient redisServiceClient;
    private final FirebaseUtils firebaseUtils;

    public EditIngredientUseCase(IngredientRepository ingredientRepo, CategoryRepository categoryRepo, SupplierRepository supplierRepo, DbUtils dbUtils, RedisServiceClient redisServiceClient, FirebaseUtils firebaseUtils) {
        this.ingredientRepo = ingredientRepo;
        this.categoryRepo = categoryRepo;
        this.supplierRepo = supplierRepo;
        this.dbUtils = dbUtils;
        this.redisServiceClient = redisServiceClient;
        this.firebaseUtils = firebaseUtils;
    }


    @SneakyThrows
    @Override
    public ApiResponse execute(InputValue input) {
        List<String> errors = new ArrayList<>();
        String id = input.ingredientRequest().getId();
        MultipartFile image = input.image();
        
        if (id.isBlank()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Field 'id' must not be null")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Optional<Ingredient> ingredientOptional = ingredientRepo.findById(id);

        if (ingredientOptional.isPresent()) {
            Ingredient ingredient = ingredientOptional.get();
            EditIngredientRequest ingredientRequest = input.ingredientRequest();
            List<String> existingSubCategoryIds = new ArrayList<>();
            String supplierId = ingredientRequest.getSupplierId();
            List<String> subCategoryIds = ingredientRequest.getSubCategoryIds();

            if(supplierId != null && !supplierId.isBlank() && supplierRepo.findById(supplierId).isEmpty()) {
                errors.add("Supplier with ID " + supplierId +" does not exist");

                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(errors)
                        .message("Edit ingredient " + id + " failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            CompletableFuture<String> cfImgUrl = null;

            if(image != null) {
                cfImgUrl = CompletableFuture.supplyAsync(
                        () -> firebaseUtils.upload(image, id)
                ).exceptionally(ex -> {
                    log.error("Error: {}", ex.getMessage());

                    return null;
                });
            }

            if(subCategoryIds != null && !subCategoryIds.isEmpty()) {
                for(String subCategoryId: ingredientRequest.getSubCategoryIds()) {
                    List<Category> existingCategories = categoryRepo.findBySubCategoryId(subCategoryId);

                    if(existingCategories.isEmpty()) {
                        errors.add("Category with ID " + subCategoryId + " does not exist, so it has been removed");
                    }
                    else {
                        existingSubCategoryIds.add(subCategoryId);
                    }
                }

                if (existingSubCategoryIds.size() != ingredientRequest.getSubCategoryIds().size()) {
                    ingredientRequest.setSubCategoryIds(existingSubCategoryIds);
                }
            }

            String imgUrl = null;
            ingredient = dbUtils.mergeMongoEntityFromRequest(ingredient, ingredientRequest);

            if(cfImgUrl != null) {
                imgUrl = cfImgUrl.get();

                if(imgUrl != null && !imgUrl.isBlank()) {
                    ingredient.setImage(imgUrl);
                }
                else {
                    errors.add("Can not change image");
                }
            }

            try {
                ingredientRepo.save(ingredient);
            }
            catch (Exception e) {
                if(imgUrl != null) {
                    CompletableFuture.runAsync(
                            () -> firebaseUtils.delete(id)
                    ).exceptionally(ex -> {
                        log.error("Error: {}", ex.getMessage());

                        return null;
                    });
                }

                throw e;
            }

            CompletableFuture.runAsync(() -> redisServiceClient.delete(
                            TableName.INGREDIENT,
                            id,
                            true
                    ))
                    .exceptionally(
                            ex -> {
                                ex.printStackTrace();
                                return null;
                            }
                    );

            return ApiResponse.builder()
                    .result(ResponseResult.success.name())
                    .message("Edit ingredient ID " + id + " successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Ingredient with ID " + id + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public record InputValue(HttpServletRequest request, EditIngredientRequest ingredientRequest, MultipartFile image) implements UseCase.InputValue {
    }
}
