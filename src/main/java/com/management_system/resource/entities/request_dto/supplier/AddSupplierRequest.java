package com.management_system.resource.entities.request_dto.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.utilities.core.validator.insert.InsertValid;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSupplierRequest extends ApiRequest implements Serializable {
    @NotNull
    @Size(max = 30)
    String name;

    @NotNull
    @Size(max = 50)
    String organization;

    @NotNull
    String address;

    @InsertValid(
            nullMessage = "Email can not be null",
            emailMessage = "Invalid email",
            isEmail = true
    )
    String email;

    @Enumerated(EnumType.STRING)
    SupplierStatusEnum status;

    @InsertValid(
            nullMessage = "Phone number can not be null",
            phoneMessage = "Wrong format for phone number",
            isPhoneNumber = true
    )
    @JsonProperty(value = "phone_number")
    String phoneNumber;


    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<>() {
        });
    }
}
