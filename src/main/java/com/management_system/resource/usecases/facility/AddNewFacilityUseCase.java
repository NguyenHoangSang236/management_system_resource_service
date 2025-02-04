package com.management_system.resource.usecases.facility;

import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.entities.request_dto.facility.AddFacilityRequest;
import com.management_system.resource.infrastucture.constant.FacilityStatusEnum;
import com.management_system.resource.infrastucture.repository.FacilityRepository;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.FirebaseUtils;
import com.management_system.utilities.utils.ValueParsingUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class AddNewFacilityUseCase extends UseCase<AddNewFacilityUseCase.InputValue, ApiResponse> {
    private final FacilityRepository facilityRepo;
    private final SupplierRepository supplierRepo;
    private final ValueParsingUtils valueParsingUtils;
    private final DbUtils dbUtils;
    private final FirebaseUtils firebaseUtils;

    public AddNewFacilityUseCase(FacilityRepository facilityRepo, SupplierRepository supplierRepo, ValueParsingUtils valueParsingUtils, DbUtils dbUtils, FirebaseUtils firebaseUtils) {
        this.facilityRepo = facilityRepo;
        this.supplierRepo = supplierRepo;
        this.valueParsingUtils = valueParsingUtils;
        this.dbUtils = dbUtils;
        this.firebaseUtils = firebaseUtils;
    }


    @SneakyThrows
    @Override
    public ApiResponse execute(InputValue input) {
        AddFacilityRequest facilityRequest = input.facilityRequest();
        Facility facility = new Facility();
        List<String> errors = new ArrayList<>();

        if(supplierRepo.findById(facilityRequest.getSupplierId()).isEmpty()) {
            errors.add("Supplier with ID " + facilityRequest.getSupplierId() +" does not exist");

            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .errors(errors)
                    .message("Add new facility failed")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Date currentTime = new Date();
        String facilityId = valueParsingUtils.parseStringToId("-", false, facilityRequest.getName());

        CompletableFuture<String> cfImgUrl = CompletableFuture.supplyAsync(
                () -> firebaseUtils.upload(input.image(), facilityId)
        ).exceptionally(ex -> {
            log.error("Error: {}", ex.getMessage());

            return null;
        });

        facility = dbUtils.mergeMongoEntityFromRequest(facility, facilityRequest);

        facility.setId(facilityId);
        facility.setCreationDate(currentTime);
        facility.setStatus(FacilityStatusEnum.AVAILABLE);

        String imgUrl = cfImgUrl.get();

        if(imgUrl != null) {
            facility.setImage(imgUrl);
        }
        else {
            errors.add("Can not add new image");
        }

        try {
            facilityRepo.insert(facility);
        }
        catch (Exception e) {
            e.printStackTrace();

            CompletableFuture.runAsync(
                    () -> firebaseUtils.delete(facilityId)
            ).exceptionally(ex -> {
                log.error("Error: {}", ex.getMessage());

                return null;
            });

            throw e;
        }

        return ApiResponse.builder()
                .result(ResponseResult.success.name())
                .message("Add new facility successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(AddFacilityRequest facilityRequest, MultipartFile image) implements UseCase.InputValue {
    }
}
