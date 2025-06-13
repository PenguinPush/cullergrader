package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.media.*;

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

    public List<PhotoGroup> generateGroups(List<Photo> photoList) {
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

            // add to the group if it's within time and hash thresholds, otherwise make a new one
            if (deltaTime <= AppConstants.TIME_THRESHOLD_MS && hammingDistance <= AppConstants.HASH_DISTANCE_THRESHOLD) {
                current.setIndex(last.getIndex() + 1);
                currentGroup.addPhoto(current);
            } else {
                currentGroup.setIndex(groups.size());
                groups.add(currentGroup);
                currentGroup = new PhotoGroup();

                current.setIndex(0);
                currentGroup.addPhoto(current);
            }

            float deltaTimeRatio = (float) deltaTime / AppConstants.TIME_THRESHOLD_MS;
            float hammingDistanceRatio = (float) hammingDistance / AppConstants.HASH_DISTANCE_THRESHOLD;

            current.setMetrics(deltaTimeRatio, hammingDistanceRatio);

            System.out.print("added " + current.getFile().getName() + " to group " + groups.size());
            System.out.print(" " + deltaTimeRatio + " " + hammingDistanceRatio);
            System.out.print("\n");
        }

        // add the last group too
        if (currentGroup.getSize() > 0) {
            currentGroup.setIndex(groups.size());
            groups.add(currentGroup);
        }

        return groups;
    }
}
