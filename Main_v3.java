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
public class Main_v3 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter a fruit:");
        String fruitName = scan.nextLine();

        // method that uses the API, it returns a JSON object
        JsonObject fruitInfo = getFruitInfo(fruitName);

        if (fruitInfo != null){
            // parse through the API response using the JSON package
            String family = fruitInfo.get("family").getAsString();

            // We want to look at the nutritions specifically
            JsonObject nutritions = fruitInfo.getAsJsonObject("nutritions");
            double calories = nutritions.get("calories").getAsDouble();
            double carbs = nutritions.get("carbohydrates").getAsDouble();
            double protein = nutritions.get("protein").getAsDouble();
            double fat = nutritions.get("fat").getAsDouble();
            double sugar = nutritions.get("sugar").getAsDouble();

            System.out.printf("%s is in the %s family:\n" +
                            "Calories: %.2f, Carbohydrates: %.2f, Protein: %.2f, Fat: %.2f, Sugar: %.2f%n",
                    fruitName, family, calories, carbs, protein, fat, sugar);
        }
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

            // Check that response.statusCode() returns a 200
            if (response.statusCode() == 200) {
                // here we create a JsonObject, which we'll parse through in the main()
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

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