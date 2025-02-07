package com.management_system.resource.entities.database.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.utilities.constant.ConstantValue;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Currency;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuPrice {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantValue.DATETIME_ISO_8601_FORMAT)
    @JsonProperty("creation_date")
    Date creationDate;

    Double price;

    @JsonProperty("update_manager_id")
    String updateManagerId;

    @Enumerated(EnumType.STRING)
    Currency currency;
}
