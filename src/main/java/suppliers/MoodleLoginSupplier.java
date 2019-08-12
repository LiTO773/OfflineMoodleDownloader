package suppliers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class MoodleLoginSupplier implements LoginSupplier {
    @Override
    public Map<String, String> login(String url, String username, String password) throws Exception {
        Map<String, String> result = new HashMap<>();
        MoodleWSConnection moodleWS = new MoodleWSConnection();

        // Gets the user's token (as a response)
        String tokenResponse = moodleWS.getTokenResponse(url, username, password);

        // Check if the response contains the user's token:
        JsonObject jsonResponse = new JsonParser().parse(tokenResponse).getAsJsonObject();
        if (jsonResponse.has("errorcode")) {
            // An error happened, throw that error back
            String error = jsonResponse.get("errorcode").toString();
            throw new Exception(error);
        }

        // Everything went correctly
        String token = jsonResponse.get("token").getAsString();
        result.put("token", token);

        // Add the userid to the response
        JsonObject infoJson = moodleWS.getMoodleInfo(url, token);
        result.put("userid", infoJson.get("userid").getAsString());
        result.put("sitename", infoJson.get("sitename").getAsString());

        // Return the response + userid
        return result;
    }
}