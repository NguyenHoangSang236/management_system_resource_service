package com.management_system.resource.usecases.supplier;

import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.SupplierRequest;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
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

    @Autowired
    DbUtils dbUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        List<SupplierRequest> suppliers = input.supplier();

        for (SupplierRequest supplierReq : suppliers) {
            String supplierId = valueParsingUtils.parseStringToId("-", false, supplierReq.getName(), supplierReq.getOrganization());
            Supplier newSupplier = new Supplier();

            supplierReq.setId(supplierId);
            supplierReq.setStatus(SupplierStatusEnum.WORKING);

            newSupplier = dbUtils.mergeMongoEntityFromRequest(newSupplier, supplierReq);

            supplierRepo.insert(newSupplier);
        }

        return ApiResponse.builder()
                .result("success")
                .message("Add new suppliers successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(List<SupplierRequest> supplier) implements UseCase.InputValue {
    }
}
