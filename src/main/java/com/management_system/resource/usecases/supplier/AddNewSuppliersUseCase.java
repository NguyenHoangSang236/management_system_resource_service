package com.management_system.resource.usecases.supplier;

import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.infrastucture.constant.FacilityStatusEnum;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.ValueParsingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AddNewSuppliersUseCase extends UseCase<AddNewSuppliersUseCase.InputValue, ApiResponse> {
    @Autowired
    SupplierRepository supplierRepo;

    @Autowired
    ValueParsingUtils valueParsingUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        List<Supplier> suppliers = input.suppliers();

        for (Supplier supplier : suppliers) {
            Date currentTime = new Date();

            String supplierId = valueParsingUtils.parseStringToId("-", false, supplier.getName(), supplier.getOrganization());

            supplier.setId(supplierId);
            supplier.setCreationDate(currentTime);
            supplier.setStatus(SupplierStatusEnum.WORKING);

            supplierRepo.insert(supplier);
        }

        return ApiResponse.builder()
                .result("success")
                .message("Add new suppliers successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(List<Supplier> suppliers) implements UseCase.InputValue{}
}
