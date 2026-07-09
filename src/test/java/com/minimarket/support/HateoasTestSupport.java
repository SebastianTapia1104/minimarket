package com.minimarket.support;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;

public final class HateoasTestSupport {

    private HateoasTestSupport() {
    }

    public static JsonNode extractCollection(JsonNode root, String collectionName) {
        if (root.isArray()) {
            return root;
        }
        JsonNode embedded = root.get("_embedded");
        if (embedded != null && embedded.has(collectionName)) {
            return embedded.get(collectionName);
        }
        throw new IllegalStateException("Coleccion HATEOAS no encontrada: " + collectionName);
    }

    public static <T> CollectionModel<EntityModel<T>> toCollectionModel(Iterable<T> entities) {
        List<EntityModel<T>> models = new ArrayList<>();
        entities.forEach(entity -> models.add(EntityModel.of(entity)));
        return CollectionModel.of(models);
    }
}
