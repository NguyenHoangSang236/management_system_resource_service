package com.management_system.ingredient.usecases.ingredient;

import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.infrastucture.constant.IngredientStatusEnum;
import com.management_system.ingredient.infrastucture.repository.IngredientRepository;
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

@Component
public class AddNewIngredientUseCase extends UseCase<AddNewIngredientUseCase.InputValue, ApiResponse> {
    @Autowired
    IngredientRepository ingredientRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ValueParsingUtils valueParsingUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            TokenInfo tokenInfo = jwtUtils.getTokenInfoFromHttpRequest(input.request());
            Ingredient newIngredient = input.ingredient();

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

            return ApiResponse.builder()
                    .result("success")
                    .content("Add new ingredient successfully")
                    .status(HttpStatus.OK)
                    .build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.builder()
                    .result("failed")
                    .content(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public record InputValue(HttpServletRequest request, Ingredient ingredient) implements UseCase.InputValue{}
}
