package suppliers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.CurrentMoodle;
import models.TokenID;

public class MoodleLoginSupplier implements LoginSupplier {
    @Override
    public JsonObject login(String url, String username, String password) throws Exception {
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

        // Add the userid to the response
        JsonObject useridJson = moodleWS.getUserID(url, token);
        jsonResponse.add("userid", useridJson.get("userid"));

        // Return the response + userid
        return jsonResponse;
    }
}