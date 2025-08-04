package com.penguinpush.cullergrader.ui;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.logic.*;
import com.penguinpush.cullergrader.media.*;
import com.penguinpush.cullergrader.utils.Logger;

import javax.swing.*;
import java.util.List;
import java.io.File;
import java.util.Objects;

import static com.penguinpush.cullergrader.utils.Logger.logMessage;

public class GroupGridFrame extends JFrame {

    private static GroupGridFrame instance;
    List<PhotoGroup> photoGroups;
    public PhotoGridFrame photoGridFrame;
    private boolean needsRefresh = false;
    private ImageLoader imageLoader;
    private GroupingEngine groupingEngine;
    private File importDirectory;

    public GroupGridFrame(ImageLoader imageLoader, GroupingEngine groupingEngine) {
        instance = this; // set a static instance
        this.imageLoader = imageLoader;
        this.groupingEngine = groupingEngine;

        initComponents();
        jGridPanel.init(imageLoader);

        initComponentProperties();
        setVisible(true);
    }


    private void loadFrame(List<PhotoGroup> photoGroups) {
        int width = AppConstants.GRIDMEDIA_PHOTO_WIDTH;
        int height = AppConstants.GRIDMEDIA_PHOTO_HEIGHT;

        this.photoGroups = photoGroups;
        photoGridFrame = new PhotoGridFrame(photoGroups, this, imageLoader);

        jGridPanel.populateGrid((List<GridMedia>) (List<? extends GridMedia>) photoGroups, width, height, AppConstants.GROUP_OFFSCREEN_PRIORITY);
        jGridPanel.refreshGrid(); // refresh grid again, because i guess the one inside populateGrid() doesn't call...

        jReloadButton.setEnabled(true);
    }

    private void initComponentProperties() {
        jTimestampSpinner.setValue(AppConstants.TIME_THRESHOLD_SECONDS);
        jSimilaritySpinner.setValue(AppConstants.SIMILARITY_THRESHOLD_PERCENT);

        jGridPanel.getGridScrollPane().getViewport().addChangeListener(e -> {
            jGridPanel.updatePriorities(AppConstants.GROUP_ONSCREEN_PRIORITY, AppConstants.GROUP_OFFSCREEN_PRIORITY);
        });

        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                if (!needsRefresh) {
                    return;
                }

                jGridPanel.repopulateGrid(AppConstants.GROUP_OFFSCREEN_PRIORITY);
                needsRefresh = false;
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent evt) {

            }
        });
    }

    public void setNeedsRefresh() {
        this.needsRefresh = true;
    }

    public static void setInfoText(String infoText) {
        instance.jInfoTextLabel.setText(infoText);
    }

    public static void initializeLoggerCallback() {
        Logger.registerLogCallback(GroupGridFrame::setInfoText);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jGridPanel = new com.penguinpush.cullergrader.ui.components.JGridPanel();
        jTimestampSpinner = new javax.swing.JSpinner();
        jTimestampLabel = new javax.swing.JLabel();
        jSimilaritySpinner = new javax.swing.JSpinner();
        jSimilarityLabel = new javax.swing.JLabel();
        jInfoTextLabel = new javax.swing.JLabel();
        jReloadButton = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu = new javax.swing.JMenu();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemExport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cullergrader");

        jTimestampSpinner.setModel(new javax.swing.SpinnerNumberModel(15.0f, 0.0f, null, 1.0f));
        jTimestampSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(jTimestampSpinner, ""));

        jTimestampLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTimestampLabel.setText("Timestamp Threshold (s)");

        jSimilaritySpinner.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(35.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(1.0f)));
        jSimilaritySpinner.setEditor(new javax.swing.JSpinner.NumberEditor(jSimilaritySpinner, ""));

        jSimilarityLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jSimilarityLabel.setText("Similarity Threshold (%)");

        jInfoTextLabel.setText(" ");

        jReloadButton.setText("Reload Groups");
        jReloadButton.setEnabled(false);
        jReloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jReloadButtonActionPerformed(evt);
            }
        });

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
                        .addComponent(jGridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jInfoTextLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 336, Short.MAX_VALUE)
                                .addComponent(jTimestampLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTimestampSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSimilarityLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSimilaritySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jReloadButton)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jGridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSimilaritySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jSimilarityLabel)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTimestampSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTimestampLabel)
                                                .addComponent(jInfoTextLabel))
                                        .addComponent(jReloadButton))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        JFileChooser chooser = new JFileChooser(Objects.requireNonNullElseGet(importDirectory, () -> new File(AppConstants.DEFAULT_FOLDER_PATH)));
        chooser.setDialogTitle("Open Folder...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            long startTime = System.currentTimeMillis();

            importDirectory = chooser.getSelectedFile();
            List<PhotoGroup> groups = FileUtils.loadFolder(importDirectory, groupingEngine, (float) jTimestampSpinner.getValue(), (float) jSimilaritySpinner.getValue());
            loadFrame(groups);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null,
                        "Successfully opened folder: " + importDirectory.getAbsolutePath(),
                        "Import Complete!",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // this message isn't logged due to dependency reasons, but you can do the math yourself with the log timestamps so it's okay
                long endTime = System.currentTimeMillis();
                setInfoText("load completed in " + (endTime - startTime) + " ms");
            });
        }
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jReloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jReloadButtonActionPerformed
        long startTime = System.currentTimeMillis();

        List<PhotoGroup> groups = FileUtils.loadFolder(importDirectory, groupingEngine, (float) jTimestampSpinner.getValue(), (float) jSimilaritySpinner.getValue());
        loadFrame(groups);

        // same case as above, not logged but all good
        long endTime = System.currentTimeMillis();
        setInfoText("load completed in " + (endTime - startTime) + " ms");
    }//GEN-LAST:event_jReloadButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.penguinpush.cullergrader.ui.components.JGridPanel jGridPanel;
    private javax.swing.JLabel jInfoTextLabel;
    private javax.swing.JMenu jMenu;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItemExport;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JButton jReloadButton;
    private javax.swing.JLabel jSimilarityLabel;
    private javax.swing.JSpinner jSimilaritySpinner;
    private javax.swing.JLabel jTimestampLabel;
    private javax.swing.JSpinner jTimestampSpinner;
    // End of variables declaration//GEN-END:variables
}
