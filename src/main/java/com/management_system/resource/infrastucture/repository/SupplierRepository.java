package com.management_system.resource.infrastucture.repository;

import com.management_system.resource.entities.database.supplier.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<Supplier, String> {
}
