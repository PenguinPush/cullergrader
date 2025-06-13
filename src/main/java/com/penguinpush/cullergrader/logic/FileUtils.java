package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.media.Photo;
import com.penguinpush.cullergrader.media.PhotoGroup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileUtils {
    public static List<PhotoGroup> loadFolder(File path, GroupingEngine groupingEngine) {
        List<Photo> photos = groupingEngine.photoListFromFolder(path);

        return groupingEngine.generateGroups(photos);
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
                    System.out.println("copied file: " + sourceFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("couldn't copy file: " + sourceFile.getAbsolutePath());
                }
            }
        }
    }
}
