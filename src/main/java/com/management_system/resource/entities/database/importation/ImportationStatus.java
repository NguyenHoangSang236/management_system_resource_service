package com.management_system.resource.entities.database.importation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.resource.infrastucture.constant.ImportationStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportationStatus {
    @Enumerated(EnumType.STRING)
    @Field(name = "name", write = Field.Write.NON_NULL)
    ImportationStatusEnum name;

    @JsonProperty(value = "debt_amount")
    @Field(name = "debt_amount")
    String debAmount;

    @JsonProperty(value = "debt_money")
    @Field(name = "debt_money")
    String debtMoney;

    @JsonProperty(value = "failed_amount")
    @Field(name = "failed_amount")
    String failedAmount;

    @JsonProperty(value = "success_amount")
    @Field(name = "success_amount")
    String successAmount;

    @JsonProperty(value = "paid_money")
    @Field(name = "paid_money")
    String paidMoney;

    @Field(name = "note")
    String note;
}
