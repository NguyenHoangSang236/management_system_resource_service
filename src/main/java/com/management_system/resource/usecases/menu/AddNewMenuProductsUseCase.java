package com.management_system.resource.usecases.menu;

import com.management_system.resource.entities.database.menu.Menu;
import com.management_system.resource.entities.database.menu.MenuPrice;
import com.management_system.resource.entities.request_dto.menu.AddMenuRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
public class AddNewMenuProductsUseCase extends UseCase<AddNewMenuProductsUseCase.InputValue, ApiResponse> {
    final MenuRepository menuRepo;
    final CategoryRepository categoryRepo;
    final DbUtils dbUtils;
    final JwtUtils jwtUtils;
    final ValueParsingUtils valueParsingUtils;

    public AddNewMenuProductsUseCase(MenuRepository menuRepo, CategoryRepository categoryRepo, DbUtils dbUtils, JwtUtils jwtUtils, ValueParsingUtils valueParsingUtils) {
        this.menuRepo = menuRepo;
        this.categoryRepo = categoryRepo;
        this.dbUtils = dbUtils;
        this.jwtUtils = jwtUtils;
        this.valueParsingUtils = valueParsingUtils;
    }


    @Override
    public ApiResponse execute(InputValue input) {
        AddMenuRequest menuRequest = input.menuRequest();
        MultipartFile image = input.image();
        String name = menuRequest.getName();
        List<String> errors = new ArrayList<>();
        String id = valueParsingUtils.parseStringToId("-", false, name);

        if(menuRepo.existsById(id)) {
            errors.add("Add product " + name + " failed because this menu product has already existed");
        }
        else {

        }

        return null;
    }

    public record InputValue(AddMenuRequest menuRequest, MultipartFile image, HttpServletRequest request) implements UseCase.InputValue {
    }
}
