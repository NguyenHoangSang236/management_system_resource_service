package com.management_system.resource.entities.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest implements Serializable {
    String id;

    @Field(value = "name")
    String name;

    @Field(value = "organization")
    String organization;

    @Field(value = "address")
    String address;

    @Field(value = "email")
    String email;

    @Enumerated(EnumType.STRING)
    @Field(value = "status")
    SupplierStatusEnum status;

    @JsonProperty(value = "phone_number")
    String phoneNumber;
}
