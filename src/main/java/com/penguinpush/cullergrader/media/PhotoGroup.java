package com.penguinpush.cullergrader.media;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PhotoGroup extends GridMedia {
    private final List<Photo> photos = new ArrayList<>();
    private Photo bestTake;
    
    @Override
    public BufferedImage getThumbnail() {
        return bestTake.getThumbnail();
    }
    
    @Override
    public String getName() {
        return bestTake.getFile().getName() + " (group)";
    }
    
    @Override
    public int getSize() {
        return photos.size();
    }
    
    @Override
    public String getTooltip() {
        return Integer.toString(getSize());
    }
    
    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setGroup(this);
        if (bestTake == null)
            bestTake = photo;
    }
    
    public void addPhotos(List<Photo> photos) {
        for (Photo photo : photos) {
            this.photos.add(photo);
            photo.setGroup(this);
        }
    }
    
    public boolean removePhoto(Photo photo) {
        boolean removed = photos.remove(photo);
        
        if (removed) {
            photo.setGroup(null);
            
            if (photo.equals(bestTake)) {
                bestTake = photos.isEmpty() ? null : photos.get(0); // fallback to first photo, or null if empty
            }
        }
        
        return removed;
    }

    public List<Photo> getPhotos() {
        return Collections.unmodifiableList(photos); // read-only list
    }
    
    public void setBestTake(Photo photo) {
        if (photos.contains(photo)) {
            bestTake = photo;
        }
    }

    public Photo getBestTake() {
        return bestTake;
    }
}
