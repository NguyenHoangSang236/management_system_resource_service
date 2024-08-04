package com.management_system.resource.usecases.facility;

import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterFacilitiesUseCase extends UseCase<FilterFacilitiesUseCase.InputValue, ApiResponse> {
    @Autowired
    DbUtils dbUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        List<Facility> resultList = dbUtils.filterData(input.filterRequest(), Facility.class);

        return ApiResponse.builder()
                .result("success")
                .content(resultList)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(FilterRequest filterRequest) implements UseCase.InputValue {
    }
}
