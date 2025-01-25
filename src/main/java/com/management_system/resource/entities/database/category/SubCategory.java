package com.management_system.resource.entities.database.category;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategory {
    @NotNull
    String id;

    @NotNull
    String name;
}
