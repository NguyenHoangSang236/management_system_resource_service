package com.management_system.resource.usecases.facility;

import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddNewFacilityUseCase extends UseCase<AddNewFacilityUseCase.InputValue, ApiResponse> {
    @Override
    public ApiResponse execute(InputValue input) {



        return null;
    }

    public record InputValue(HttpServletRequest request, Facility facility) implements UseCase.InputValue {
    }
}
