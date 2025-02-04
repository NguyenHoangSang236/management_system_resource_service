package com.management_system.resource.usecases.supplier;

import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.supplier.AddSupplierRequest;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AddNewSuppliersUseCase extends UseCase<AddNewSuppliersUseCase.InputValue, ApiResponse> {
    private final SupplierRepository supplierRepo;
    private final ValueParsingUtils valueParsingUtils;
    private final DbUtils dbUtils;

    public AddNewSuppliersUseCase(SupplierRepository supplierRepo, ValueParsingUtils valueParsingUtils, DbUtils dbUtils) {
        this.supplierRepo = supplierRepo;
        this.valueParsingUtils = valueParsingUtils;
        this.dbUtils = dbUtils;
    }


    @Override
    public ApiResponse execute(InputValue input) {
        AddSupplierRequest supplierRequest = input.supplierRequest();
        List<String> errors = new ArrayList<>();

        String supplierName = supplierRequest.getName().trim();
        String organization = supplierRequest.getOrganization().trim();

        Optional<List<Supplier>> supplierOptional = supplierRepo.getSupplierByNameAndOrganization(supplierName, organization);

        if (supplierOptional.isPresent() && !supplierOptional.get().isEmpty()) {
            errors.add("Supplier with name " + supplierName + " and organization " + organization + " has already existed");

            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .errors(errors)
                    .message("Add new supplier failed")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            String supplierId = valueParsingUtils.parseStringToId("-", false, supplierRequest.getName(), supplierRequest.getOrganization());
            Supplier newSupplier = new Supplier();

            if (supplierRequest.getStatus() == null) {
                supplierRequest.setStatus(SupplierStatusEnum.WORKING);
            }

            newSupplier = dbUtils.mergeMongoEntityFromRequest(newSupplier, supplierRequest);
            newSupplier.setId(supplierId);

            supplierRepo.insert(newSupplier);
        }

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .errors(errors)
                .message("Add new supplier successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(AddSupplierRequest supplierRequest) implements UseCase.InputValue {
    }
}
