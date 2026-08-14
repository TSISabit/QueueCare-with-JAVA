package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static void writeToFile(String fileName, String data) {
        writeToFile(fileName, data, true);
    }

    public static void writeToFile(String fileName, String data, boolean append) {
        try {
            File file = new File(fileName);

            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            FileWriter writer = new FileWriter(file, append);

            writer.write(data);

            writer.close();

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<String> readFromFile(String fileName) {
        List<String> data = new ArrayList<>();

        try {
            File file = new File(fileName);

            if (!file.exists()) {
                return data;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                data.add(line);
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return data;
    }
}