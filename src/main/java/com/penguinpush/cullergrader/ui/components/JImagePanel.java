
package com.penguinpush.cullergrader.ui.components;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;

import com.penguinpush.cullergrader.logic.ImageLoader;
import com.penguinpush.cullergrader.media.Photo;
import com.penguinpush.cullergrader.config.AppConstants;

public class JImagePanel extends JPanel {
    private BufferedImage image;
    private Photo photo;
    private ImageLoader imageLoader;

    public JImagePanel() {
        super();
    }
    
    public void init(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        imageLoader.loadImage(photo, AppConstants.IMAGE_PRIORITY, false, (imagePreview) -> {
            SwingUtilities.invokeLater(() -> setImage(imagePreview));

            imageLoader.loadImage(photo, AppConstants.MAX_PRIORITY, true, (imageFull) -> {
                SwingUtilities.invokeLater(() -> setImage(imageFull));
            });
        });
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    public Photo getPhoto() {
        return photo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();

        double panelAspect = (double) panelWidth / panelHeight;
        double imgAspect = (double) imgWidth / imgHeight;

        int drawWidth, drawHeight;

        if (panelAspect > imgAspect) {
            // prioritize height
            drawHeight = panelHeight;
            drawWidth = (int) (imgAspect * drawHeight);
        } else {
            // prioritize width
            drawWidth = panelWidth;
            drawHeight = (int) (drawWidth / imgAspect);
        }

        int x = (panelWidth - drawWidth) / 2;
        int y = (panelHeight - drawHeight) / 2;

        g.drawImage(image, x, y, drawWidth, drawHeight, this);
    }
}
