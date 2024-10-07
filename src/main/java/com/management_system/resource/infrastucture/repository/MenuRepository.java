package com.management_system.resource.infrastucture.repository;

import com.management_system.resource.entities.database.menu.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {
    @Query("{'name': { $regex: ?0, $options: 'i' }}")
    Optional<List<Menu>> getMenuProductsByName(String name);
}
