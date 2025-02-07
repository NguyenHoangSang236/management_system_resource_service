package com.management_system.resource.entities.request_dto.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.utilities.core.validator.insert.InsertValid;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditSupplierRequest extends ApiRequest implements Serializable {
    @NotNull(message = "Supplier ID must not be null")
    @NotBlank(message = "Supplier ID must not be empty")
    @Size(max = 30)
    String id;

    @Size(max = 30)
    String name;

    @Size(max = 50)
    String organization;

    @Size(max = 200)
    String address;

    @InsertValid(
            emailMessage = "Invalid email",
            nullable = true,
            isEmail = true
    )
    String email;

    @Enumerated(EnumType.STRING)
    @Field(value = "status")
    SupplierStatusEnum status;

    @InsertValid(
            phoneMessage = "Wrong format for phone number",
            nullable = true,
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
