package nl.tudelft.sem.sem54.integration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

public class RedisMessageIntegrationTest {
    private static HttpClient httpClient;

    private static final String mainUri = "http://localhost:8080";
    private static final String authUri = "http://localhost:8081";
    private static final String fridgeUri = "http://localhost:8082";
    private static Jedis jedis;

    private static String token;

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        httpClient = HttpClient.newHttpClient();

        HttpRequest makeAccount = HttpRequest
                .newBuilder()
                .uri(URI.create(authUri + "/auth/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\n"
                        + "\t\"username\": \"user1\",\n"
                        + "\t\"password\": \"verysecurepassword\"\n"
                        + "}"))
                .build();

        HttpRequest login = HttpRequest
                .newBuilder()
                .uri(URI.create(mainUri + "/main/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\n"
                        + "\t\"username\": \"user1\",\n"
                        + "\t\"password\": \"verysecurepassword\"\n"
                        + "}"))
                .build();

        httpClient.send(makeAccount, HttpResponse.BodyHandlers.ofString()).body();

        token = httpClient.send(login, HttpResponse.BodyHandlers.ofString()).body();
    }

    @Test
    public void removeItemRedisTest() throws IOException, InterruptedException {

        String productName = "removeItemRedisTest MILK1";

        // create product

        HttpRequest makeProduct = HttpRequest
                .newBuilder()
                .uri(URI.create(fridgeUri + "/fridge/products"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString("{ \"productName\" : \"" + productName + "\", \"portions\" : 10, \"creditValue\" : 10, \"expirationDate\" : \"2021-10-12\" }"))
                .build();

        HttpResponse<String> response = httpClient.send(makeProduct, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = object.get("id").getAsString();
        String name = object.get("productName").getAsString();
        Assertions.assertEquals(productName, name);


        HttpRequest checkCredits = HttpRequest
                .newBuilder()
                .uri(URI.create(mainUri + "/main/credits/me"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token).GET().build();

        HttpResponse<String> checkResponse = httpClient.send(checkCredits, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals("0.0", checkResponse.body());
        // remove product
        HttpRequest removeProduct = HttpRequest
                .newBuilder()
                .uri(URI.create(fridgeUri + "/fridge/products"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(id)).build();

        HttpResponse<String> removeResponse = httpClient.send(removeProduct, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, removeResponse.statusCode());


        HttpRequest checkCreditsAfter = HttpRequest
                .newBuilder()
                .uri(URI.create(mainUri + "/main/credits/me"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token).GET().build();

        HttpResponse<String> checkResponseAfter = httpClient.send(checkCreditsAfter, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("0.0", checkResponseAfter.body());
    }
}