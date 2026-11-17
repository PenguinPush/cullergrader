package com.penguinpush.cullergrader.utils;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.config.AppPaths;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Consumer;

public class Logger {

    private static final File LOG_DIRECTORY = AppPaths.resolve(AppConstants.LOG_DIRECTORY);
    private static final int MAX_LOG_FILES = 20;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static String fileName = LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".txt";

    private static File logFile;
    private static Consumer<String> logCallback;

    static {
        try {
            if (!LOG_DIRECTORY.exists()) {
                LOG_DIRECTORY.mkdirs();
            }

            pruneOldLogs();

            logFile = new File(LOG_DIRECTORY, fileName);

            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
        }
    }

    /**
     * Keeps only the newest log files, so the hidden data directory doesn't grow forever.
     * Log names are timestamps, so sorting by name sorts by age.
     */
    private static void pruneOldLogs() {
        File[] logs = LOG_DIRECTORY.listFiles((dir, name) -> name.endsWith(".txt"));
        if (logs == null || logs.length < MAX_LOG_FILES) {
            return;
        }

        Arrays.sort(logs);
        // leave room for the log file this session is about to create
        for (int i = 0; i <= logs.length - MAX_LOG_FILES; i++) {
            logs[i].delete();
        }
    }

    public static synchronized void logMessage(String message) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

            bufferedWriter.write(timestamp + ": " + message);
            bufferedWriter.newLine();

            if (logCallback != null) {
                logCallback.accept(message);
            }

            System.out.println(message);

        } catch (IOException e) {
            System.err.println("an error occurred... ironically, there's nowhere to log this:  " + e.getMessage());
        }
    }

    public static void registerLogCallback(Consumer<String> callback) {
        logCallback = callback;
    }

    /**
     * Logs a message to console and log file only, without triggering the GUI callback.
     * Useful for verbose/cache operations that shouldn't clutter the GUI info panel.
     */
    public static synchronized void logToConsoleOnly(String message) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

            bufferedWriter.write(timestamp + ": " + message);
            bufferedWriter.newLine();

            // Do NOT call logCallback - this prevents GUI updates
            System.out.println(message);

        } catch (IOException e) {
            System.err.println("an error occurred... ironically, there's nowhere to log this:  " + e.getMessage());
        }
    }
}