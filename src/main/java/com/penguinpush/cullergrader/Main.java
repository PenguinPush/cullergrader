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
        // Detect CLI mode by checking for CLI-specific arguments
        if (isCLIMode(args)) {
            // CLI mode - skip GUI initialization
            CLI cli = new CLI();
            int exitCode = cli.run(args);
            System.exit(exitCode);
        } else {
            // GUI mode
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

    /**
     * Determines if the application should run in CLI mode based on command-line arguments.
     *
     * @param args Command-line arguments
     * @return true if CLI mode should be used, false for GUI mode
     */
    private static boolean isCLIMode(String[] args) {
        if (args.length == 0) {
            return false;
        }

        // Check for CLI-specific flags
        return CLI.hasArgument(args, "--input") ||
               CLI.hasArgument(args, "-i") ||
               CLI.hasArgument(args, "--help") ||
               CLI.hasArgument(args, "-h");
    }
}
