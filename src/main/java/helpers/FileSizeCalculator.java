package helpers;

public class FileSizeCalculator {
    // Receives the size in bytes and coverts to a string in kb, mb or gb
    public static String calculate(int size) {
        if (size < 1023) {
            return size + " bytes";
        } else if (size < 1048999) {
            return (size/1024) + " KB";
        } else if (size < 1073999999) {
            return (size/1049000) + " MB";
        }
        return (size/1074000000) + " GB";
    }
}
