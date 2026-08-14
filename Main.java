import util.FileManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String fileName = "data/test.txt";

        FileManager.writeToFile(
                fileName,
                "QueueCare is working!\n"
        );

        FileManager.writeToFile(
                fileName,
                "File handling is ready.\n"
        );

        List<String> data = FileManager.readFromFile(fileName);

        System.out.println("===== QueueCare =====");

        for (String line : data) {
            System.out.println(line);
        }
    }
}