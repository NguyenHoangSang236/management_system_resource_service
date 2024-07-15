package com.management_system.ingredient.infrastucture.repository;

import com.management_system.ingredient.entities.database.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    @Query("{'name': ?0}")
    Ingredient getIngredientByName(String name);

    @Query("{'_id': ?0}")
    Ingredient getIngredientById(String id);
}
