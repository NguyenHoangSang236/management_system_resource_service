package com.management_system.resource.entities.request_dto.filter_requests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryFilterRequest extends ApiRequest {
    String name;

    @Enumerated(EnumType.STRING)
    TableName type;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<>() {});
    }
}
