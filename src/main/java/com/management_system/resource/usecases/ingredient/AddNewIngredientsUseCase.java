package com.management_system.resource.usecases.ingredient;

import com.management_system.resource.entities.database.category.Category;
import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.ingredient.AddIngredientRequest;
import com.management_system.resource.infrastucture.constant.IngredientStatusEnum;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.resource.infrastucture.repository.IngredientRepository;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.FirebaseUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class AddNewIngredientsUseCase extends UseCase<AddNewIngredientsUseCase.InputValue, ApiResponse> {
    private final IngredientRepository ingredientRepo;
    private final SupplierRepository supplierRepo;
    private final CategoryRepository categoryRepo;
    private final ValueParsingUtils valueParsingUtils;
    private final DbUtils dbUtils;
    private final FirebaseUtils firebaseUtils;

    public AddNewIngredientsUseCase(IngredientRepository ingredientRepo, SupplierRepository supplierRepo, CategoryRepository categoryRepo, ValueParsingUtils valueParsingUtils, DbUtils dbUtils, FirebaseUtils firebaseUtils) {
        this.ingredientRepo = ingredientRepo;
        this.supplierRepo = supplierRepo;
        this.categoryRepo = categoryRepo;
        this.valueParsingUtils = valueParsingUtils;
        this.dbUtils = dbUtils;
        this.firebaseUtils = firebaseUtils;
    }


    @SneakyThrows
    @Override
    public ApiResponse execute(InputValue input) {
        List<String> errors = new ArrayList<>();
        AddIngredientRequest ingredientReq = input.addIngredientRequest();
        String message = new String();
        String ingredientName = ingredientReq.getName();
        Optional<Ingredient> existingIngredientOptional = ingredientRepo.getIngredientByName(ingredientReq.getName());

        if (existingIngredientOptional.isPresent()) {
            errors.add("Add ingredient " + ingredientName + " failed because this ingredient has already existed");
        } else {
            Optional<Supplier> supplierOptional = supplierRepo.findById(ingredientReq.getSupplierId());

            if (supplierOptional.isPresent()) {
                Ingredient ingredient = dbUtils.mergeMongoEntityFromRequest(new Ingredient(), ingredientReq);
                List<String> existingSubCategoryIds = new ArrayList<>();

                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = formatter.format(currentTime);

                String formatedDateStr = valueParsingUtils.parseStringToId("-", false, strDate);
                String formatedIngredientName = valueParsingUtils.parseStringToId("-", false, ingredientReq.getName());
                String ingredientId = formatedIngredientName + "_" + formatedDateStr;

                CompletableFuture<String> cfUploadedImageUrl = CompletableFuture.supplyAsync(
                        () -> firebaseUtils.upload(input.image(), ingredientId)
                ).exceptionally(ex -> {
                    log.error("Error: {}", ex.getMessage());

                    return null;
                });

                for(String subCategoryId: ingredient.getSubCategoryIds()) {
                    List<Category> existingCategories = categoryRepo.findBySubCategoryId(subCategoryId);

                    if(existingCategories.isEmpty()) {
                        errors.add("Category with ID " + subCategoryId + " of ingredient " + ingredientName + " does not exist, so it has been removed");
                    }
                    else {
                        existingSubCategoryIds.add(subCategoryId);
                    }
                }

                if (existingSubCategoryIds.size() != ingredient.getSubCategoryIds().size()) {
                    ingredient.setSubCategoryIds(existingSubCategoryIds);
                }

                String uploadedImageUrl = cfUploadedImageUrl.get();

                if(uploadedImageUrl == null || uploadedImageUrl.isBlank()) {
                    errors.add("Failed to upload ingredient image");
                }

                ingredient.setImage(uploadedImageUrl);
                ingredient.setId(ingredientId);
                ingredient.setCreationDate(currentTime);
                ingredient.setStatus(IngredientStatusEnum.AVAILABLE);

                try {
                    ingredientRepo.insert(ingredient);
                    message = "Add ingredient " + formatedIngredientName + " successfully";
                }
                catch (Exception e) {
                    CompletableFuture.runAsync(
                            () -> firebaseUtils.delete(ingredientId)
                    ).exceptionally(ex -> {
                        log.error("Error: {}", ex.getMessage());

                        return null;
                    });

                    throw e;
                }

            } else {
                errors.add("Supplier with ID " + ingredientReq.getSupplierId() +" does not exist");
                message = "Add new ingredient failed";

                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(errors)
                        .message(message)
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .errors(errors)
                .message(message)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(AddIngredientRequest addIngredientRequest, MultipartFile image) implements UseCase.InputValue {
    }
}
