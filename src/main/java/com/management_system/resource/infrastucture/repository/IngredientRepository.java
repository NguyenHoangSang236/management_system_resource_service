package com.management_system.resource.infrastucture.repository;

import com.management_system.resource.entities.database.ingredient.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    @Query("{'_id': ?0}")
    Ingredient getIngredientById(String id);
}
