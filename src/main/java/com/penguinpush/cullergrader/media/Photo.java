package com.penguinpush.cullergrader.media;

import com.penguinpush.cullergrader.config.AppConstants;
import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class Photo extends GridMedia {

    private final File file;
    private final long timestamp;
    private final String hash;
    private int grade;
    private PhotoGroup group;

    private float deltaTimeRatio;
    private float hammingDistanceRatio;

    public Photo(File file, long timestamp, String hash) {
        this.file = file;
        this.timestamp = timestamp;
        this.hash = hash;
        this.grade = 0;
    }

    @Override
    public BufferedImage getThumbnail() {
        BufferedImage thumbnail = thumbnailRef == null ? null : thumbnailRef.get();
        if (thumbnail == null) {
            try {
                thumbnail = generateThumbnail(file);
            } catch (Exception e) {
            }
            thumbnailRef = new SoftReference<>(thumbnail);
        }
        return thumbnail;
    }

    @Override
    public String getName() {
        return file.getName() + " (photo)";
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public String getTooltip() {
        return getMetrics().toString();
    }

    private BufferedImage generateThumbnail(File file) throws Exception {
        try {
            int width = AppConstants.THUMBNAIL_ICON_WIDTH;
            int height = AppConstants.THUMBNAIL_ICON_HEIGHT;

            BufferedImage thumbnail = PhotoUtils.readLowResImage(file, width, height);
            if (thumbnail == null) {
                System.out.println("no thumbnail at: " + file.getName());
                logMessage("no thumbnail at: " + file.getName());
                throw null;
            }

            return thumbnail;
        } catch (IOException e) {
            System.err.println("couldn't get thumbnail for: " + file.getName());
            logMessage("couldn't get thumbnail for: " + file.getName());
            return null;
        }
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public PhotoGroup getGroup() {
        return group;
    }

    public void setGroup(PhotoGroup group) {
        this.group = group;
    }

    public boolean isBestTake() {
        return group != null && group.getBestTake() == this;
    }

    public void setMetrics(float deltaTimeRatio, float hammingDistanceRatio) {
        this.deltaTimeRatio = deltaTimeRatio;
        this.hammingDistanceRatio = hammingDistanceRatio;
    }

    public List<Float> getMetrics() {
        List<Float> metrics = new ArrayList<>();
        metrics.add(deltaTimeRatio);
        metrics.add(hammingDistanceRatio);

        return metrics;
    }
}
