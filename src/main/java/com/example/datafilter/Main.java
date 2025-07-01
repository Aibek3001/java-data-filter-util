package com.example.datafilter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CliOptions options = CliOptions.parse(args);
        if (options == null || options.getInputFiles().isEmpty()) {
            System.err.println("Ошибка: необходимо указать хотя бы один входной файл.");
            System.exit(1);
        }

        try {
            new DataFilterProcessor(options).run();
        } catch (IOException e) {
            System.err.println("Произошла ошибка при выполнении: " + e.getMessage());
        }
    }
}
