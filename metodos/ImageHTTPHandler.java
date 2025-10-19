package metodos;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 *
 * @author Jesus Castillo
 */
public class ImageHTTPHandler {

    private final String baseUrl;

    public ImageHTTPHandler(String serverUrl) {
        this.baseUrl = serverUrl;
    }

    /**
     * Uploads an image to the server
     *
     * @param imagePath Path to the image file to upload
     * @return The server-side path/identifier of the uploaded image
     * @throws IOException If the upload fails
     */
    public String upload(String imagePath) throws IOException {
        URL url = URI.create(baseUrl + "/upload").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        String boundary = UUID.randomUUID().toString();
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.setDoOutput(true);

        try (OutputStream out = conn.getOutputStream()) {
            // Write form boundary
            String boundaryLine = "--" + boundary + "\r\n";
            out.write(boundaryLine.getBytes(StandardCharsets.UTF_8));

            // Write file header
            String fileHeader = "Content-Disposition: form-data; name=\"file\"; filename=\""
                    + Path.of(imagePath).getFileName() + "\"\r\n\r\n";
            out.write(fileHeader.getBytes(StandardCharsets.UTF_8));

            // Write file content
            Files.copy(Path.of(imagePath), out);

            // Write final boundary
            out.write(("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() == 200) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // Extract path from JSON response
                String json = response.toString();
                return json.substring(json.indexOf(":\"") + 2, json.lastIndexOf("\""));
            }
        }
        throw new IOException("Upload failed with status: " + conn.getResponseCode());
    }

    /**
     * Retrieves an image from the server
     *
     * @param imagePath The server-side path/identifier of the image
     * @return The image data as a byte array
     * @throws IOException If the image retrieval fails
     */
    public byte[] obtain(String imagePath) throws IOException {
        URL url = URI.create(baseUrl + "/app/images/" + imagePath).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            try (InputStream in = conn.getInputStream()) {
                return in.readAllBytes();
            }
        }
        throw new IOException("Image retrieval failed with status: " + conn.getResponseCode());
    }

    /**
     * Removes an image from the server
     *
     * @param imagePath The server-side path/identifier of the image to delete
     * @return true if the image was successfully deleted, false otherwise
     * @throws IOException If the deletion request fails
     */
    public boolean remove(String imagePath) throws IOException {
        URL url = URI.create(baseUrl + "/delete/" + imagePath).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        return conn.getResponseCode() == 200;
    }
}
