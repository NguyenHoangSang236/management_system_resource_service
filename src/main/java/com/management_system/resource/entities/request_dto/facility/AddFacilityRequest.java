package com.management_system.resource.entities.request_dto.facility;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.FacilityStatusEnum;
import com.management_system.resource.infrastucture.constant.FacilityTypeEnum;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddFacilityRequest extends ApiRequest implements Serializable {
    @NotNull(message = "Facility name must not be null")
    @NotBlank(message = "Facility name must not be empty")
    @Size(max = 50)
    String name;

    @NotNull(message = "Facility type must not be null")
    @Enumerated(EnumType.STRING)
    FacilityTypeEnum type;

    @NotNull(message = "Facility quantity must not be null")
    @Min(0)
    Integer quantity;

    String note;

    @JsonProperty("supplier_id")
    @NotNull(message = "Supplier ID must not be null")
    @NotBlank(message = "Supplier ID must not be empty")
    @Size(max = 30)
    String supplierId;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<>() {
        });
    }
}
