package com.penguinpush.cullergrader;

import com.penguinpush.cullergrader.logic.*;
import com.penguinpush.cullergrader.ui.GroupGridFrame;
import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.media.PhotoUtils;
import javax.swing.SwingUtilities;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {

    public static void main(String[] args) {
        // load theme
        if (AppConstants.DARK_THEME) {
            FlatDarculaLaf.setup();
        } else {
            FlatIntelliJLaf.setup();
        }

        GroupingEngine groupingEngine = new GroupingEngine();
        ImageLoader imageLoader = new ImageLoader();

        SwingUtilities.invokeLater(() -> new GroupGridFrame(imageLoader, groupingEngine));
        GroupGridFrame.initializeLoggerCallback();
    }
}
