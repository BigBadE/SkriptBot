package software.bigbade.skriptbot.utils;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import software.bigbade.skriptbot.SkriptBot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResourceDataFetcher {
    private final Map<String, String> addons = new HashMap<>();
    private final String skUnityKey;

    public ResourceDataFetcher(String skUnityKey) {
        this.skUnityKey = skUnityKey;
        JsonObject result = (JsonObject) readData("https://api.skripttools.net/v4/addons").orElseThrow(() -> new IllegalStateException("SkriptTools addon page is down!"));
        if (!result.getBoolean(JsonKeys.SUCCESS.getKey())) {
            throw new IllegalStateException("SkriptTools addon get not successful");
        }
        JsonObject data = result.getMap(JsonKeys.DATA.getKey());
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            JsonArray array = (JsonArray) entry.getValue();
            addons.put(entry.getKey().toLowerCase(), (String) array.get(array.size() - 1));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Jsonable> Optional<T> readData(String url) {
        try (InputStreamReader reader = new InputStreamReader(getInputStream(url), StandardCharsets.UTF_8)) {
            return Optional.of((T) Jsoner.deserialize(reader));
        } catch (IOException | JsonException e) {
            SkriptBot.getLogger().error("Could not read data from {}", url, e);
            return Optional.empty();
        }
    }

    public static InputStream getInputStream(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");
        return connection.getInputStream();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public JsonArray getDocsResults(String query) {
        Optional<Jsonable> optional = readData("https://docs.skunity.com/api/?key="
                + skUnityKey + "&function=doSearch&query=" + URLEncoder.encode(query, StandardCharsets.UTF_8.name()));
        if (!optional.isPresent()) {
            return new JsonArray();
        }
        JsonObject result = (JsonObject) optional.get();
        if (!result.getString(JsonKeys.RESPONSE.getKey()).equals("success")) {
            return new JsonArray();
        }
        result = (JsonObject) result.get("result");
        int found = ((JsonObject) result.get("info")).getInteger(JsonKeys.RETURNED.getKey());
        if (found == 0) {
            return new JsonArray();
        }
        return (JsonArray) result.get("records");
    }

    public Optional<String> getAddon(String name) {
        for (Map.Entry<String, String> entry : addons.entrySet()) {
            if (entry.getKey().contains(name)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }
}
