package com.bigbade.skriptbot.testutils;

import com.bigbade.skriptbot.api.IDataFetcher;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.Jsonable;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestResourceDataFetcher implements IDataFetcher {
    private final Map<String, String> addons = new HashMap<>();
    private final Map<String, JsonArray> results = new HashMap<>();

    @Nullable
    @Setter
    private Jsonable data = null;

    public void addResult(String query, JsonArray result) {
        results.put(query, result);
    }

    public void addAddon(String query, String result) {
        addons.put(query, result);
    }

    @Override
    public JsonArray getDocsResults(String query) {
        return results.get(query);
    }

    @Override
    public Optional<String> getAddon(String name) {
        return Optional.ofNullable(addons.get(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Jsonable> Optional<T> readData(String url) {
        T output = (T) data;
        return Optional.ofNullable(output);
    }
}
