package software.bigbade.skriptbot.utils;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.mockStatic;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResourceDataFetcherTest {
    private static final MockedStatic<ResourceDataFetcher> MOCK_DATA_FETCHER = mockStatic(ResourceDataFetcher.class);
    private static ResourceDataFetcher dataFetcher;

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
        MOCK_DATA_FETCHER.when(() -> ResourceDataFetcher
                .readData("https://api.skripttools.net/v4/addons"))
                .thenReturn(Optional.of(topData));
        dataFetcher = new ResourceDataFetcher("key");
        MOCK_DATA_FETCHER.reset();
    }

    private static void setStaticMockOutput(JsonObject output) {
        MOCK_DATA_FETCHER.when(() -> ResourceDataFetcher
                .readData("https://docs.skunity.com/api/?key=key&function=doSearch&query=test+addon"))
                .thenReturn(Optional.ofNullable(output));
    }

    @AfterEach
    void resetStaticMock() {
        if (!MOCK_DATA_FETCHER.isClosed()) {
            MOCK_DATA_FETCHER.reset();
        }
    }

    @Order(7)
    @Test
    void testGetInputStream() {
        MOCK_DATA_FETCHER.close();
        try {
            Assertions.assertNotNull(ResourceDataFetcher.getInputStream("https://www.google.com"));
        } catch (IOException e) {
            System.out.println("No internet!");
        }
    }

    @Order(8)
    @Test
    void testGetJsonFromPage() {
        Optional<Jsonable> optional = ResourceDataFetcher.readData("https://jsonplaceholder.typicode.com/todos/1");
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals("{\"id\":1,\"completed\":false,\"title\":\"delectus aut autem\",\"userId\":1}",
                optional.get().toJson());
        Assertions.assertFalse(ResourceDataFetcher.readData("https://www.google.com").isPresent());

    }

    @Order(1)
    @Test
    void testUnsuccessfulConnection() {
        JsonObject topData = new JsonObject();
        topData.put("success", false);
        MOCK_DATA_FETCHER.when(() -> ResourceDataFetcher
                .readData("https://api.skripttools.net/v4/addons")).thenReturn(Optional.of(topData));
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
        setStaticMockOutput(null);
        Assertions.assertTrue(dataFetcher.getDocsResults("test addon").isEmpty());
    }

    @SneakyThrows
    @Order(6)
    @Test
    void testEmptyAddonResults() {
        JsonObject docsResult = (JsonObject) Jsoner.deserialize("{ \"response\": \"fail\" }");
        setStaticMockOutput(docsResult);
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
        setStaticMockOutput(docsResult);
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
        setStaticMockOutput(docsResult);
        Assertions.assertEquals(1, dataFetcher.getDocsResults("test addon").size());
    }
}
