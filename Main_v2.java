// Gson is Google's JSON package, therefore it must be in the pom file
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

// Main_v1: the API response is returned as a string.
// Main_v2: the GSON package is used to Pretty print the API response.
// Main_v3: the GSON package is used to parse through the API response.
// Main_v4: a Fruit class is utilized
public class Main_v2 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter a fruit:");
        String fruitName = scan.nextLine();

        // method that uses the API, it returns a JSON object
        // we aren't doing anything with the fruitInfo in this example.
        JsonObject fruitInfo = getFruitInfo(fruitName);

        scan.close();
    }

    public static JsonObject getFruitInfo(String fruitName) {
        // Constructing the URL for the API request
        String url = "https://www.fruityvice.com/api/fruit/" + fruitName;

        // Create an HTTP client object
        HttpClient client = HttpClient.newHttpClient();

        // Build an HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Send the request to the API, and get a response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // A 200 means success!
            if (response.statusCode() == 200) {
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                // to print out the response in a legible way
                // in a production environment, you would want to use the condensed version, as it takes up less space.
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonOutput = gson.toJson(jsonResponse);

                System.out.println((jsonOutput));

                // returning it, but we aren't doing anything with it in this example.
                return jsonResponse;
            } else {
                System.out.println("Failed to retrieve data. HTTP status: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}