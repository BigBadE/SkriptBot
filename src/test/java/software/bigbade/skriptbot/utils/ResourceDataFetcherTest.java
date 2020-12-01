package software.bigbade.skriptbot.utils;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import software.bigbade.skriptbot.URLConnectionHandler;

import javax.annotation.Nullable;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResourceDataFetcherTest {
    private static ResourceDataFetcher dataFetcher;

    private static final String TEST_JSON = "{\"id\":1,\"completed\":false,\"title\":\"delectus aut autem\",\"userId\":1}";

    @SneakyThrows
    @BeforeAll
    static void setupDataFetcher() {
        JsonObject topData = (JsonObject) Jsoner.deserialize("{" +
                "  \"success\": true," +
                "  \"data\": {" +
                "    \"testing\": [" +
                "      \"testValue\"" +
                "    ]," +
                "    \"invalid\": null" +
                "  }" +
                "}");
        URLConnectionHandler.resetValues(topData.toJson(), null, null);
        dataFetcher = new ResourceDataFetcher("key");
    }

    private static void setWebsiteInput(@Nullable JsonObject output) {
        URLConnectionHandler.resetValues(output == null ? "" : output.toJson(),
                "key=key&function=doSearch&query=test+addon", null);
    }

    @Order(7)
    @Test
    void testGetJsonFromPage() {
        URLConnectionHandler.resetValues(TEST_JSON, null, null);
        Optional<Jsonable> optional = dataFetcher.readData("testing:test.file");
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(TEST_JSON,
                optional.get().toJson());
        URLConnectionHandler.resetValues("not json", null, null);
        Assertions.assertFalse(dataFetcher.readData("https://www.google.com").isPresent());

    }

    @Order(1)
    @Test
    void testUnsuccessfulConnection() {
        JsonObject topData = new JsonObject();
        topData.put("success", false);
        URLConnectionHandler.resetValues(topData.toJson(), null, null);
        Assertions.assertThrows(IllegalStateException.class, () -> new ResourceDataFetcher("key"));
    }

    @Order(2)
    @Test
    void testAddonGetter() {
        Optional<String> found = dataFetcher.getAddon("testing");
        Assertions.assertEquals("testValue", found.orElse("No Value"));
        found = dataFetcher.getAddon("test");
        Assertions.assertEquals("testValue", found.orElse("No Value"));
        found = dataFetcher.getAddon("nope");
        Assertions.assertFalse(found.isPresent());
    }

    @Order(3)
    @Test
    void testDocsGetterFails() {
        setWebsiteInput(null);
        Assertions.assertTrue(dataFetcher.getDocsResults("test addon").isEmpty());
    }

    @SneakyThrows
    @Order(6)
    @Test
    void testEmptyAddonResults() {
        JsonObject docsResult = (JsonObject) Jsoner.deserialize("{ \"response\": \"fail\" }");
        setWebsiteInput(docsResult);
        Assertions.assertTrue(dataFetcher.getDocsResults("test addon").isEmpty());
    }

    @Order(4)
    @SneakyThrows
    @Test
    void testZeroResultsReturnsEmptyArray() {
        //Test JSON output
        JsonObject docsResult = (JsonObject) Jsoner.deserialize(
                "{" +
                        "  \"response\": \"success\"," +
                        "  \"result\": {" +
                        "    \"info\": {" +
                        "      \"returned\": 0" +
                        "    }" +
                        "  }" +
                        "}");
        setWebsiteInput(docsResult);
        Assertions.assertTrue(dataFetcher.getDocsResults("test addon").isEmpty());
    }

    @Order(5)
    @SneakyThrows
    @Test
    void testGetsDocsResults() {
        //Test JSON output
        JsonObject docsResult = (JsonObject) Jsoner.deserialize(
                "{" +
                        "  \"response\": \"success\"," +
                        "  \"result\": {" +
                        "    \"info\": {" +
                        "      \"returned\": 1" +
                        "    }," +
                        "    \"records\": [" +
                        "      {" +
                        "        \"name\": \"test addon\"" +
                        "      }" +
                        "    ]" +
                        "  }" +
                        "}");
        setWebsiteInput(docsResult);
        Assertions.assertEquals(1, dataFetcher.getDocsResults("test addon").size());
    }
}
