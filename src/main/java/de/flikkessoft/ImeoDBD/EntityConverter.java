package de.flikkessoft.ImeoDBD;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class EntityConverter {
    private final String entityName;
    private final Map<String, Object> entityData;

    public EntityConverter(final Object obj) throws IllegalAccessException {
        this.entityName = obj.getClass().getSimpleName();
        this.entityData = new HashMap<>();
        for (final Field f : obj.getClass().getDeclaredFields()) {
            final int modifiers = f.getModifiers();
            if (!Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                this.entityData.put(f.getName(), f.get(obj));
            }
        }
    }

    public Document getMongoDBEntity() {
        final Document object = new Document();
        for (final String s : this.entityData.keySet()) {
            object.put(s, this.entityData.get(s));
        }
        return object;
    }

    public void saveMongoDBEntity(final Document object, final MongoTemplate template) {
        template.insert(object, this.entityName);
    }
}
