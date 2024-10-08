package com.management_system.resource.entities.redis;

import com.management_system.utilities.core.redis.RedisData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRedisData extends RedisData implements Serializable {
    String name;

    public IngredientRedisData(String id, String name) {
        super(id);
        this.name = name;
    }
}
