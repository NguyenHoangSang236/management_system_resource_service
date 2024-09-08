package com.management_system.resource.entities.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.resource.infrastucture.constant.IngredientMeasurementUnitEnum;
import com.management_system.resource.infrastucture.constant.IngredientStatusEnum;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest extends ApiRequest implements Serializable {
    String id;

    String supplierName;

    String name;

    String image;

    @JsonProperty(value = "last_update_time")
    Date lastUpdateTime;

    @JsonProperty(value = "creation_date")
    Date creationDate;

    @JsonProperty(value = "last_update_username")
    String lastUpdateUsername;

    @Enumerated(EnumType.STRING)
    IngredientStatusEnum status;

    @Field(name = "note")
    String note;

    @JsonProperty(value = "measurement_unit")
    @Enumerated(EnumType.STRING)
    IngredientMeasurementUnitEnum measurementUnit;

    double quantity;

    List<String> categories;
}
