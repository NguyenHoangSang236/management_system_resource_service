package com.management_system.resource.usecases.facility;

import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.infrastucture.feign.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.FacilityRepository;
import com.management_system.utilities.constant.enumuration.FilterType;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class EditFacilityUseCase extends UseCase<EditFacilityUseCase.InputValue, ApiResponse> {
    @Autowired
    FacilityRepository facilityRepo;

    @Autowired
    DbUtils dbUtils;

    @Autowired
    RedisServiceClient redisServiceClient;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            Facility rqFacility = input.facility();
            Optional<Facility> facilityOptional = facilityRepo.findById(rqFacility.getId());

            if (facilityOptional.isPresent()) {
                facilityRepo.save(dbUtils.mergeMongoEntityFromRequest(facilityOptional.get(), input.facility()));

                CompletableFuture.runAsync(() -> redisServiceClient.deleteByKey(
                                FilterType.FACILITY.name(), input.facility().getId()))
                        .exceptionally(
                                ex -> {
                                    ex.printStackTrace();
                                    return null;
                                }
                        );

                return ApiResponse.builder()
                        .result("success")
                        .content("Edit facility with ID " + input.facility.getId() + " successfully")
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ApiResponse.builder()
                        .result("failed")
                        .content("This facility does not exist")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .content(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public record InputValue(Facility facility) implements UseCase.InputValue {
    }
}
