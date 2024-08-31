package com.zygne.data;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class FileWriter {

    private static final String DIRECTORY_NAME = "market/data";

    private PrintWriter writer = null;

    public FileWriter(String name) {

        try {
            File directory = new File(DIRECTORY_NAME);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(DIRECTORY_NAME + "/" + name);
            writer = new PrintWriter(file.getAbsolutePath(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeLine(String line) {
        if (writer == null) {
            return;
        }
        writer.println(line);
    }

    public void close() {
        if (writer == null) {
            return;
        }
        writer.close();
    }
}
