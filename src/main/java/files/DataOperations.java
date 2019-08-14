package files;

import models.CurrentMoodle;
import models.MoodleStorage;

import java.io.*;

public class DataOperations {
    private static String FILE = "data.omd";

    /**
     * Reads the data from the serialized file
     * @return If the data was successfully read
     */
    public static MoodleStorage readData() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(FILE);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
        MoodleStorage result = (MoodleStorage) objectInputStream.readObject();
        objectInputStream.close();
        System.out.println("loaded: " + result.size() + " moodles!");
        return result;
    }

    public static void writeData() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(FILE);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
        objectOutputStream.writeObject(CurrentMoodle.getAllMoodles());
        System.out.println("Written!");
        objectOutputStream.close();
    }
}
