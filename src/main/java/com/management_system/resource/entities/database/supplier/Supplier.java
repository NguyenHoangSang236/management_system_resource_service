package com.management_system.resource.entities.database.supplier;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Getter
@Setter
@Document("suppliers")
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    String id;

    @Field(value = "name")
    String name;

    @Field(value = "address")
    String address;

    @Field(value = "email")
    String email;

    @Field(value = "phone_number")
    String phoneNumber;
}
