package com.management_system.resource.usecases.supplier;

import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.SupplierRequest;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import com.mongodb.MongoWriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Map<String, Object> resMap = new HashMap<>();
        int successCount = 0;

        for (SupplierRequest supplierReq : suppliers) {
            String supplierName = supplierReq.getName();

            try {
                Optional<List<Supplier>> supplierOptional = supplierRepo.getSupplierByName(supplierReq.getName());

                if (supplierOptional.isPresent() && !supplierOptional.get().isEmpty()) {
                    resMap.put(supplierName, "Add supplier " + supplierName + " failed because this supplier has already existed");
                } else {
                    String supplierId = valueParsingUtils.parseStringToId("-", false, supplierReq.getName(), supplierReq.getOrganization());
                    Supplier newSupplier = new Supplier();

                    supplierReq.setId(supplierId);

                    if (supplierReq.getStatus() == null) {
                        supplierReq.setStatus(SupplierStatusEnum.WORKING);
                    }

                    newSupplier = dbUtils.mergeMongoEntityFromRequest(newSupplier, supplierReq);

                    supplierRepo.insert(newSupplier);

                    successCount++;
                    resMap.put(supplierName, "Add new supplier " + supplierName + " successfully");
                }
            } catch (DuplicateKeyException e) {
                Throwable rootCause = e.getRootCause();

                if (rootCause instanceof MongoWriteException) {
                    resMap.put(supplierName, "Add supplier " + supplierName + " failed because " + ((MongoWriteException) rootCause).getError().getMessage());
                } else {
                    resMap.put(supplierName, "Add supplier " + supplierName + " failed because " + e.getLocalizedMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
                resMap.put(supplierName, "Add supplier " + supplierName + " failed because " + e.getMessage());
            }
        }

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .content(resMap)
                .message("Add " + successCount + "/" + suppliers.size() + " new suppliers successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(List<SupplierRequest> supplier) implements UseCase.InputValue {
    }
}
