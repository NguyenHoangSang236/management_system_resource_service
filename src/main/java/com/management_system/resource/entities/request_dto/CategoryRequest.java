package com.management_system.resource.entities.request_dto;

import com.management_system.resource.entities.database.ingredient.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    String id;
    String name;
    List<SubCategory> subCategories;
}
