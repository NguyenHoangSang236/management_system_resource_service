package com.management_system.ingredient.entities.redis;

import com.management_system.utilities.core.redis.RedisData;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class IngredientRedisData extends RedisData implements Serializable {
    String name;

    public IngredientRedisData(String id, String name) {
        super(id);
        this.name = name;
    }
}
