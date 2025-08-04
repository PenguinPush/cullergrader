package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.media.Photo;
import com.penguinpush.cullergrader.media.PhotoGroup;
import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

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
}
