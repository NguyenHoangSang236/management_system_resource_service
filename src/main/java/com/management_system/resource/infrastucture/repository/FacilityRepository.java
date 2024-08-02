package com.management_system.resource.infrastucture.repository;

import com.management_system.resource.entities.database.facility.Facility;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends MongoRepository<Facility, String> {
}
