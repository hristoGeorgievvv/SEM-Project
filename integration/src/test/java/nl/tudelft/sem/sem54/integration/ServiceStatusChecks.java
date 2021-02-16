package nl.tudelft.sem.sem54.integration;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ServiceStatusChecks {
    private static HttpClient httpClient;

    private static final String fridgeUri = "http://localhost:8082";

    @BeforeAll
    static void setup() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    public void testFridgeConnection() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(fridgeUri + "/actuator/health"))
                .header("accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals("{\"status\":\"UP\"}", response.body());
    }
}
