package com.management_system.resource.entities.request_dto.filter_requests.options;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.IngredientMeasurementUnitEnum;
import com.management_system.resource.infrastucture.constant.IngredientStatusEnum;
import com.management_system.resource.infrastucture.constant.SortTypeEnum;
import com.management_system.utilities.core.filter.FilterOption;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonProperty("start_creation_date")
    Date startCreationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonProperty("end_creation_date")
    Date endCreationDate;

    List<String> categories;

    @JsonProperty("sort_type")
    @Enumerated(EnumType.STRING)
    SortTypeEnum sortType;


    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<Map<String, Object>>() {
        });
    }
}
