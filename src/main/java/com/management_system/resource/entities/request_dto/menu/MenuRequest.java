package com.management_system.resource.entities.request_dto.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.infrastucture.constant.MenuStatusEnum;
import com.management_system.utilities.core.validator.insert.InsertValid;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class MenuRequest extends ApiRequest implements Serializable {
    String id;

    @InsertValid(nullMessage = "Menu product name can not be null")
    String name;

    @InsertValid(nullMessage = "Menu product image can not be null")
    String image;

    String description;

    @JsonProperty("current_discount_id")
    String currentDiscountId;

    @JsonProperty("category_id")
    @InsertValid(nullMessage = "Category ID can not be null")
    String categoryId;

    @JsonProperty("sub_category_ids")
    List<String> subCategoryIds;

    @InsertValid(nullMessage = "Product price can not be null")
    Double price;

    @InsertValid(nullMessage = "Currency can not be null")
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
