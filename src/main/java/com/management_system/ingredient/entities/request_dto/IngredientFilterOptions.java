package com.management_system.ingredient.entities.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.ingredient.infrastucture.constant.IngredientMeasurementUnitEnum;
import com.management_system.ingredient.infrastucture.constant.IngredientStatusEnum;
import com.management_system.ingredient.infrastucture.constant.SortType;
import com.management_system.utilities.core.filter.FilterOption;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientFilterOptions extends FilterOption implements Serializable {
    String name;

    @JsonProperty("supplier_name")
    String supplierNName;

    String id;

    @JsonProperty("last_update_user_name")
    String lastUpdateUserName;

    @JsonProperty("measurement_unit")
    @Enumerated(EnumType.STRING)
    IngredientMeasurementUnitEnum measurementUnit;

    @Enumerated(EnumType.STRING)
    IngredientStatusEnum status;

    @JsonProperty("start_creation_date")
    Date startCreationDate;

    @JsonProperty("end_creation_date")
    Date endCreationDate;

    @JsonProperty("sort_type")
    @Enumerated(EnumType.STRING)
    SortType sortType;


    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<Map<String, Object>>() {});
    }
}
