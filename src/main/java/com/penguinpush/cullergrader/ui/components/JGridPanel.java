package com.penguinpush.cullergrader.ui.components;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.logic.ImageLoader;
import com.penguinpush.cullergrader.media.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class JGridPanel extends JPanel {

    boolean singleRow = false;
    private ImageLoader imageLoader;

    public JGridPanel() {
        super();
    }
    
    public void init(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        initComponents();
        initComponentProperties();
    }

    public void setSingleRow(boolean singleRow) {
        this.singleRow = singleRow;
        initComponentProperties();
    }

    public void setHorizontalScrollBarPolicy(int policy) {
        gridScrollPane.setHorizontalScrollBarPolicy(policy);
    }

    public void setVerticalScrollBarPolicy(int policy) {
        gridScrollPane.setVerticalScrollBarPolicy(policy);
    }

    public void populateGrid(List<GridMedia> gridMedias, int width, int height, int priority) {
        gridPanel.removeAll();

        ImageIcon placeholderIcon = new ImageIcon(AppConstants.PLACEHOLDER_THUMBNAIL_PATH);

        for (GridMedia gridMedia : gridMedias) {
            JGridMedia jGridMedia = new JGridMedia(gridMedia, placeholderIcon, new Dimension(width, height), imageLoader);
            gridPanel.add(jGridMedia);

            if (gridMedia instanceof PhotoGroup) {
                jGridMedia.setLabelText(Integer.toString(gridMedia.getSize()));
            }

            // load real thumbnail in background
            SwingUtilities.invokeLater(() -> jGridMedia.loadThumbnail(priority));
        }

        SwingUtilities.invokeLater(() -> refreshGrid());
    }

    public void repopulateGrid(int priority) {
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JGridMedia) {
                JGridMedia jGridMedia = (JGridMedia) component;

                if (jGridMedia.gridMedia instanceof PhotoGroup) {
                    SwingUtilities.invokeLater(() -> jGridMedia.loadThumbnail(priority));
                }
            }
        }
    }

    public void updatePriorities(int ONSCREEN_PRIORITY, int OFFSCREEN_PRIORITY) {
        Rectangle visibleRect = gridPanel.getVisibleRect();

        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JGridMedia) {
                JGridMedia jGridMedia = (JGridMedia) component;
                Rectangle mediaBounds = jGridMedia.getBounds();

                int priority;
                if (visibleRect.intersects(mediaBounds)) {
                    priority = ONSCREEN_PRIORITY;
                } else {
                    priority = OFFSCREEN_PRIORITY;
                }

                if (jGridMedia.gridMedia instanceof Photo) {
                    Photo photo = (Photo) jGridMedia.gridMedia;
                    imageLoader.updatePriority(photo, priority);
                } else if (jGridMedia.gridMedia instanceof PhotoGroup) {
                    PhotoGroup photoGroup = (PhotoGroup) jGridMedia.gridMedia;
                    // Update priority for first photo in group (thumbnail)
                    if (!photoGroup.getPhotos().isEmpty()) {
                        Photo firstPhoto = photoGroup.getPhotos().get(0);
                        imageLoader.updatePriority(firstPhoto, priority);
                    }
                }
            }
        }
    }

    public void setCurrentlyViewedPhoto(Photo currentPhoto) {
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JGridMedia) {
                JGridMedia jGridMedia = (JGridMedia) component;
                // Set border on the matching photo thumbnail
                boolean isCurrentlyViewed = jGridMedia.gridMedia instanceof Photo
                        && jGridMedia.gridMedia == currentPhoto;
                jGridMedia.setCurrentlyViewed(isCurrentlyViewed);
            }
        }
    }

    private void initComponentProperties() {
        gridPanel.setLayout(new WrapLayout(FlowLayout.LEFT, singleRow));

        gridScrollPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    refreshGrid();
                });
            }
        });

        JScrollBar verticalScrollBar = gridScrollPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = gridScrollPane.getHorizontalScrollBar();

        verticalScrollBar.setUnitIncrement(AppConstants.SCROLL_BAR_INCREMENT);
        horizontalScrollBar.setUnitIncrement(AppConstants.SCROLL_BAR_INCREMENT);
    }

    public void refreshGrid() {
        Dimension preferredSize = gridPanel.getLayout().preferredLayoutSize(gridPanel);
        gridPanel.setPreferredSize(preferredSize);
        gridScrollPane.revalidate();
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public JScrollPane getGridScrollPane() {
        return gridScrollPane;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gridScrollPane = new javax.swing.JScrollPane();
        gridPanel = new javax.swing.JPanel();

        gridScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        gridPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout gridPanelLayout = new javax.swing.GroupLayout(gridPanel);
        gridPanel.setLayout(gridPanelLayout);
        gridPanelLayout.setHorizontalGroup(
                gridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 800, Short.MAX_VALUE)
        );
        gridPanelLayout.setVerticalGroup(
                gridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE)
        );

        gridScrollPane.setViewportView(gridPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(gridScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(gridScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel gridPanel;
    private javax.swing.JScrollPane gridScrollPane;
    // End of variables declaration//GEN-END:variables
}
