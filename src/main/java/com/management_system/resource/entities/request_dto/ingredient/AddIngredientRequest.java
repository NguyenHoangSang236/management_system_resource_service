package com.management_system.resource.entities.request_dto.ingredient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.IngredientMeasurementUnitEnum;
import com.management_system.resource.infrastucture.constant.IngredientStatusEnum;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddIngredientRequest extends ApiRequest implements Serializable {
    @JsonProperty("supplier_id")
    @NotNull(message = "Supplier ID must not be null")
    @NotBlank(message = "Supplier ID must not be empty")
    String supplierId;

    @NotNull(message = "Ingredient name must not be null")
    String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantValue.DATETIME_ISO_8601_FORMAT)
    @JsonProperty(value = "last_update_time")
    Date lastUpdateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantValue.DATETIME_ISO_8601_FORMAT)
    @JsonProperty(value = "creation_date")
    Date creationDate;

    @JsonProperty(value = "last_update_username")
    String lastUpdateUsername;

    @Enumerated(EnumType.STRING)
    IngredientStatusEnum status;

    @Field(name = "note")
    String note;

    @NotNull(message = "Measurement unit must not be null")
    @JsonProperty(value = "measurement_unit")
    @Enumerated(EnumType.STRING)
    IngredientMeasurementUnitEnum measurementUnit;

    @Min(0)
    @NotNull(message = "Quantity must not be null")
    double quantity;

    @JsonProperty(value = "sub_category_ids")
    @NotNull(message = "List of sub-category IDs must not be null")
    @NotEmpty(message = "List of sub-category IDs must not be empty")
    List<String> subCategoryIds;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<>() {
        });
    }
}
