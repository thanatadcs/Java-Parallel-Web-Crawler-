import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class URLDownloader {
    // Implement using Apache HttpComponents
    public static void get(String url, String filepath) {
        try {
            URL baseUrl = new URL(url);
            FileUtils.copyURLToFile(baseUrl, new File(filepath + baseUrl.getFile()));
        } catch (IOException | IllegalArgumentException e) {
            try (FileWriter myWriter = new FileWriter("download_error.txt", true)) {
                myWriter.write(url + "\n");
            } catch (IOException ex) {
                System.out.println("Error while writing to log");
            }
        }
    }
}
