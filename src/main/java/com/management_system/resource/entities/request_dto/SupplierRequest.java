package com.management_system.resource.entities.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.utilities.core.validator.EmailConstraint;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest extends ApiRequest implements Serializable {
    String id;

    @Field(value = "name")
    String name;

    @Field(value = "organization")
    String organization;

    @Field(value = "address")
    String address;

    @Email(message = "Invalid email format")
    @Field(value = "email")
    String email;

    @Enumerated(EnumType.STRING)
    @Field(value = "status")
    SupplierStatusEnum status;

    @JsonProperty(value = "phone_number")
    String phoneNumber;
}
