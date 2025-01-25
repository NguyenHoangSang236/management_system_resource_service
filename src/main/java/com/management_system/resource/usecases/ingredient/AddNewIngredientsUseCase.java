package com.management_system.resource.usecases.ingredient;

import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.IngredientRequest;
import com.management_system.resource.infrastucture.constant.IngredientStatusEnum;
import com.management_system.resource.infrastucture.repository.IngredientRepository;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AddNewIngredientsUseCase extends UseCase<AddNewIngredientsUseCase.InputValue, ApiResponse> {
    @Autowired
    IngredientRepository ingredientRepo;

    @Autowired
    SupplierRepository supplierRepo;

    @Autowired
    ValueParsingUtils valueParsingUtils;

    @Autowired
    DbUtils dbUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            List<IngredientRequest> ingredientRequests = input.ingredientRequests();
            Map<String, String> resMap = new HashMap<>();
            int successCount = 0;

            if (!ingredientRequests.isEmpty()) {
                for (IngredientRequest ingredientReq : ingredientRequests) {
                    String ingredientName = ingredientReq.getName();

                    if (ingredientRepo.getIngredientByName(ingredientReq.getName()).isPresent()) {
                        resMap.put(ingredientName, "Add ingredient " + ingredientName + " failed because this ingredient has already existed");
                    } else if (ingredientReq.getSupplierName().isBlank()) {
                        resMap.put(ingredientName, "Add ingredient " + ingredientName + " failed because supplier's name is null");
                    } else if (ingredientReq.getCategories() == null || ingredientReq.getCategories().isEmpty()) {
                        resMap.put(ingredientName, "Add ingredient " + ingredientName + " failed because there is no category");
                    } else {
                        Optional<List<Supplier>> supplierOptional = supplierRepo.getSupplierByName(ingredientReq.getSupplierName());

                        if (supplierOptional.isPresent() && !supplierOptional.get().isEmpty()) {
                            Ingredient ingredient = new Ingredient();
                            ingredient = dbUtils.mergeMongoEntityFromRequest(ingredient, ingredientReq);

                            Date currentTime = new Date();

                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String strDate = formatter.format(currentTime);

                            String formatedDateStr = valueParsingUtils.parseStringToId("-", false, strDate);
                            String formatedIngredientName = valueParsingUtils.parseStringToId("-", false, ingredientReq.getName());
                            String ingredientId = formatedIngredientName + "_" + formatedDateStr;

                            ingredient.setId(ingredientId);
                            ingredient.setCreationDate(currentTime);
                            ingredient.setStatus(IngredientStatusEnum.AVAILABLE);

                            ingredientRepo.insert(ingredient);

                            successCount++;
                            resMap.put(ingredientName, "Add ingredient " + formatedIngredientName + " successfully");
                        } else {
                            resMap.put(ingredientName, "Add ingredient" + ingredientReq.getSupplierName() + " failed because supplier does not exist");
                        }
                    }
                }

                return ApiResponse.builder()
                        .result(ResponseResult.success.name())
                        .content(resMap)
                        .message("Add " + successCount + "/" + ingredientRequests.size() + " new ingredients successfully")
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message("There is no ingredient to add")
                        .status(HttpStatus.OK)
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

    public record InputValue(HttpServletRequest request,
                             List<IngredientRequest> ingredientRequests) implements UseCase.InputValue {
    }
}
