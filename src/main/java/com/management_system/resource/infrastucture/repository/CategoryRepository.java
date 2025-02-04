package com.management_system.resource.infrastucture.repository;

import com.management_system.resource.entities.database.category.Category;
import com.management_system.utilities.constant.enumuration.TableName;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ '_id': 1 }")
    List<Category> findIdsByIdIn(List<String> ids);

    @Query("{ 'sub_categories._id': ?0 }")
    List<Category> findBySubCategoryId(String subCategoryId);

    List<Category> findByType(TableName tableName);
}
