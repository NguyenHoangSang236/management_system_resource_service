package com.management_system.resource.entities.request_dto.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.utilities.core.validator.insert.InsertValid;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest extends ApiRequest implements Serializable {
    String id;

    @InsertValid(nullMessage = "Name can not be null")
    @Field(value = "name")
    String name;

    @InsertValid(nullMessage = "Organization can not be null")
    @Field(value = "organization")
    String organization;

    @InsertValid(nullMessage = "Address can not be null")
    @Field(value = "address")
    String address;

    @InsertValid(
            nullMessage = "Email can not be null",
            emailMessage = "Invalid email",
            isEmail = true
    )
    @Field(value = "email")
    String email;

    @Enumerated(EnumType.STRING)
    @Field(value = "status")
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
