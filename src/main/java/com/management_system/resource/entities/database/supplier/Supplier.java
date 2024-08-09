package com.management_system.resource.entities.database.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.resource.infrastucture.constant.SupplierStatusEnum;
import com.management_system.utilities.entities.database.MongoDbEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Builder
@Getter
@Setter
@Document("suppliers")
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends MongoDbEntity implements Serializable {
    @Id
    String id;

    @Field(value = "name")
    String name;

    @Field(value = "organization")
    String organization;

    @Field(value = "address")
    String address;

    @Indexed(unique = true, name = "unique_supplier_email_index")
    @Field(value = "email")
    String email;

    @Enumerated(EnumType.STRING)
    @Field(value = "status")
    SupplierStatusEnum status;

    @Indexed(unique = true, name = "unique_supplier_phone_number_index")
    @JsonProperty(value = "phone_number")
    @Field(value = "phone_number")
    String phoneNumber;
}
