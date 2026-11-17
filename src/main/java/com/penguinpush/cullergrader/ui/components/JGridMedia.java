package com.penguinpush.cullergrader.ui.components;

import com.penguinpush.cullergrader.logic.ImageLoader;
import com.penguinpush.cullergrader.media.*;
import com.penguinpush.cullergrader.ui.*;
import com.penguinpush.cullergrader.config.AppConstants;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.event.*;

public class JGridMedia extends JLabel {

    public GridMedia gridMedia;
    private Dimension dimensions;
    private String labelText;
    private PhotoGridFrame photoGridFrame;
    private Photo thumbnailPhoto = new Photo(new File("placeholder.jpg"), 0, "");
    private ImageLoader imageLoader;
    private boolean isCurrentlyViewed = false;


    public JGridMedia(GridMedia gridMedia, ImageIcon placeholder, Dimension dimensions, ImageLoader imageLoader) {
        super(placeholder);
        this.gridMedia = gridMedia;
        this.dimensions = dimensions;
        this.imageLoader = imageLoader;

        initComponentProperties();
    }

    private void initComponentProperties() {
        this.setPreferredSize(dimensions);
        this.setToolTipText(gridMedia.getName() + ", " + gridMedia.getTooltip());

        SwingUtilities.invokeLater(() -> {
            if (gridMedia instanceof PhotoGroup) {
                photoGridFrame = ((GroupGridFrame) SwingUtilities.getWindowAncestor(this)).photoGridFrame;
            } else if (gridMedia instanceof Photo) {
                photoGridFrame = (PhotoGridFrame) SwingUtilities.getWindowAncestor(this);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (gridMedia instanceof PhotoGroup) {
                        photoGridFrame.updateGrid((PhotoGroup) gridMedia);
                    } else if (gridMedia instanceof Photo) {
                        photoGridFrame.setImagePanelPhoto((Photo) gridMedia);
                    }
                });
            }
        });
    }

    public void loadThumbnail(int priority) {
        if (gridMedia instanceof Photo) {
            thumbnailPhoto = (Photo) gridMedia;
        } else if (gridMedia instanceof PhotoGroup) {
            PhotoGroup photoGroup = (PhotoGroup) gridMedia;
            // Always use first photo in group for thumbnail
            thumbnailPhoto = photoGroup.getPhotos().isEmpty() ? null : photoGroup.getPhotos().get(0);
        }

        if (thumbnailPhoto != null) {
            imageLoader.loadImage(thumbnailPhoto, priority, false, (image) -> {
                SwingUtilities.invokeLater(() -> {
                    ImageIcon scaledIcon = PhotoUtils.getScaledIcon(image, dimensions.width, dimensions.height);
                    setIcon(scaledIcon);
                });
            });
        }
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
        repaint();
    }

    public String getLabelText() {
        return labelText;
    }

    public void updateBestTake() {
        if (gridMedia instanceof Photo) {
            Photo photo = (Photo) gridMedia;

            if (photo.isSelected()) {
                setLabelText(AppConstants.SELECTED_LABEL_TEXT);
            } else {
                setLabelText(null);
            }
        }
    }

    public Photo getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public void setCurrentlyViewed(boolean currentlyViewed) {
        this.isCurrentlyViewed = currentlyViewed;
        repaint();
    }

    public boolean isCurrentlyViewed() {
        return isCurrentlyViewed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Apply 50% opacity to groups with 0 selections
        if (gridMedia instanceof PhotoGroup) {
            PhotoGroup photoGroup = (PhotoGroup) gridMedia;
            if (photoGroup.getSelectedTakes().isEmpty()) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                super.paintComponent(g2d);
                g2d.dispose();
            } else {
                super.paintComponent(g);
            }
        } else {
            super.paintComponent(g);
        }

        updateBestTake();

        // Draw 3px red border if this is the currently viewed photo
        if (isCurrentlyViewed && gridMedia instanceof Photo) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
            g2d.dispose();
        }

        if (labelText != null) {
            Graphics2D graphics2d = (Graphics2D) g.create();
            drawLabelText(graphics2d, labelText);
            graphics2d.dispose();
        }
    }

    private void drawLabelText(Graphics2D graphics2d, String labelText) {
        float labelFontSize = AppConstants.GRIDMEDIA_LABEL_FONT_SIZE;
        int labelHeight = AppConstants.GRIDMEDIA_LABEL_HEIGHT;
        float labelOpacity = AppConstants.GRIDMEDIA_LABEL_OPACITY;

        Color textColor = AppConstants.GRIDMEDIA_LABEL_TEXT_COLOR;
        Color backgroundColor = AppConstants.GRIDMEDIA_LABEL_BACKGROUND_COLOR;

        graphics2d.setFont(graphics2d.getFont().deriveFont(labelFontSize)); // Set font size
        FontMetrics fontMetrics = graphics2d.getFontMetrics();
        String text = labelText;
        int textWidth = fontMetrics.stringWidth(text);
        int padding = (int) labelFontSize / 2;

        int x = getWidth() - (textWidth + padding);
        int y = getHeight() - (labelHeight - fontMetrics.getAscent());

        graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, labelOpacity));
        graphics2d.setColor(backgroundColor);
        graphics2d.fillRect(x - padding, getHeight() - labelHeight, getWidth(), labelHeight);

        graphics2d.setComposite(AlphaComposite.SrcOver);
        graphics2d.setColor(textColor);
        graphics2d.setFont(graphics2d.getFont().deriveFont(labelFontSize));

        graphics2d.drawString(text, x, y);
    }
}
