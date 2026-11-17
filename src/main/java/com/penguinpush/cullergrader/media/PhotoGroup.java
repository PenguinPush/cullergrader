package com.penguinpush.cullergrader.media;

import com.penguinpush.cullergrader.config.AppConstants;
import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class PhotoGroup extends GridMedia {
    private final List<Photo> photos = new ArrayList<>();
    private final LinkedHashSet<Photo> selectedTakes = new LinkedHashSet<>();

    @Override
    public BufferedImage getThumbnail() {
        // Always use first photo in group for thumbnail
        return photos.isEmpty() ? null : photos.get(0).getThumbnail();
    }

    @Override
    public String getName() {
        // Always use first photo in group for name
        return photos.isEmpty() ? "(empty group)" : photos.get(0).getFile().getName() + " (group)";
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
            // Remove from selected takes (allow 0 selections)
            selectedTakes.remove(photo);
        }

        return removed;
    }

    public List<Photo> getPhotos() {
        return Collections.unmodifiableList(photos); // read-only list
    }

    // New selection methods
    public Set<Photo> getSelectedTakes() {
        return Collections.unmodifiableSet(selectedTakes);
    }

    public boolean isSelected(Photo photo) {
        return selectedTakes.contains(photo);
    }

    public boolean addSelectedTake(Photo photo) {
        if (photos.contains(photo)) {
            return selectedTakes.add(photo);
        }
        return false;
    }

    public boolean removeSelectedTake(Photo photo) {
        return selectedTakes.remove(photo);
    }

    public void toggleSelection(Photo photo) {
        if (isSelected(photo)) {
            removeSelectedTake(photo);
        } else {
            addSelectedTake(photo);
        }
    }

    public void clearSelections() {
        selectedTakes.clear();
    }

    public void applyDefaultSelectionStrategy() {
        String strategy = AppConstants.DEFAULT_SELECTION_STRATEGY;

        switch (strategy.toLowerCase()) {
            case "first":
                if (!photos.isEmpty()) {
                    selectedTakes.add(photos.get(0));
                }
                break;

            case "last":
                if (!photos.isEmpty()) {
                    selectedTakes.add(photos.get(photos.size() - 1));
                }
                break;

            case "first_and_last":
                if (!photos.isEmpty()) {
                    selectedTakes.add(photos.get(0));
                    if (photos.size() > 1) {
                        selectedTakes.add(photos.get(photos.size() - 1));
                    }
                }
                break;

            case "all":
                selectedTakes.addAll(photos);
                break;

            case "none":
                // Don't select anything
                break;

            default:
                // Invalid strategy, fallback to "first"
                if (!photos.isEmpty()) {
                    selectedTakes.add(photos.get(0));
                }
                logMessage("Invalid selection strategy: " + strategy + ", using 'first'");
        }
    }
}
