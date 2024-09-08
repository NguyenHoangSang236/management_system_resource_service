package com.management_system.resource.entities.database.ingredient;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategory {
    @NonNull
    String id;

    @NonNull
    String name;
}
