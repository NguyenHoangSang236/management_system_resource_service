package com.management_system.resource.entities.request_dto.filter_requests;

import com.management_system.resource.entities.request_dto.filter_requests.options.FacilityFilterOptions;
import com.management_system.utilities.entities.api.request.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FacilityFilterRequest extends FilterRequest<FacilityFilterOptions> {
}
