package de.flikkessoft.ImeoDBD;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EntityConverter {
    private final String entityName;
    private final Map<String, Class<?>> fieldNameAndType;
    private final List<Document> entities;

    public EntityConverter(final List<Object> objects) throws IllegalAccessException {
        if (objects.size() < 1) {
            throw new IllegalArgumentException("There must be at least one object to read fields and data from!");
        }
        this.entityName = objects.get(0).getClass().getSimpleName();
        this.fieldNameAndType = new HashMap<>();
        Arrays.stream(objects.get(0).getClass().getDeclaredFields())
                .filter(f -> !Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()))
                .forEach(f -> this.fieldNameAndType.put(f.getName(), f.getType()));
        this.entities = new ArrayList<>();
        for (final Object obj : objects) {
            final Document document = new Document();
            for (final Field f : obj.getClass().getDeclaredFields()) {
                final int modifiers = f.getModifiers();
                if (!Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                    f.setAccessible(true);
                    document.put(f.getName(), f.get(obj));
                    f.setAccessible(false);
                }
            }
            this.entities.add(document);
        }
    }

    private String getLegalTypeForMySQL(final Class<?> type) {

        if (type.equals(short.class) || type.equals(Short.class)) {
            return "SMALLINT";
        }
        if (type.equals(int.class) || type.equals(Integer.class)) {
            return "INT";
        }
        if (type.equals(long.class) || type.equals(Long.class)) {
            return "BIGINT";
        }
        if (type.equals(float.class) || type.equals(Float.class)) {
            return "FLOAT";
        }
        if (type.equals(double.class) || type.equals(Double.class)) {
            return "DOUBLE";
        }
        if (type.equals(LocalDate.class)) {
            return "DATE";
        }
        if (type.equals(Date.class) || type.equals(LocalDateTime.class)) {
            return "DATETIME";
        }
        return "VARCHAR(250)";
    }

    public List<Document> getMongoDBEntities() {
        return this.entities;
    }

    public void saveMongoDBEntity(final Document object, final MongoTemplate template) {
        template.insert(object, this.entityName);
    }

    public String createSQLQuery() {
        String query = "";
        query += "CREATE TABLE IF NOT EXISTS " + this.entityName + "(id INT NOT NULL AUTO_INCREMENT, ";
        final Iterator<String> iterator = this.fieldNameAndType.keySet().iterator();
        while (iterator.hasNext()) {
            final String fieldName = iterator.next();
            query += fieldName + " " + getLegalTypeForMySQL(this.fieldNameAndType.get(fieldName)) + (iterator
                    .hasNext() ? ", " : "");
        }
        query += ");\n";
        for (final Document document : this.entities) {
            query += "INSERT INTO " + this.entityName + " VALUES " + "(NULL, ";
            final Iterator<String> documentIterator = this.fieldNameAndType.keySet().iterator();
            while (documentIterator.hasNext()) {
                final String fieldName = documentIterator.next();
                query += (this.fieldNameAndType.get(fieldName).equals(String.class) ? "'" + document.get(fieldName)
                        .toString() + "'" : document.get(fieldName).toString()) + "" + (documentIterator
                        .hasNext() ? ", " : "");
            }
            query += ");\n";
        }
        return query;
    }

}
