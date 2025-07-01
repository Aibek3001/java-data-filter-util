package com.example.datafilter;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataFilterProcessor {
    private final CliOptions options;
    private final List<String> strings = new ArrayList<>();
    private final List<Long> integers = new ArrayList<>();
    private final List<Double> floats = new ArrayList<>();

    public DataFilterProcessor(CliOptions options) {
        this.options = options;
    }

    public void run() throws IOException {
        readInputFiles();
        Files.createDirectories(Paths.get(options.getOutputDir()));
        writeResults();
        printStatistics();
    }

    private void readInputFiles() {
        for (String file : options.getInputFiles()) {
            Path path = Paths.get(file);
            if (!Files.exists(path)) {
                System.err.println("Файл не найден: " + file);
                continue;
            }
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    classify(line.trim());
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла: " + file);
            }
        }
    }

    private void classify(String line) {
        if (line.isEmpty()) return;

        try {
            if (line.matches("[+-]?\\d+")) {
                integers.add(Long.parseLong(line));
            } else if (line.matches("[+-]?((\\d*\\.\\d+)|(\\d+\\.\\d*))(E[+-]?\\d+)?")) {
                floats.add(Double.parseDouble(line));
            } else {
                strings.add(line);
            }
        } catch (NumberFormatException e) {
            strings.add(line);
        }
    }

    private <T> void writeFile(String suffix, List<T> data) {
        String fileName = Paths.get(options.getOutputDir(), options.getPrefix() + suffix).toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, options.isAppend()))) {
            for (T item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + fileName);
        }
    }

    private void writeResults() {
        if (!integers.isEmpty()) writeFile("integers.txt", integers);
        if (!floats.isEmpty()) writeFile("floats.txt", floats);
        if (!strings.isEmpty()) writeFile("strings.txt", strings);
    }

    private void printStatistics() {
        if (!options.isSummary() && !options.isFull()) return;

        System.out.println("СТАТИСТИКА:");

        if (!integers.isEmpty()) {
            System.out.println("Целые: " + integers.size());
            if (options.isFull()) {
                long min = Collections.min(integers);
                long max = Collections.max(integers);
                long sum = integers.stream().mapToLong(Long::longValue).sum();
                System.out.printf("  Мин: %d, Макс: %d, Сумма: %d, Среднее: %.2f%n", min, max, sum, sum / (double) integers.size());
            }
        }

        if (!floats.isEmpty()) {
            System.out.println("Вещественные: " + floats.size());
            if (options.isFull()) {
                double min = Collections.min(floats);
                double max = Collections.max(floats);
                double sum = floats.stream().mapToDouble(Double::doubleValue).sum();
                System.out.printf("  Мин: %.6f, Макс: %.6f, Сумма: %.6f, Среднее: %.6f%n", min, max, sum, sum / floats.size());
            }
        }

        if (!strings.isEmpty()) {
            System.out.println("Строки: " + strings.size());
            if (options.isFull()) {
                int min = strings.stream().mapToInt(String::length).min().orElse(0);
                int max = strings.stream().mapToInt(String::length).max().orElse(0);
                System.out.printf("  Мин. длина: %d, Макс. длина: %d%n", min, max);
            }
        }
    }
}
