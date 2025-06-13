package com.penguinpush.cullergrader.ui;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.logic.*;
import com.penguinpush.cullergrader.media.*;

import javax.swing.*;
import java.util.List;
import java.io.File;

public class GroupGridFrame extends JFrame {

    List<PhotoGroup> photoGroups;
    public PhotoGridFrame photoGridFrame;
    private boolean needsRefresh = false;
    private ImageLoader imageLoader;
    private GroupingEngine groupingEngine;

    public GroupGridFrame(List<PhotoGroup> photoGroups, ImageLoader imageLoader, GroupingEngine groupingEngine) {
        this.imageLoader = imageLoader;
        this.groupingEngine = groupingEngine;

        initComponents();
        jGridPanel.init(imageLoader);
        
        loadFrame(photoGroups);
    }

    private void loadFrame(List<PhotoGroup> photoGroups) {
        this.photoGroups = photoGroups;
        photoGridFrame = new PhotoGridFrame(photoGroups, this, imageLoader);

        initComponentProperties();
        setVisible(true);
    }

    private void initComponentProperties() {
        int width = AppConstants.GRIDMEDIA_PHOTO_WIDTH;
        int height = AppConstants.GRIDMEDIA_PHOTO_HEIGHT;

        jGridPanel.populateGrid((List<GridMedia>) (List<? extends GridMedia>) photoGroups, width, height, AppConstants.GROUP_OFFSCREEN_PRIORITY);

        jGridPanel.getGridScrollPane().getViewport().addChangeListener(e -> {
            jGridPanel.updatePriorities(AppConstants.GROUP_ONSCREEN_PRIORITY, AppConstants.GROUP_OFFSCREEN_PRIORITY);
        });

        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                if (!needsRefresh) {
                    return;
                }

                jGridPanel.repopulateGrid(AppConstants.GROUP_OFFSCREEN_PRIORITY);
                needsRefresh = false;
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {

            }
        });
    }

    public void setNeedsRefresh() {
        this.needsRefresh = true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jGridPanel = new com.penguinpush.cullergrader.ui.components.JGridPanel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu = new javax.swing.JMenu();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemExport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cullergrader");

        jMenu.setText("File");

        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemOpen.setText("Open Folder");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenu.add(jMenuItemOpen);

        jMenuItemExport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemExport.setText("Export Best Takes");
        jMenuItemExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExportActionPerformed(evt);
            }
        });
        jMenu.add(jMenuItemExport);

        jMenuBar.add(jMenu);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jGridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jGridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        JFileChooser chooser = new JFileChooser(new File(AppConstants.DEFAULT_FOLDER_PATH));
        chooser.setDialogTitle("Open Folder...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File importDirectory = chooser.getSelectedFile();
            List<PhotoGroup> groups = FileUtils.loadFolder(importDirectory, groupingEngine);
            loadFrame(groups);
            
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null,
                        "Sucessfully opened folder: " + importDirectory.getAbsolutePath(),
                        "Import Complete!",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });
        }
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jMenuItemExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExportActionPerformed
        JFileChooser chooser = new JFileChooser(AppConstants.DEFAULT_FOLDER_PATH);
        chooser.setDialogTitle("Export To...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File exportDirectory = chooser.getSelectedFile();
            FileUtils.exportBestTakes(photoGroups, exportDirectory);
            
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null,
                        "Export successful to path: " + exportDirectory.getAbsolutePath(),
                        "Export Complete!",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });
        }
    }//GEN-LAST:event_jMenuItemExportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.penguinpush.cullergrader.ui.components.JGridPanel jGridPanel;
    private javax.swing.JMenu jMenu;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItemExport;
    private javax.swing.JMenuItem jMenuItemOpen;
    // End of variables declaration//GEN-END:variables
}
