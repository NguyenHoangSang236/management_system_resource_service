package com.management_system.resource.infrastucture.repository;

import com.management_system.resource.entities.database.supplier.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends MongoRepository<Supplier, String> {
    @Query("{'name': { $regex: ?0, $options: 'i' }}")
    Optional<List<Supplier>> getSupplierByName(String name);

    @Query("{'name': { $regex: ?0, $options: 'i' }, 'organization': { $regex: ?1, $options: 'i' }}")
    Optional<List<Supplier>> getSupplierByNameAndOrganization(String name, String organization);
}
