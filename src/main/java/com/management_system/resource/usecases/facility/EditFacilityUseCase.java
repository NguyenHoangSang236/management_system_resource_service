package com.management_system.resource.usecases.facility;

import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.entities.request_dto.facility.EditFacilityRequest;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.FacilityRepository;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.FirebaseUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class EditFacilityUseCase extends UseCase<EditFacilityUseCase.InputValue, ApiResponse> {
    private final FacilityRepository facilityRepo;
    private final SupplierRepository supplierRepo;
    private final DbUtils dbUtils;
    private final FirebaseUtils firebaseUtils;
    private final RedisServiceClient redisServiceClient;

    public EditFacilityUseCase(FacilityRepository facilityRepo, SupplierRepository supplierRepo, DbUtils dbUtils, FirebaseUtils firebaseUtils, RedisServiceClient redisServiceClient) {
        this.facilityRepo = facilityRepo;
        this.supplierRepo = supplierRepo;
        this.dbUtils = dbUtils;
        this.firebaseUtils = firebaseUtils;
        this.redisServiceClient = redisServiceClient;
    }


    @SneakyThrows
    @Override
    public ApiResponse execute(InputValue input) {
        EditFacilityRequest facilityRequest = input.facilityRequest();
        MultipartFile image = input.image();
        String id = facilityRequest.getId();
        String supplierId = facilityRequest.getSupplierId();
        List<String> errors = new ArrayList<>();
        Optional<Facility> facilityOptional = facilityRepo.findById(id);

        if (facilityOptional.isPresent()) {
            if(supplierId != null && !supplierId.isBlank() && supplierRepo.findById(supplierId).isEmpty()) {
                errors.add("Supplier with ID " + facilityRequest.getSupplierId() +" does not exist");

                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(errors)
                        .message("Edit facility " + id + " failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            CompletableFuture<String> cfImgUrl = null;

            if(image != null) {
                cfImgUrl = CompletableFuture.supplyAsync(
                        () -> firebaseUtils.upload(image, id)
                ).exceptionally(ex -> {
                    log.error("Error: {}", ex.getMessage());

                    return null;
                });
            }

            String imgUrl = null;
            Facility facility = dbUtils.mergeMongoEntityFromRequest(facilityOptional.get(), facilityRequest);

            if(cfImgUrl != null) {
                imgUrl = cfImgUrl.get();

                if(imgUrl != null && !imgUrl.isBlank()) {
                    facility.setImage(imgUrl);
                }
                else {
                    errors.add("Can not change image");
                }
            }

            try {
                facilityRepo.save(facility);
            }
            catch (Exception e) {
                if(imgUrl != null) {
                    CompletableFuture.runAsync(
                            () -> firebaseUtils.delete(id)
                    ).exceptionally(ex -> {
                        log.error("Error: {}", ex.getMessage());

                        return null;
                    });
                }

                throw e;
            }

            CompletableFuture.runAsync(() -> redisServiceClient.delete(TableName.FACILITY, id, true))
                    .exceptionally(
                            ex -> {
                                ex.printStackTrace();

                                return null;
                            }
                    );

            return ApiResponse.builder()
                    .errors(errors)
                    .result(ResponseResult.success.name())
                    .message("Edit facility with ID " + facilityRequest.getId() + " successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Facility with ID " + id + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public record InputValue(EditFacilityRequest facilityRequest, MultipartFile image) implements UseCase.InputValue {
    }
}
