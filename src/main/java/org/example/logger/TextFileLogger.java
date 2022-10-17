package org.example.logger;

import java.io.IOException;

import static org.example.FileUtils.createNewFile;
import static org.example.FileUtils.writeTextToFile;

public class TextFileLogger implements Logger {

    private String path;
    private String file;

    public TextFileLogger(String path, String file) {
        this.path = path;
        this.file = file;
        createNewFile(path, file);
        logNewMessage("---New session---");
    }

    @Override
    public void logNewMessage(String message) {
        try {
            writeTextToFile(path, file, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
