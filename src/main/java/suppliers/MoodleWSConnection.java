package suppliers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import models.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public JsonObject getMoodleInfo(String url, String token) throws IOException, CustomException {
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
            return responseJSON;
        }
    }

    public List<Course> getCourses() throws IOException, CustomException {
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
        ArrayList<Course> courses = new ArrayList<>();
        for (JsonElement jo: responseJSON) {
            int id = ((JsonObject) jo).get("id").getAsInt();
            String shortname = ((JsonObject) jo).get("shortname").getAsString();

            Course c = new Course(id, shortname, false);
            courses.add(c);
        }
        return courses;
    }

    public void fillCourse(Course course) throws Exception {
        boolean DEFAULT_DOWNLOAD = false; // All documents are not downloadable by default
        Moodle currentMoodle = CurrentMoodle.getMoodle();
        String moodleURL = currentMoodle.getUrl() + "/webservice/rest/server.php";

        RequestBody body = new FormEncodingBuilder()
                .add("wstoken", currentMoodle.getToken())
                .add("wsfunction", "core_course_get_contents")
                .add("courseid", Integer.toString(course.getId()))
                .add("moodlewsrestformat", "json")
                .build();

        // Read the response
        String response = sendPOSTRequest(moodleURL, body);
        System.out.println(response);
        JsonArray responseJSON = new JsonParser().parse(response).getAsJsonArray();

        // --> Get the sections
        for (JsonElement sectionJSON: responseJSON) {
            JsonObject section = (JsonObject) sectionJSON;

            // Create a section
            Section s = new Section(
                    section.get("id").getAsInt(),
                    section.get("name").getAsString(),
                    DEFAULT_DOWNLOAD
            );
            // Add the section to CurrentMoodle
            course.addSection(s);


            // --> Get the modules (which contain the content inside)
            JsonArray modules = (JsonArray) section.get("modules");
            for (JsonElement moduleJSON: modules) {
                JsonObject module = (JsonObject) moduleJSON;
                boolean folder = false;
                Folder fo = null;

                // Check if it's a folder
                if (module.get("modname").getAsString().equals("folder")) {
                    // It's a folder
                    folder = true;
                    // Create a folder
                    fo = new Folder(
                            module.get("id").getAsInt(),
                            module.get("name").getAsString(),
                            DEFAULT_DOWNLOAD
                    );
                    s.addContent(fo);
                } else if (!module.get("modname").getAsString().equals("resource")) {
                    // Check if it is not a file, if true, continue, since there is no interest in this module
                    continue;
                }

                // --> Get the Contents (only the files)
                JsonArray contents = (JsonArray) module.get("contents");
                for (JsonElement contentJSON: contents) {
                    JsonObject content = (JsonObject) contentJSON;

                    if (content.get("type").getAsString().equals("file")) {
                        // Create the file
                        File fi = new File(
                                // If it is inside a folder, then assume it's real name
                                folder ? content.get("filename").getAsString() : module.get("name").getAsString(),
                                content.get("filename").getAsString(),
                                content.get("timemodified").getAsInt(),
                                content.get("fileurl").getAsString(),
                                course.isDownloadable()
                        );

                        // Add to the folder or the section
                        if (folder) {
                            fo.addFile(fi);
                        } else {
                            s.addContent(fi);
                        }
                    }
                }
            }
        }
    }

    private String sendPOSTRequest(String url, RequestBody body) throws IOException, CustomException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new CustomException(Errors.INVALID_URL);
        }
        if (!response.isSuccessful()) throw new CustomException(Errors.INVALID_URL);

        return response.body().string();
    }
}
