package com.penguinpush.cullergrader.utils;

import com.penguinpush.cullergrader.config.AppConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class Logger {

    private static final String LOG_DIRECTORY = AppConstants.LOG_DIRECTORY;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static String fileName = LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".txt";

    private static File logFile;
    private static Consumer<String> logCallback;

    static {
        try {
            File logDirectory = new File(LOG_DIRECTORY);

            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }

            logFile = new File(logDirectory, fileName);

            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
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