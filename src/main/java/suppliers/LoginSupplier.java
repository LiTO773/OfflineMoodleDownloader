package suppliers;

import java.util.Map;

/**
 * Defines how a login Service should behave
 */
public interface LoginSupplier {
    // Used to login into the service, it gets it's information from CurrentMoodle
    Map<String, String> login(String url, String username, String password) throws Exception;
}
