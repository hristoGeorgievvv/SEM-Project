package nl.tudelft.sem.sem54.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReddisIntergationTest {
    private static HttpClient httpClient;

    private static final String mainUri = "http://localhost:8080";
    private static final String authUri = "http://localhost:8081";
    private static final String fridgeUri = "http://localhost:8082";

    private static String token1;
    private static String token2;

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        httpClient = HttpClient.newHttpClient();

        HttpRequest makeAccount1 = HttpRequest
            .newBuilder()
            .uri(URI.create(authUri + "/auth/user"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\n"
                + "\t\"username\": \"user1\",\n"
                + "\t\"password\": \"verysecurepassword\"\n"
                + "}"))
            .build();

        httpClient.send(makeAccount1, HttpResponse.BodyHandlers.ofString()).body();

        HttpRequest login1 = HttpRequest
            .newBuilder()
            .uri(URI.create(mainUri + "/main/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\n"
                + "\t\"username\": \"user1\",\n"
                + "\t\"password\": \"verysecurepassword\"\n"
                + "}"))
            .build();

        token1 = httpClient.send(login1, HttpResponse.BodyHandlers.ofString()).body();

        HttpRequest makeAccount2 = HttpRequest
            .newBuilder()
            .uri(URI.create(authUri + "/auth/user"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\n"
                + "\t\"username\": \"user2\",\n"
                + "\t\"password\": \"verysecurepassword\"\n"
                + "}"))
            .build();

        httpClient.send(makeAccount2, HttpResponse.BodyHandlers.ofString()).body();

        HttpRequest login2 = HttpRequest
            .newBuilder()
            .uri(URI.create(mainUri + "/main/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\n"
                + "\t\"username\": \"user2\",\n"
                + "\t\"password\": \"verysecurepassword\"\n"
                + "}"))
            .build();

        token2 = httpClient.send(login2, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(token2);

    }

    @Test
    public void testFridgeConnection() throws IOException, InterruptedException {
        HttpRequest addProduct1 = HttpRequest
            .newBuilder()
            .uri(URI.create(fridgeUri + "/fridge/products"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token1)
            .POST(HttpRequest.BodyPublishers.ofString("{\n"
                + "    \"productName\" : \"testProduct1\",\n"
                + "    \"portions\" : 1,\n"
                + "    \"creditValue\" : 31,\n"
                + "    \"expirationDate\" : \"2021-05-11\"\n"
                + "}"))
            .build();

        HttpResponse<String> addProduct1Response =
            httpClient.send(addProduct1, HttpResponse.BodyHandlers.ofString());
        int id1 = Integer.parseInt(Objects.requireNonNull(findId(addProduct1Response.body())));

        HttpRequest addProduct2 = HttpRequest
            .newBuilder()
            .uri(URI.create(fridgeUri + "/fridge/products"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token2)
            .POST(HttpRequest.BodyPublishers.ofString("{\n"
                + "    \"productName\" : \"testProduct2\",\n"
                + "    \"portions\" : 1,\n"
                + "    \"creditValue\" : 31,\n"
                + "    \"expirationDate\" : \"2021-05-11\"\n"
                + "}"))
            .build();

        HttpResponse<String> addProduct2Response =
            httpClient.send(addProduct2, HttpResponse.BodyHandlers.ofString());
        System.out.println(addProduct2Response);
        int id2 = Integer.parseInt(Objects.requireNonNull(findId(addProduct2Response.body())));

        HttpRequest takePortion = HttpRequest
            .newBuilder()
            .uri(URI.create(fridgeUri + "/fridge/take"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token1)
            .POST(HttpRequest.BodyPublishers.ofString("{\n"
                + "    \"username\": \"user1\",\n"
                + "    \"productId\": " + id2 + ",\n"
                + "    \"amount\": 1\n"
                + "}"))
            .build();

        HttpResponse<String> takePortionResponse =
            httpClient.send(takePortion, HttpResponse.BodyHandlers.ofString());

        Thread.sleep(1000);

        HttpRequest credits1 = HttpRequest
            .newBuilder()
            .uri(URI.create(mainUri + "/main/credits/me"))
            .header("Authorization", "Bearer " + token1)
            .GET()
            .build();

        HttpResponse<String> credits1Response =
            httpClient.send(credits1, HttpResponse.BodyHandlers.ofString());
        String result1 = credits1Response.body();

        HttpRequest credits2 = HttpRequest
            .newBuilder()
            .uri(URI.create(mainUri + "/main/credits/me"))
            .header("Authorization", "Bearer " + token2)
            .GET()
            .build();

        HttpResponse<String> credits2Response =
            httpClient.send(credits2, HttpResponse.BodyHandlers.ofString());
        String result2 = credits2Response.body();

        assertThat(result1).isEqualTo("-31.0");
        assertThat(result2).isEqualTo("31.0");
    }

    @AfterAll
    static void cleanup() throws IOException, InterruptedException {
        httpClient = HttpClient.newHttpClient();

        HttpRequest reset = HttpRequest
            .newBuilder()
            .uri(URI.create(mainUri + "/main/reset"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token1)
            .POST(HttpRequest.BodyPublishers.ofString(""))
            .build();

        System.out.println(httpClient.send(reset, HttpResponse.BodyHandlers.ofString()));
    }

    private String findId(String s) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        return m.find() ? m.group() : null;
    }
}
