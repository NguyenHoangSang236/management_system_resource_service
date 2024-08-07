package com.management_system.resource.usecases.facility;

import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.infrastucture.constant.FacilityStatusEnum;
import com.management_system.resource.infrastucture.repository.FacilityRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.ValueParsingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AddNewFacilityUseCase extends UseCase<AddNewFacilityUseCase.InputValue, ApiResponse> {
    @Autowired
    FacilityRepository facilityRepo;

    @Autowired
    ValueParsingUtils valueParsingUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        List<Facility> facilities = input.facility();

        for (Facility facility : facilities) {
            Date currentTime = new Date();

            String formatedIngredientName = valueParsingUtils.parseStringToId(facility.getName(), "-", false);
            String facilityId = formatedIngredientName;

            facility.setId(facilityId);
            facility.setCreationDate(currentTime);
            facility.setStatus(FacilityStatusEnum.AVAILABLE);

            facilityRepo.insert(facility);
        }

        return ApiResponse.builder()
                .result("success")
                .message("Add new facilities successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(List<Facility> facility) implements UseCase.InputValue {
    }
}
