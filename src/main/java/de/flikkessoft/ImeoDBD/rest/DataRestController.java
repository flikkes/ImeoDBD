package de.flikkessoft.ImeoDBD.rest;

import de.flikkessoft.ImeoDBD.EntityConverter;
import de.flikkessoft.ImeoDBD.migration.Database;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("data")
public class DataRestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/{database}/{collection}")
    public ResponseEntity<?> writeData(@RequestBody final List<Document> objects, @PathVariable final Database database,
                                       @PathVariable final String collection)
            throws URISyntaxException, IllegalAccessException {
        final EntityConverter converter = new EntityConverter(objects, collection);
        for (final Document obj : converter.getEntities()) {
            System.out.println(obj);
        }
        System.out.println(converter.createSQLQuery());
        switch (database) {
            case MYSQL:
                converter.executeSQLQuery(converter.createSQLQuery(), jdbcTemplate);
                break;
            case MONGODB:
                converter.saveMongoDBEntity(converter.getMongoDBEntities(), mongoTemplate);
            default:
                break;
        }
        return ResponseEntity.created(
                new URI(ServletUriComponentsBuilder.fromCurrentRequest().toUriString() + "/" + converter
                        .getEntityName())).build();
    }

    @GetMapping("/{database}/{collection}")
    public ResponseEntity<?> readData(@PathVariable final Database database, @PathVariable final String collection) {
        switch (database) {
            case MYSQL:
                return ResponseEntity
                        .ok(jdbcTemplate.query("SELECT * FROM " + collection, new ColumnMapRowMapper()));
            case MONGODB:
                return ResponseEntity.ok(mongoTemplate.findAll(Document.class, collection));
            default:
                break;
        }
        return ResponseEntity.ok().build();
    }

}
