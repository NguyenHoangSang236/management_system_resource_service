package com.management_system.resource.entities.database.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.entities.database.MongoDbEntity;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Field(name = "name", write = Field.Write.NON_NULL)
    String name;

    @NotNull
    @Field(name = "type", write = Field.Write.NON_NULL)
    TableName type;

    @JsonProperty(value = "sub_categories")
    @Field(name = "sub_categories", write = Field.Write.NON_NULL)
    List<SubCategory> subCategories;
}
