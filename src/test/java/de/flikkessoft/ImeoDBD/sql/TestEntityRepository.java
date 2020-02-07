package de.flikkessoft.ImeoDBD.sql;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends CrudRepository<TestEntity, Long> {
}
