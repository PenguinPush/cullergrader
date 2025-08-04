package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.media.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.penguinpush.cullergrader.config.AppConstants;
import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import java.awt.image.BufferedImage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class HashManager {

    private static final File CACHE_FILE = new File(AppConstants.CACHE_DIRECTORY);
    private final Map<String, HashEntry> cache = new HashMap<>();

    public HashManager() {
        loadCache();
    }

    public List<Photo> hashAllPhotos(File[] files) {
        List<Photo> photoList = Collections.synchronizedList(new ArrayList<>());

        int maxThreads = Math.max(1, Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newWorkStealingPool(maxThreads);

        for (File file : files) {
            // handle the hashing by executing along multiple threads
            executor.submit(() -> {
                try {
                    String hash;

                    if (AppConstants.HASHING_ENABLED) {
                        hash = getOrComputeHash(file);
                    } else {
                        hash = "0";
                    }

                    long timestamp = PhotoUtils.extractTimestamp(file);
                    Photo photo = new Photo(file, timestamp, hash);
                    photoList.add(photo);
                } catch (Exception e) {
                    System.out.println("error processing: " + file.getName());
                    logMessage("error processing: " + file.getName());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(AppConstants.EXECUTOR_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("thread interrupted: " + e.getMessage());
            logMessage("thread interrupted: " + e.getMessage());
        }

        return photoList;
    }

    public String getOrComputeHash(File file) {
        String path = file.getAbsolutePath();
        long lastModified = file.lastModified();

        HashEntry entry = cache.get(path);
        if (entry != null && entry.lastModified == lastModified && entry.hash.length() == AppConstants.HASHED_WIDTH * AppConstants.HASHED_HEIGHT * 3) {
            // return the cached hash if entry not null, last modified dates line up, and hash length is unchanged
            System.out.println("file: " + file.getName() + ", retrieving hash: " + entry.hash);
            logMessage("file: " + file.getName() + ", retrieving hash: " + entry.hash);
            return entry.hash;
        }

        // else, hash the file and return
        try {
            BufferedImage image = PhotoUtils.readLowResImage(file, AppConstants.HASHED_WIDTH, AppConstants.HASHED_HEIGHT);
            if (image == null) {
                System.out.println("no image at: " + file.getName());
                logMessage("no image at: " + file.getName());
                throw null;
            }

            String hash = HashUtils.computeChanneledAverageHash(image);
            cache.put(path, new HashEntry(lastModified, hash));
            System.out.println("file: " + file.getName() + ", sucessfully generated hash: " + hash);
            logMessage("file: " + file.getName() + ", sucessfully generated hash: " + hash);
            return hash;
        } catch (Exception e) {
            return null;
        }

    }

    private void loadCache() {
        if (!CACHE_FILE.exists()) {
            return;
        }
        try (Reader reader = new FileReader(CACHE_FILE)) {
            Type type = new TypeToken<Map<String, HashEntry>>() {
            }.getType();
            Map<String, HashEntry> loaded = new Gson().fromJson(reader, type);
            if (loaded != null) {
                cache.putAll(loaded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCache() {
        try (Writer writer = new FileWriter(CACHE_FILE)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(cache, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("successfully saved cache!");
        logMessage("successfully saved cache!");
    }

    private static class HashEntry {
        long lastModified;
        String hash;

        public HashEntry(long lastModified, String hash) {
            this.lastModified = lastModified;
            this.hash = hash;
        }
    }
}
