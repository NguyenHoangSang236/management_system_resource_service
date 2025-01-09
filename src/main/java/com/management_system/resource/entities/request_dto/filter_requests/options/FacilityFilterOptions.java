package com.management_system.resource.entities.request_dto.filter_requests.options;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.FacilityStatusEnum;
import com.management_system.resource.infrastucture.constant.FacilityTypeEnum;
import com.management_system.utilities.core.filter.FilterOption;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityFilterOptions extends FilterOption implements Serializable {
    @Enumerated(EnumType.STRING)
    FacilityTypeEnum type;

    String name;

    @Enumerated(EnumType.STRING)
    FacilityStatusEnum status;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<Map<String, Object>>() {
        });
    }
}
