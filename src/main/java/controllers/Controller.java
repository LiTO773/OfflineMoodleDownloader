package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Controller {
    public TextField moodleField;
    public TextField nameField;
    public PasswordField passwordField;

    public void doLogin(ActionEvent actionEvent) {
        try {
            sendLoginRequest(
                moodleField.getText(),
                nameField.getText(),
                passwordField.getText()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLoginRequest(String moodle, String username, String password) throws Exception {
        String url = moodle + "/login/token.php?username=" + username + "&password=" + password + "&service=moodle_mobile_app";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        // send request
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        // read request
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        System.out.println(response.toString());
        //Read JSON response and print
        //JSONObject myResponse = new JSONObject(response.toString());
    }
}
