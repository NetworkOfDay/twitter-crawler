package writer;

import java.io.FileWriter;
import java.io.IOException;


public class JsonWriter {
    
    private final FileWriter writer;
    
    public JsonWriter(String filename) {
        this.writer = createWriter(filename + ".json", true);
    }

    private FileWriter createWriter(String filename, boolean append) {
        FileWriter f = null;
        try {
            f = new FileWriter(filename, append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public void writeTweet(String tweet) {
        try {
            writer.write(tweet);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
