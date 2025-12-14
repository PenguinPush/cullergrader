package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.media.Photo;
import com.penguinpush.cullergrader.media.PhotoGroup;
import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileUtils {

    public static List<PhotoGroup> loadFolder(File path, GroupingEngine groupingEngine, float timestampThreshold, float similarityThreshold) {
        List<Photo> photos = groupingEngine.photoListFromFolder(path);

        return groupingEngine.generateGroups(photos, timestampThreshold, similarityThreshold);
    }

    public static void exportBestTakes(List<PhotoGroup> photoGroups, File targetFolder) {
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        Map<String, Integer> filenameCounts = new HashMap<>();

        for (PhotoGroup group : photoGroups) {
            Set<Photo> selectedPhotos = group.getSelectedTakes();

            // Skip groups with 0 selections
            if (selectedPhotos.isEmpty()) {
                continue;
            }

            for (Photo photo : selectedPhotos) {
                File sourceFile = photo.getFile();
                String originalName = sourceFile.getName();
                String baseName = getBaseName(originalName);
                String extension = getExtension(originalName);

                // Handle filename collisions
                String finalName = originalName;
                if (filenameCounts.containsKey(originalName)) {
                    int count = filenameCounts.get(originalName);
                    finalName = baseName + "_" + count + extension;
                    filenameCounts.put(originalName, count + 1);
                } else {
                    filenameCounts.put(originalName, 1);
                }

                File destinationFile = new File(targetFolder, finalName);

                try {
                    Files.copy(sourceFile.toPath(), destinationFile.toPath(),
                              StandardCopyOption.REPLACE_EXISTING);
                    logMessage("copied file: " + sourceFile.getAbsolutePath() + " → " + finalName);
                } catch (IOException e) {
                    logMessage("couldn't copy file: " + sourceFile.getAbsolutePath());
                }
            }
        }
    }

    private static String getBaseName(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(0, lastDot) : filename;
    }

    private static String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot) : "";
    }
}
