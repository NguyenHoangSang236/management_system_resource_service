package com.management_system.resource.entities.request_dto.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.MenuStatusEnum;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.Currency;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMenuRequest extends ApiRequest implements Serializable {
    @NotNull(message = "Menu product name must not be null")
    @NotBlank(message = "Menu product name must not be empty")
    String name;

    String description;

    @Size(max = 30)
    @JsonProperty("current_discount_id")
    String currentDiscountId;

    @NotNull(message = "List of sub-category IDs must not be null")
    @NotEmpty(message = "List of sub-category IDs must not be empty")
    @Size(max = 10)
    @JsonProperty("sub_category_ids")
    List<String> subCategoryIds;

    @NotNull
    @Min(0)
    @Max(1000000)
    Double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    Currency currency;

    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    MenuStatusEnum menuStatus;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<>() {
        });
    }
}
