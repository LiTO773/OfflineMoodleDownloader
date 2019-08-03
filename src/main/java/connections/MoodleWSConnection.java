package connections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import models.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class MoodleWSConnection {

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Validates the URL and transforms it into a https:// url
     * @param url Desired Moodle URL
     * @return Correctly formatted URL
     */
    public static String httpsURL(String url) throws CustomException {
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

    public JsonObject getUserID(String url, String token) throws IOException, CustomException {
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
//            int userid = Integer.parseInt(responseJSON.get("userid").toString());
            return responseJSON;
        }
    }

    public Set<Course> getCourses() throws IOException, CustomException {
        Moodle currentMoodle = CurrentMoodle.getMoodle();
        String moodleURL = currentMoodle.getUrl() + "/webservice/rest/server.php";

        // Send a request for all the available courses
        RequestBody body = new FormEncodingBuilder()
                .add("wstoken", currentMoodle.getToken())
                .add("wsfunction", "core_enrol_get_users_courses")
                .add("userid", Integer.toString(currentMoodle.getUserid()))
                .add("moodlewsrestformat", "json")
                .build();

        // Read the response
        String response = sendPOSTRequest(moodleURL, body);
        System.out.println(response);
        JsonArray responseJSON = new JsonParser().parse(response).getAsJsonArray();

        // Create a Set of Courses with all the information
        HashSet<Course> courses = new HashSet<>();
        for (JsonElement jo: responseJSON) {
            int id = ((JsonObject) jo).get("id").getAsInt();
            String shortname = ((JsonObject) jo).get("shortname").getAsString();

            Course c = new Course(id, shortname, true);
            courses.add(c);
        }
        return courses;
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
