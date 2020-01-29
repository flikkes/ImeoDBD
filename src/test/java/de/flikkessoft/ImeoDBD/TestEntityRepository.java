package de.flikkessoft.ImeoDBD;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends CrudRepository<TestEntity, Long> {
}
