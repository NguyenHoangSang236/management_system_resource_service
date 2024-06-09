package com.management_system.ingredient.entities.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.ingredient.infrastucture.constant.IngredientStatusEnum;
import com.management_system.ingredient.infrastucture.constant.SortType;
import com.management_system.utilities.core.filter.FilterOption;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientFilterOptions extends FilterOption {
    String name;

    @Enumerated(EnumType.STRING)
    IngredientStatusEnum status;

    @JsonProperty("creation_date")
    Date creationDate;

    @JsonProperty("sort_type")
    @Enumerated(EnumType.STRING)
    SortType sortType;
}
