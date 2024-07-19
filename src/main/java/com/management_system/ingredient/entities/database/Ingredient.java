package com.management_system.ingredient.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.ingredient.infrastucture.constant.IngredientMeasurementUnitEnum;
import com.management_system.ingredient.infrastucture.constant.IngredientStatusEnum;
import com.management_system.utilities.entities.database.MongoDbEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("ingredients")
@Builder
public class Ingredient extends MongoDbEntity implements Serializable {
    @Id
    @Indexed(unique = true)
    String id;

    @JsonProperty(value = "supplier_name")
    @Field(name = "supplier_name")
    String supplierName;

    @Field(name = "name")
    String name;

    @Field(name = "image")
    String image;

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

    @JsonProperty(value = "categories")
    @Field(name = "categories")
    List<String> categories;
}
