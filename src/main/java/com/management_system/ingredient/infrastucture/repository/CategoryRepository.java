package com.management_system.ingredient.infrastucture.repository;

import com.management_system.ingredient.entities.database.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}
