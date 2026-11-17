package com.penguinpush.cullergrader.ui;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.logic.ImageLoader;
import com.penguinpush.cullergrader.media.*;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;
import java.awt.event.*;

public class PhotoGridFrame extends JFrame {

    public PhotoGroup photoGroup;
    List<PhotoGroup> photoGroups;
    private GroupGridFrame groupGridFrame;
    private final Map<Photo, BufferedImage> thumbnailCache = new ConcurrentHashMap<>();
    private ImageLoader imageLoader;

    public PhotoGridFrame(List<PhotoGroup> photoGroups, GroupGridFrame groupGridFrame, ImageLoader imageLoader) {
        this.photoGroups = photoGroups;
        this.groupGridFrame = groupGridFrame;
        this.imageLoader = imageLoader;

        initComponents();
        initComponentProperties();
    }

    public void updateGrid(PhotoGroup photoGroup) {
        int width = AppConstants.GRIDMEDIA_PHOTO_WIDTH;
        int height = AppConstants.GRIDMEDIA_PHOTO_HEIGHT;

        this.photoGroup = photoGroup;
        jGridPanel.populateGrid((List<GridMedia>) (List<? extends GridMedia>) photoGroup.getPhotos(), width, height, AppConstants.PHOTO_OFFSCREEN_PRIORITY);
        jGridPanel.updatePriorities(AppConstants.PHOTO_ONSCREEN_PRIORITY, AppConstants.PHOTO_OFFSCREEN_PRIORITY);

        // Get first selected photo (or first photo if none selected)
        Photo photoToShow = photoGroup.getSelectedTakes().isEmpty()
            ? (photoGroup.getPhotos().isEmpty() ? null : photoGroup.getPhotos().get(0))
            : photoGroup.getSelectedTakes().iterator().next();

        if (photoToShow != null) {
            setImagePanelPhoto(photoToShow);
        }
        setVisible(true);
    }

    private void initComponentProperties() {
        jGridPanel.init(imageLoader);
        jImagePanel.init(imageLoader);
        
        jGridPanel.getGridScrollPane().getViewport().addChangeListener(e -> {
            jGridPanel.updatePriorities(AppConstants.PHOTO_ONSCREEN_PRIORITY, AppConstants.PHOTO_OFFSCREEN_PRIORITY);
        });

        jGridPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jGridPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jGridPanel.setSingleRow(true);
    }

    // Action methods
    private void previousPhoto() {
        Photo photo = jImagePanel.getPhoto();

        if (photo.getIndex() > 0) {
            Photo photo_new = photoGroup.getPhotos().get(photo.getIndex() - 1);
            setImagePanelPhoto(photo_new);
        }
    }

    private void nextPhoto() {
        Photo photo = jImagePanel.getPhoto();

         if (photo.getIndex() < photoGroup.getSize() - 1) {
            Photo photo_new = photoGroup.getPhotos().get(photo.getIndex() + 1);
            setImagePanelPhoto(photo_new);
        }
    }

    private void previousGroup() {
        if (photoGroup.getIndex() > 0) {
            PhotoGroup photoGroup_new = photoGroups.get(photoGroup.getIndex() - 1);
            updateGrid(photoGroup_new);
        }
    }

    private void nextGroup() {
        if (photoGroup.getIndex() < photoGroups.size() - 1) {
            PhotoGroup photoGroup_new = photoGroups.get(photoGroup.getIndex() + 1);
            updateGrid(photoGroup_new);
        }
    }

    private void toggleSelection() {
        Photo photo = jImagePanel.getPhoto();

        if (photoGroup.getIndex() <= photoGroups.size() - 1) {
            photoGroup.toggleSelection(photo);
            groupGridFrame.setNeedsRefresh();
            jGridPanel.repaint(); // Repaint photo grid thumbnails
            groupGridFrame.repaint(); // Repaint group grid to update opacity
            repaint();
        }
    }

    // Keep for compatibility with generated menu code
    private void setBestTake() {
        toggleSelection();
    }

    public void setImagePanelPhoto(Photo photo) {
        if (thumbnailCache.containsKey(photo)) {
            jImagePanel.setImage(thumbnailCache.get(photo));
        } else {
            SwingUtilities.invokeLater(() -> jImagePanel.setPhoto(photo));
        }

        // Update border highlighting for currently viewed photo
        jGridPanel.setCurrentlyViewedPhoto(photo);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jImagePanel = new com.penguinpush.cullergrader.ui.components.JImagePanel();
        jGridPanel = new com.penguinpush.cullergrader.ui.components.JGridPanel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu = new javax.swing.JMenu();
        jMenuPreviousPhoto = new javax.swing.JMenuItem();
        jMenuNextPhoto = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuPreviousGroup = new javax.swing.JMenuItem();
        jMenuNextGroup = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuSetBestTake = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cullergrader");

        jMenu.setText("Controls");

        jMenuPreviousPhoto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, 0));
        jMenuPreviousPhoto.setText("Previous Photo");
        jMenuPreviousPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPreviousPhotoActionPerformed(evt);
            }
        });
        jMenu.add(jMenuPreviousPhoto);

        jMenuNextPhoto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, 0));
        jMenuNextPhoto.setText("Next Photo");
        jMenuNextPhoto.setToolTipText("");
        jMenuNextPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNextPhotoActionPerformed(evt);
            }
        });
        jMenu.add(jMenuNextPhoto);
        jMenu.add(jSeparator1);

        jMenuPreviousGroup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0));
        jMenuPreviousGroup.setText("Previous Group");
        jMenuPreviousGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPreviousGroupActionPerformed(evt);
            }
        });
        jMenu.add(jMenuPreviousGroup);

        jMenuNextGroup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0));
        jMenuNextGroup.setText("Next Group");
        jMenuNextGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNextGroupActionPerformed(evt);
            }
        });
        jMenu.add(jMenuNextGroup);
        jMenu.add(jSeparator2);

        jMenuSetBestTake.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0));
        jMenuSetBestTake.setText("Set Best Take");
        jMenuSetBestTake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSetBestTakeActionPerformed(evt);
            }
        });
        jMenu.add(jMenuSetBestTake);

        jMenuBar.add(jMenu);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jGridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jGridPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuPreviousPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPreviousPhotoActionPerformed
        previousPhoto();
    }//GEN-LAST:event_jMenuPreviousPhotoActionPerformed

    private void jMenuNextPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNextPhotoActionPerformed
        nextPhoto();
    }//GEN-LAST:event_jMenuNextPhotoActionPerformed

    private void jMenuPreviousGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPreviousGroupActionPerformed
        previousGroup();
    }//GEN-LAST:event_jMenuPreviousGroupActionPerformed

    private void jMenuNextGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNextGroupActionPerformed
        nextGroup();
    }//GEN-LAST:event_jMenuNextGroupActionPerformed

    private void jMenuSetBestTakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSetBestTakeActionPerformed
        setBestTake();
    }//GEN-LAST:event_jMenuSetBestTakeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.penguinpush.cullergrader.ui.components.JGridPanel jGridPanel;
    private com.penguinpush.cullergrader.ui.components.JImagePanel jImagePanel;
    private javax.swing.JMenu jMenu;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuNextGroup;
    private javax.swing.JMenuItem jMenuNextPhoto;
    private javax.swing.JMenuItem jMenuPreviousGroup;
    private javax.swing.JMenuItem jMenuPreviousPhoto;
    private javax.swing.JMenuItem jMenuSetBestTake;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
