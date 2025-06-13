package com.penguinpush.cullergrader.media;

import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;

public class GridMedia {
    protected BufferedImage thumbnail;
    protected SoftReference<BufferedImage> thumbnailRef;
    private int index;

    public BufferedImage getThumbnail() {
        return null;
    }
    
    public String getName() {
        return null;
    }
    
    public int getSize() {
        return -1;
    }
    
    public String getTooltip() {
        return null;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
