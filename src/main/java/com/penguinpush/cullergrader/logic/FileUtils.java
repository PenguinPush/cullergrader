package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.media.Photo;
import com.penguinpush.cullergrader.media.PhotoGroup;
import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static List<PhotoGroup> loadFolder(File path, GroupingEngine groupingEngine, float timestampThreshold, float similarityThreshold) {
        List<Photo> photos = groupingEngine.photoListFromFolder(path);

        return groupingEngine.generateGroups(photos, timestampThreshold, similarityThreshold);
    }

    public static void exportBestTakes(List<PhotoGroup> photoGroups, File targetFolder) {
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        for (PhotoGroup group : photoGroups) {
            Photo bestTake = group.getBestTake();
            if (bestTake != null) {
                File sourceFile = bestTake.getFile();
                File destinationFile = new File(targetFolder, sourceFile.getName());

                try {
                    Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    logMessage("copied file: " + sourceFile.getAbsolutePath());
                } catch (IOException e) {
                    logMessage("couldn't copy file: " + sourceFile.getAbsolutePath());
                }
            }
        }
    }

    public static void exportGroupsJson(List<PhotoGroup> photoGroups, File jsonFile,
                                         float timeThreshold, float similarityThreshold) {
        // Create parent directories if needed
        File parentDir = jsonFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Build JSON structure
        Map<String, Object> root = new HashMap<>();
        root.put("totalGroups", photoGroups.size());
        root.put("totalPhotos", photoGroups.stream().mapToInt(PhotoGroup::getSize).sum());
        root.put("exportTimestamp", System.currentTimeMillis());

        Map<String, Float> thresholds = new HashMap<>();
        thresholds.put("timeThresholdSeconds", timeThreshold);
        thresholds.put("similarityThresholdPercent", similarityThreshold);
        root.put("thresholds", thresholds);

        List<Map<String, Object>> groupsList = new ArrayList<>();
        for (PhotoGroup group : photoGroups) {
            Map<String, Object> groupMap = new HashMap<>();
            groupMap.put("groupIndex", group.getIndex());
            groupMap.put("photoCount", group.getSize());

            Photo bestTake = group.getBestTake();
            if (bestTake != null) {
                groupMap.put("bestTakeFilename", bestTake.getFile().getName());
            }

            List<Map<String, Object>> photosList = new ArrayList<>();
            for (Photo photo : group.getPhotos()) {
                Map<String, Object> photoMap = new HashMap<>();
                photoMap.put("filename", photo.getFile().getName());
                photoMap.put("path", photo.getPath());
                photoMap.put("timestamp", photo.getTimestamp());
                photoMap.put("hash", photo.getHash());
                photoMap.put("isBestTake", photo.isBestTake());

                List<Float> metrics = photo.getMetrics();
                if (metrics.size() >= 2) {
                    photoMap.put("deltaTimeSeconds", metrics.get(0));
                    photoMap.put("similarityPercent", metrics.get(1));
                }

                photosList.add(photoMap);
            }
            groupMap.put("photos", photosList);
            groupsList.add(groupMap);
        }
        root.put("groups", groupsList);

        // Write JSON file with pretty printing
        try (FileWriter writer = new FileWriter(jsonFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(root, writer);
            logMessage("Exported group information to: " + jsonFile.getAbsolutePath());
        } catch (IOException e) {
            logMessage("Failed to export JSON: " + e.getMessage());
            throw new RuntimeException("Failed to export JSON", e);
        }
    }
}
