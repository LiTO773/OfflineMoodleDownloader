package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import models.CustomException;
import models.Errors;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MoodleWSController {

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Validates the URL and transforms it into a https:// url
     * @param url Desired Moodle URL
     * @return Correctly formatted URL
     */
    public String httpsURL(String url) throws CustomException {
        // Add protocol to the URL and remove the end '/'
        String properURL = url;
        if (!url.contains("http://") && !url.contains("https://")) {
            properURL = "https://" + url;
        }
        if (properURL.charAt(properURL.length() - 1) == '/') {
            properURL = properURL.substring(0, properURL.length() - 1);
        }

        // Check if the URL is correct by itself
        URL obj = null;
        try {
            obj = new URL(properURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new CustomException(Errors.INVALID_URL);
        }

        return properURL;
    }

    public String getTokenResponse(String url, String username, String password) throws CustomException, IOException {
        String moodleURL = url + "/login/token.php";
        // Create the body
        RequestBody body = new FormEncodingBuilder()
            .add("service", "moodle_mobile_app")
            .add("username", username)
            .add("password", password)
            .build();

        return sendPOSTRequest(moodleURL, body);
    }

    public int getUserID(String url, String token) throws IOException, CustomException {
        String moodleURL = url + "/webservice/rest/server.php";

        RequestBody body = new FormEncodingBuilder()
            .add("wstoken", token)
            .add("wsfunction", "core_webservice_get_site_info")
            .add("moodlewsrestformat", "json")
            .build();

        String response = sendPOSTRequest(moodleURL, body);
        System.out.println(response);
        JsonObject responseJSON = new JsonParser().parse(response).getAsJsonObject();

        if (!responseJSON.has("userid")) {
            throw new CustomException(Errors.USERID_ERROR);
        } else {
            int userid = Integer.parseInt(responseJSON.get("userid").toString());
            return userid;
        }
    }

    private String sendPOSTRequest(String url, RequestBody body) throws IOException, CustomException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new CustomException(Errors.INVALID_URL);
        }
        if (!response.isSuccessful()) throw new CustomException(Errors.INVALID_URL);

        return response.body().string();
    }
}
