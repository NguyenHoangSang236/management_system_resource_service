package com.management_system.resource.entities.database.ingredient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.utilities.entities.database.MongoDbEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("categories")
@Builder
public class Category extends MongoDbEntity implements Serializable {
    @Id
    String id;

    @Field(name = "name", write = Field.Write.NON_NULL)
    String name;

    @JsonProperty(value = "sub_categories")
    @Field(name = "sub_categories", write = Field.Write.NON_NULL)
    List<SubCategory> subCategories;
}
