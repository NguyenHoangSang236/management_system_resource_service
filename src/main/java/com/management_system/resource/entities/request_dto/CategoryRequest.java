package com.management_system.resource.entities.request_dto;

import com.management_system.resource.entities.database.ingredient.SubCategory;
import com.management_system.utilities.entities.api.request.ApiRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest extends ApiRequest implements Serializable {
    String id;
    String name;
    List<SubCategory> subCategories;
}
