package nl.tudelft.sem.sem54.mainservice.service;

import org.springframework.http.HttpStatus;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class HttpCommunicationService {

    private static final HttpClient mainMicroservice = HttpClient.newBuilder().build();

    @Value("${project.auth.endpoint}")
    private transient String prefix;

    /**
     * Creates and sends a get request to another microservice.
     *
     * @param endpoint the REST API mapping to the other microservice
     * @param port     the port the other microservice is running on
     * @param token    the JWT token of the user making the request
     * @return an HttpResponse in string form containing the response
     */
    public HttpResponse<String> getUtilityAuthorized(
            String endpoint, String port, String token) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(prefix + port + "/" + endpoint))
                .setHeader("Authorization", "Bearer " + token).build();
        HttpResponse<String> response;
        try {
            response = mainMicroservice.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (response.statusCode() != HttpStatus.OK.value()) {
            System.out.println("Status: " + response.statusCode());
        }
        return response;
    }

    /**
     * Creates and sends an authorized post request to another microservice.
     *
     * @param endpoint The endpoint of the other microservice
     * @param port     The port the other microservice is listing on
     * @param token    The authentication token of the user making the request.
     *                 Null if the request should not be authorized
     * @param body     The body of the post request
     * @return the HttpResponse as a string
     */
    public HttpResponse<String> postUtility(
            String endpoint, String port, String token, String body) {
        HttpRequest request;
        if (token == null) {
            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(prefix + port + "/" + endpoint)).build();
        } else {
            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(prefix + port + "/" + endpoint))
                    .setHeader("Authorization", "Bearer " + token).build();
        }
        HttpResponse<String> response;
        try {
            response = mainMicroservice.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    /**
     * Creates and sends a delete request to another microservice.
     *
     * @param endpoint The endpoint of the other microservice
     * @param port     The port the other microservice is listing on
     * @param token    The authentication token of the user making the request.
     *                 Null if the request should not be authorized
     * @return the HttpResponse as a string
     */
    public HttpResponse<String> deleteUtility(
            String endpoint, String port, String token) {
        HttpRequest request;

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(prefix + port + "/" + endpoint))
                .setHeader("Authorization", "Bearer " + token).build();
        HttpResponse<String> response;
        try {
            response = mainMicroservice.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }
}
