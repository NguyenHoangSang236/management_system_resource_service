package com.management_system.resource.usecases.resource;

import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.infrastucture.constant.IngredientStatusEnum;
import com.management_system.resource.infrastucture.repository.IngredientRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.TokenInfo;
import com.management_system.utilities.utils.JwtUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class AddNewIngredientsUseCase extends UseCase<AddNewIngredientsUseCase.InputValue, ApiResponse> {
    @Autowired
    IngredientRepository ingredientRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ValueParsingUtils valueParsingUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        TokenInfo tokenInfo = jwtUtils.getTokenInfoFromHttpRequest(input.request());
        List<Ingredient> newIngredients = input.ingredients();

        for (Ingredient newIngredient : newIngredients) {
            Date currentTime = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = formatter.format(currentTime);

            String formatedDateStr = valueParsingUtils.parseStringToId(strDate, "-", false);
            String formatedIngredientName = valueParsingUtils.parseStringToId(newIngredient.getName(), "-", false);
            String ingredientId = formatedIngredientName + "_" + formatedDateStr;

            newIngredient.setId(ingredientId);
            newIngredient.setCreationDate(currentTime);
            newIngredient.setStatus(IngredientStatusEnum.AVAILABLE);
            newIngredient.setLastUpdateUsername(tokenInfo.getUserName());

            ingredientRepo.insert(newIngredient);
        }

        return ApiResponse.builder()
                .result("success")
                .content("Add new ingredients successfully")
                .status(HttpStatus.OK)
                .build();

    }

    public record InputValue(HttpServletRequest request, List<Ingredient> ingredients) implements UseCase.InputValue {
    }
}
