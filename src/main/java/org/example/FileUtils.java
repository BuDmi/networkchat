package org.example;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    public static Boolean createNewFile(String path, String fileName) {
        File file = new File(fileName);

        try {
            if (file.exists()) {
                file.delete();
            }
            return file.createNewFile();
        } catch (IOException ex) {
            return false;
        }
    }

    public static Map<String, String> readSettingsFromFile(String path, String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path + file))) {
            String str;
            Map<String, String> settings = new HashMap<>();
            while((str = reader.readLine()) != null) {
                String[] tmp = str.split("=");
                if (tmp.length == 2) {
                    settings.put(tmp[0], tmp[1]);
                }
            }
            return settings;
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            throw new IOException(ex);
        }
    }

    public static void writeTextToFile(String path, String file, String text) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            throw new IOException(ex);
        }
    }
}
