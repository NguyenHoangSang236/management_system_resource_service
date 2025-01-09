package com.management_system.resource.entities.request_dto.filter_requests;

import com.management_system.resource.entities.request_dto.filter_requests.options.SupplierFilterOptions;
import com.management_system.utilities.entities.api.request.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SupplierFilterRequest extends FilterRequest<SupplierFilterOptions> {
}

