package de.flikkessoft.ImeoDBD;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityConverterTest {

    @Test
    void getMongoDBEntities() {
    }

    @Test
    void saveMongoDBEntity() {
    }

    @Test
    void createSQLQuery() throws IllegalAccessException {
        final List<Object> entities = new ArrayList<>();
        entities.add(new TestEntity(3, "Entity 1", 1.9f));
        entities.add(new TestEntity(6, "Entity 2", 8.6f));
        entities.add(new TestEntity(4, "Entity 3", 4.3f));
        final EntityConverter converter = new EntityConverter(entities);
        assertEquals(
                "CREATE TABLE IF NOT EXISTS TestEntity(id INT PRIMARY KEY AUTO_INCREMENT, someFloat FLOAT, name VARCHAR(250), age INT);\n" +
                        "INSERT INTO TestEntity VALUES (NULL, 1.9, 'Entity 1', 3);\n" +
                        "INSERT INTO TestEntity VALUES (NULL, 8.6, 'Entity 2', 6);\n" +
                        "INSERT INTO TestEntity VALUES (NULL, 4.3, 'Entity 3', 4);\n", converter.createSQLQuery());
    }
}