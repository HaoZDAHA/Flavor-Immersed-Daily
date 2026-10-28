package com.fidtest.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public enum DishModelLoader implements IGeometryLoader<DishModelGeometry> {
    INSTANCE;

    @Override
    public DishModelGeometry read(JsonObject jsonObject, JsonDeserializationContext ctx) throws JsonParseException {
        return new DishModelGeometry();
    }
}
