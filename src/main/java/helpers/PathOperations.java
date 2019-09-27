package helpers;

import java.io.File;
import java.util.ArrayList;

public class PathOperations {
    public static String path(String original, String file) {
        String os = System.getProperty("os.name");
        boolean isWindows = os.startsWith("Windows");

        return original + (isWindows ? '\\' : '/') + safeFileName(file);
    }

    public static String path(String original, ArrayList<String> files) {
        String result = original;
        for (String file: files) {
            result = path(result, file);
        }
        return result;
    }

    public static boolean createFolder(String path) {
        return new File(path).mkdirs();
    }

    // Removes any unsupported file characters from a String and replaces them with a space
    // These are the characters: \\:\/\|\"\?\*<>
    // The atrocity below is Java:
    public static String safeFileName(String name) {
        return name.replaceAll("[\\\\\\\\:\\\\/\\\\|\\\\\\\"\\\\?\\\\*<>]", " ").trim();
    }
}
