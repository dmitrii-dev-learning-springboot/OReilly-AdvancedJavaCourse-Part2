package org.example.networking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Geocoder {

    private static final String BASE = "https://nominatim.openstreetmap.org/search";

    // Encode address list into a single string
    public String encodeAddress(List<String> address) {
        return address.stream()
                .map(s -> URLEncoder.encode(s, StandardCharsets.UTF_8))
                .collect(Collectors.joining(","));
    }

    // Perform HTTP GET request to OSM Nominatim API
    public String getData(List<String> address) {
        String encoded = encodeAddress(address);

        String url = String.format("%s?q=%s&format=json&limit=1", BASE, encoded);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "JavaGeocoderApp/1.0 (someemail@example.com)") // Required by OSM
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to call OSM API", e);
        }
    }

    public double[] extractLatLon(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arr = mapper.readTree(json);
        if (arr.isArray() && arr.size() > 0) {
            JsonNode first = arr.get(0);
            double lat = first.get("lat").asDouble();
            double lon = first.get("lon").asDouble();
            return new double[]{lat, lon};
        }
        throw new IllegalArgumentException("No results found");
    }
}
