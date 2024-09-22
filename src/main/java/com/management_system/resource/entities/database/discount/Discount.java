package com.management_system.resource.entities.database.discount;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount {
    @Id
    String id;

    @Field(name = "name", write = Field.Write.NON_NULL)
    String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("start_date")
    @Field(name = "start_date", write = Field.Write.NON_NULL)
    Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("end_date")
    @Field(name = "end_date", write = Field.Write.NON_NULL)
    Date endDate;

    @Field(name = "image")
    String image;

    @Field(name = "description")
    String description;

    @Field(name = "condition", write = Field.Write.NON_NULL)
    String condition;

    @Field(name = "available_amount")
    int availableAmount;

    @Field(name = "percentage")
    Double percentage;

    @Field(name = "money")
    Double money;
}
