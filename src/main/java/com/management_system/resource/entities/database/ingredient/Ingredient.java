package com.management_system.resource.entities.database.ingredient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.resource.infrastucture.constant.IngredientMeasurementUnitEnum;
import com.management_system.resource.infrastucture.constant.IngredientStatusEnum;
import com.management_system.utilities.entities.database.MongoDbEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Document("ingredients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ingredient extends MongoDbEntity implements Serializable {
    @Id
    String id;

    @JsonProperty(value = "supplier_id")
    @Field(name = "supplier_name", write = Field.Write.NON_NULL)
    String supplierId;

    @Field(name = "name", write = Field.Write.NON_NULL)
    String name;

    @Field(name = "image", write = Field.Write.NON_NULL)
    String image;

    @Enumerated(EnumType.STRING)
    @Field(name = "status", write = Field.Write.NON_NULL)
    IngredientStatusEnum status;

    @Field(name = "note")
    String note;

    @JsonProperty(value = "measurement_unit")
    @Enumerated(EnumType.STRING)
    @Field(name = "measurement_unit", write = Field.Write.NON_NULL)
    IngredientMeasurementUnitEnum measurementUnit;

    @Field(name = "quantity", write = Field.Write.NON_NULL)
    double quantity;

    @JsonProperty(value = "sub_category_ids")
    @Field(name = "sub_category_ids", write = Field.Write.NON_NULL)
    List<String> subCategoryIds;
}
