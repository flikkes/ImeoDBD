package de.flikkessoft.ImeoDBD;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ImeoDbdApplication.class)
class EntityConverterTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TestEntityRepository testEntityRepository;

    @Test
    void getMongoDBEntities() {
    }

    @Test
    void saveMongoDBEntity() {
    }

    @Test
    void createSQLQuery() throws IllegalAccessException {
        final List<Object> entities = new ArrayList<>();
        entities.add(new TestEntity(null, 3, "Entity 1", 1.9f));
        entities.add(new TestEntity(null, 6, "Entity 2", 8.6f));
        entities.add(new TestEntity(null, 4, "Entity 3", 4.3f));
        final EntityConverter converter = new EntityConverter(entities);
        assertEquals(
                "CREATE TABLE IF NOT EXISTS TestEntity(id INT PRIMARY KEY AUTO_INCREMENT, someFloat FLOAT, name VARCHAR(250), age INT);\n" +
                        "INSERT INTO TestEntity VALUES (1, 1.9, 'Entity 1', 3);\n" +
                        "INSERT INTO TestEntity VALUES (2, 8.6, 'Entity 2', 6);\n" +
                        "INSERT INTO TestEntity VALUES (3, 4.3, 'Entity 3', 4);\n", converter.createSQLQuery());
    }

    @Test
    void saveLoadSaveMySQLtoMongo() throws IllegalAccessException {
        final List<Document> documents = new ArrayList<>();
        documents.add(new Document().append("age", 5).append("name", "MyDoc 1").append("someFloat", 4.3f));
        documents.add(new Document().append("age", 8).append("name", "MyDoc 2").append("someFloat", 4.7f));
        documents.add(new Document().append("age", 13).append("name", "MyDoc 3").append("someFloat", 4.2f));

        final EntityConverter converter = new EntityConverter(documents, "TestEntity");
        converter.saveSQLEntities(this.jdbcTemplate);

        final List<TestEntity> testEntities = StreamSupport.stream(testEntityRepository.findAll().spliterator(), false)
                .collect(
                        Collectors.toList());


        final EntityConverter newSaveConverter = new EntityConverter(new ArrayList<Object>(testEntities));
        newSaveConverter.saveMongoDBEntities(mongoTemplate);

    }
}