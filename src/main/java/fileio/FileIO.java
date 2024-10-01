package fileio;

import java.io.IOException;

public interface FileIO {
    String read(String fileName) throws IOException;

    void write(String fileName, String value) throws IOException;
}
