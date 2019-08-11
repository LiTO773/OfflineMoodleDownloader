package suppliers;

import com.google.gson.JsonObject;

/**
 * Defines how a login Service should behave
 */
public interface LoginSupplier {
    // Used to login into the service, it gets it's information from CurrentMoodle
    JsonObject login(String url, String username, String password) throws Exception;
}
