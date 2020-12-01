package software.bigbade.skriptbot.api;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.util.Optional;

public interface IDataFetcher {
    JsonArray getDocsResults(String query);

    Optional<String> getAddon(String name);

    <T extends Jsonable> Optional<T> readData(String url);
}
