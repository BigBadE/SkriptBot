package software.bigbade.skriptbot.utils;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.mockito.Mockito.mockStatic;

class ResourceDataFetcherTest {
    private static final MockedStatic<ResourceDataFetcher> mockedStatic = mockStatic(ResourceDataFetcher.class);
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
                "  }" +
                "}");
        mockedStatic.when(() -> ResourceDataFetcher
                .readData("https://api.skripttools.net/v4/addons"))
                .thenReturn(Optional.of(topData));
        dataFetcher = new ResourceDataFetcher("key");
        mockedStatic.reset();
    }


    private static void setStaticMockOutput(JsonObject output) {
        mockedStatic.when(() -> ResourceDataFetcher
                .readData("https://docs.skunity.com/api/?key=key&function=doSearch&query=test+addon"))
                .thenReturn(Optional.ofNullable(output));
    }

    @AfterAll
    static void closeStaticMocks() {
        mockedStatic.close();
    }

    @Test
    void testUnsuccessfulConnection() {
        JsonObject topData = new JsonObject();
        topData.put("success", false);
        mockedStatic.when(() -> ResourceDataFetcher
                .readData("https://api.skripttools.net/v4/addons")).thenReturn(Optional.of(topData));
        Assertions.assertThrows(IllegalStateException.class, () -> new ResourceDataFetcher("key"));
    }

    @Test
    void testAddonGetter() {
        Optional<String> found = dataFetcher.getAddon("testing");
        Assertions.assertEquals("testValue", found.orElse("No Value"));
        found = dataFetcher.getAddon("test");
        Assertions.assertEquals("testValue", found.orElse("No Value"));
        found = dataFetcher.getAddon("nope");
        Assertions.assertFalse(found.isPresent());
    }

    @Test
    void testDocsGetterFails() {
        setStaticMockOutput(null);
        Assertions.assertTrue(dataFetcher.getDocsResults("test addon").isEmpty());
    }

    @SneakyThrows
    @Test
    void testEmptyAddonResults() {
        JsonObject docsResult = (JsonObject) Jsoner.deserialize("{ \"response\": \"fail\" }");
        setStaticMockOutput(docsResult);
        Assertions.assertTrue(dataFetcher.getDocsResults("test addon").isEmpty());
    }

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
