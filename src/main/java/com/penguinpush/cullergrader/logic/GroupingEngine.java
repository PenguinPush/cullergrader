package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.media.*;

import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import javax.swing.*;

import java.util.*;
import java.io.File;

public class GroupingEngine {

    public List<Photo> photoListFromFolder(File folder) {
        File[] imageFiles = folder.listFiles((f) -> f.isFile() && PhotoUtils.isImageFile(f));
        if (imageFiles == null) {
            return Collections.emptyList();
        }

        HashManager hashManager = new HashManager();
        List<Photo> photoList = hashManager.hashAllPhotos(imageFiles);

        hashManager.saveCache();

        // sort first by timestamp, and then by file name
        photoList.sort(Comparator
                .comparingLong(Photo::getTimestamp)
                .thenComparing(photo -> photo.getFile().getName()));

        return photoList;
    }

    public List<PhotoGroup> generateGroups(List<Photo> photoList, float timestampThreshold, float similarityThreshold) {
        List<PhotoGroup> groups = new ArrayList<>();
        PhotoGroup currentGroup = new PhotoGroup();

        for (int i = 0; i < photoList.size(); i++) {
            Photo current = photoList.get(i);

            if (currentGroup.getSize() == 0) {
                current.setIndex(0);
                currentGroup.addPhoto(current);
                continue;
            }

            Photo last = currentGroup.getPhotos().get(currentGroup.getSize() - 1);
            long deltaTime = Math.abs(current.getTimestamp() - last.getTimestamp());
            int hammingDistance = HashUtils.hammingDistance(current.getHash(), last.getHash());

            float deltaTimeSeconds = deltaTime / 1000;
            float hammingDistancePercent = 100 * ((float) hammingDistance) / (AppConstants.HASHED_WIDTH * AppConstants.HASHED_HEIGHT * 3);
            
            // add to the group if it's within time and hash thresholds, otherwise make a new one
            if (deltaTimeSeconds <= timestampThreshold && hammingDistancePercent <= similarityThreshold) {
                current.setIndex(last.getIndex() + 1);
                currentGroup.addPhoto(current);
            } else {
                currentGroup.setIndex(groups.size());
                groups.add(currentGroup);
                currentGroup = new PhotoGroup();

                current.setIndex(0);
                currentGroup.addPhoto(current);
            }

            current.setMetrics(deltaTimeSeconds, hammingDistancePercent);

            logMessage("added " + current.getFile().getName() + " " + deltaTimeSeconds + " " + hammingDistancePercent + " to group " + groups.size());
        }

        // add the last group too
        if (currentGroup.getSize() > 0) {
            currentGroup.setIndex(groups.size());
            groups.add(currentGroup);
        }

        return groups;
    }

}
