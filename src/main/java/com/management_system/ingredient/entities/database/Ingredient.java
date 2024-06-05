package com.management_system.ingredient.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.ingredient.infrastucture.constant.IngredientMeasurementUnitEnum;
import com.management_system.ingredient.infrastucture.constant.IngredientStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("ingredients")
@Builder
public class Ingredient implements Serializable {
    @Id
    @Indexed(unique = true)
    String id;

    @JsonProperty(value = "supplier_id")
    @Field(name = "supplier_id")
    String supplierId;

    @Field(name = "name")
    String name;

    @Field(name = "image")
    String image;

    @JsonProperty(value = "last_update_time")
    @Field(name = "last_update_time")
    Date lastUpdateTime;

    @JsonProperty(value = "creation_date")
    @Field(name = "creation_date")
    Date creationDate;

    @JsonProperty(value = "last_update_username")
    @Field(name = "last_update_username")
    String lastUpdateUsername;

    @Enumerated(EnumType.STRING)
    @Field(name = "status")
    IngredientStatusEnum status;

    @Field(name = "note")
    String note;

    @JsonProperty(value = "measurement_unit")
    @Enumerated(EnumType.STRING)
    @Field(name = "measurement_unit")
    IngredientMeasurementUnitEnum measurementUnit;

    @Field(name = "quantity")
    double quantity;
}
