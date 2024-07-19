package com.management_system.ingredient.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.utilities.entities.database.MongoDbEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Indexed(unique = true)
    String id;

    @Field(name = "name")
    String name;

    @JsonProperty(value = "sub_categories")
    @Field(name = "sub_categories")
    List<SubCategory> subCategories;
}
