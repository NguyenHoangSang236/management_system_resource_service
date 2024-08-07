package com.management_system.resource.entities.database.importation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.utilities.constant.enumuration.MeasurementUnitEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Builder
@Getter
@Setter
@Document("importation")
@NoArgsConstructor
@AllArgsConstructor
public class Importation {
    @Id
    String id;

    @Field(value = "image")
    String image;

    @Field(value = "quantity")
    double quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("import_date")
    @Field(name = "import_date")
    Date importDate;

    @Enumerated(EnumType.STRING)
    @JsonProperty("measurement_unit")
    @Field(name = "measurement_unit")
    MeasurementUnitEnum measurementUnit;

    @JsonProperty("ingredient_id")
    @Field(value = "ingredient_id")
    String ingredientId;

    @JsonProperty("facility_id")
    @Field(value = "facility_id")
    String facilityId;

    @JsonProperty("importation_status")
    @Field(value = "importation_status")
    ImportationStatus importationStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("manufacture_date")
    @Field(name = "manufacture_date")
    Date manufactureDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("expiration_date")
    @Field(name = "expiration_date")
    Date expirationDate;

    @JsonProperty("supplier")
    @Field(value = "supplier")
    Supplier supplier;
}
