package com.management_system.resource.usecases.supplier;

import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.filter_requests.SupplierFilterRequest;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterSuppliersUseCase extends UseCase<FilterSuppliersUseCase.InputValue, ApiResponse> {
    @Autowired
    DbUtils dbUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        List<Supplier> resultList = dbUtils.filterData(input.filterRequest(), Supplier.class);

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .content(resultList)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(SupplierFilterRequest filterRequest) implements UseCase.InputValue {
    }
}
