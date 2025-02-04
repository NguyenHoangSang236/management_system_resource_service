package com.management_system.resource.usecases.menu;

import com.management_system.resource.entities.database.menu.Menu;
import com.management_system.resource.entities.database.menu.MenuPrice;
import com.management_system.resource.entities.request_dto.menu.MenuRequest;
import com.management_system.resource.infrastucture.constant.MenuStatusEnum;
import com.management_system.resource.infrastucture.repository.CategoryRepository;
import com.management_system.resource.infrastucture.repository.MenuRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.database.TokenInfo;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.JwtUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import com.mongodb.MongoWriteException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AddNewMenuProductsUseCase extends UseCase<AddNewMenuProductsUseCase.InputValue, ApiResponse> {
    final MenuRepository menuRepo;
    final CategoryRepository categoryRepo;
    final DbUtils dbUtils;
    final JwtUtils jwtUtils;
    final ValueParsingUtils valueParsingUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        List<MenuRequest> menuRequestList = input.menuRequest();

        if (!menuRequestList.isEmpty()) {
            int successCount = 0;
            Map<String, String> resMap = new HashMap<>();

            for (MenuRequest request : menuRequestList) {
                String menuProductName = request.getName();

                try {
                    Optional<List<Menu>> menuOptional = menuRepo.getMenuProductsByName(menuProductName);

                    if (menuOptional.isPresent() && !menuOptional.get().isEmpty()) {
                        resMap.put(menuProductName, "Add menu product " + menuProductName + " failed because this product has already existed");
                    } else if (categoryRepo.findById(request.getCategoryId()).isEmpty()) {
                        resMap.put(menuProductName, "Add menu product " + menuProductName + " failed because the category does not exist");
                    } else {
                        Menu menu = new Menu();
                        TokenInfo tokenInfo = jwtUtils.getTokenInfoFromHttpRequest(input.request());
                        String addedNewMenuItemUserName = tokenInfo.getUserName();
                        menu = dbUtils.mergeMongoEntityFromRequest(menu, request);
                        String id = valueParsingUtils.parseStringToId("_", false, menuProductName);

                        List<MenuPrice> menuPrices = new ArrayList<>();
                        menuPrices.add(
                                MenuPrice.builder()
                                        .price(request.getPrice())
                                        .startDate(new Date())
                                        .currency(request.getCurrency())
                                        .updateManagerId(addedNewMenuItemUserName)
                                        .build()
                        );

                        menu.setPrices(menuPrices);
                        menu.setId(id);
                        if (menu.getMenuStatus() == null) {
                            menu.setMenuStatus(MenuStatusEnum.NEW);
                        }

                        menuRepo.save(menu);
                        successCount++;
                        resMap.put(menuProductName, "Add menu product " + menuProductName + " successfully");
                    }
                } catch (DuplicateKeyException e) {
                    Throwable rootCause = e.getRootCause();

                    if (rootCause instanceof MongoWriteException) {
                        resMap.put(menuProductName, "Add menu product " + menuProductName + " failed because " + ((MongoWriteException) rootCause).getError().getMessage());
                    } else {
                        resMap.put(menuProductName, "Add menu product " + menuProductName + " failed because " + e.getLocalizedMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    resMap.put(menuProductName, "Add menu product " + menuProductName + " failed because " + e.getMessage());
                }
            }

            return ApiResponse.builder()
                    .result(ResponseResult.success.name())
                    .content(resMap)
                    .message("Add " + successCount + "/" + menuRequestList.size() + " new ingredients successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("There is no product to add!")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public record InputValue(List<MenuRequest> menuRequest, HttpServletRequest request) implements UseCase.InputValue {
    }
}
