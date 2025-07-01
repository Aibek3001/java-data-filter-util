package com.example.datafilter;

import java.util.ArrayList;
import java.util.List;

public class CliOptions {
    private String outputDir = ".";
    private String prefix = "";
    private boolean append = false;
    private boolean summary = false;
    private boolean full = false;
    private final List<String> inputFiles = new ArrayList<>();

    public static CliOptions parse(String[] args) {
        CliOptions opts = new CliOptions();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (++i < args.length) opts.outputDir = args[i];
                    break;
                case "-p":
                    if (++i < args.length) opts.prefix = args[i];
                    break;
                case "-a":
                    opts.append = true;
                    break;
                case "-s":
                    opts.summary = true;
                    break;
                case "-f":
                    opts.full = true;
                    break;
                default:
                    opts.inputFiles.add(args[i]);
            }
        }
        return opts;
    }

    public String getOutputDir() { return outputDir; }
    public String getPrefix() { return prefix; }
    public boolean isAppend() { return append; }
    public boolean isSummary() { return summary; }
    public boolean isFull() { return full; }
    public List<String> getInputFiles() { return inputFiles; }
}
