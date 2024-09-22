package com.management_system.resource.entities.database.menu;

import com.management_system.resource.infrastucture.constant.MenuStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    String id;

    @Field(name = "name", write = Field.Write.NON_NULL)
    String name;

    @Field(name = "image", write = Field.Write.NON_NULL)
    String image;

    @Field(name = "description")
    String description;

    @Field(name = "current_discount_id")
    String currentDiscountId;

    @Field(name = "category_id", write = Field.Write.NON_NULL)
    String categoryId;

    @Field(name = "sub_category_ids", write = Field.Write.NON_NULL)
    List<String> subCategoryIds;

    @Field(name = "prices", write = Field.Write.NON_NULL)
    List<MenuPrice> prices;

    @Enumerated(EnumType.STRING)
    @Field(name = "status", write = Field.Write.NON_NULL)
    MenuStatusEnum menuStatus;
}
