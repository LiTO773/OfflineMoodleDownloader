package suppliers;

import com.squareup.okhttp.*;

import java.io.*;
import java.nio.file.Paths;

import static helpers.PathOperations.safeFileName;

public class DownloadSupplier {
    public Void download(String fileName, String location, String fileURL) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(fileURL).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to download file: " + response);
        }
        String fileLocation = Paths.get(location, safeFileName(fileName)).toString();

        FileOutputStream fos = new FileOutputStream(fileLocation);
        fos.write(response.body().bytes());
        fos.close();
        return null;
    }
}
