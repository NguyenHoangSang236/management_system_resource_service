package com.management_system.resource.entities.request_dto.filter_requests.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.utilities.core.filter.FilterOption;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierFilterOptions extends FilterOption implements Serializable {
    String name;

    @Email(message = "Invalid email format")
    String email;

    String address;

    String organization;

    @Size(max = 10)
    @JsonProperty(value = "phone_number")
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    SupplierStatusEnum status;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<>() {});
    }
}

