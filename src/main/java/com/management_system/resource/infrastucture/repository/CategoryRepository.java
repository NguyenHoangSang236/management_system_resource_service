package com.management_system.resource.infrastucture.repository;

import com.management_system.resource.entities.database.category.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ '_id': 1 }")
    List<Category> findIdsByIdIn(List<String> ids);
}
