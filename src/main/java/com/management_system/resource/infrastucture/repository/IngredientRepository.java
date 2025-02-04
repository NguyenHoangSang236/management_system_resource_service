package com.management_system.resource.infrastucture.repository;

import com.management_system.resource.entities.database.ingredient.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    @Query("{'name': {$regex: '^?0$', $options: 'i'}}")
    Optional<Ingredient> getIngredientByName(String name);


}
