package com.penguinpush.cullergrader;

import com.penguinpush.cullergrader.logic.*;
import com.penguinpush.cullergrader.media.*;
import com.penguinpush.cullergrader.config.AppConstants;

import java.io.File;
import java.util.List;

/**
 * Command-line interface for Cullergrader.
 * Provides photo grouping and export functionality without launching the GUI.
 */
public class CLI {

    // Exit codes
    private static final int EXIT_SUCCESS = 0;
    private static final int EXIT_FAILURE = 1;

    // Parsed arguments with defaults from AppConstants
    private String inputPath = null;
    private String outputPath = null;
    private String jsonPath = null;
    private float timeThreshold = AppConstants.TIME_THRESHOLD_SECONDS;
    private float similarityThreshold = AppConstants.SIMILARITY_THRESHOLD_PERCENT;

    /**
     * Main entry point for CLI mode.
     *
     * @param args Command-line arguments
     * @return Exit code (0 = success, 1 = failure)
     */
    public int run(String[] args) {
        // Handle --help first
        if (hasArgument(args, "--help") || hasArgument(args, "-h")) {
            printHelp();
            return EXIT_SUCCESS;
        }

        // Parse arguments
        if (!parseArguments(args)) {
            System.err.println("Error: Invalid arguments. Use --help for usage information.");
            return EXIT_FAILURE;
        }

        // Validate required arguments
        if (inputPath == null) {
            System.err.println("Error: --input is required.");
            printHelp();
            return EXIT_FAILURE;
        }

        // Validate input directory
        File inputFolder = new File(inputPath);
        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            System.err.println("Error: Input directory does not exist: " + inputPath);
            return EXIT_FAILURE;
        }

        if (!inputFolder.canRead()) {
            System.err.println("Error: Cannot read input directory: " + inputPath);
            return EXIT_FAILURE;
        }

        // Validate output directory if provided
        File outputFolder = null;
        if (outputPath != null) {
            outputFolder = new File(outputPath);
            if (outputFolder.exists() && !outputFolder.isDirectory()) {
                System.err.println("Error: Output path exists but is not a directory: " + outputPath);
                return EXIT_FAILURE;
            }

            if (outputFolder.exists() && !outputFolder.canWrite()) {
                System.err.println("Error: Cannot write to output directory: " + outputPath);
                return EXIT_FAILURE;
            }
        }

        // Execute workflow
        try {
            executeWorkflow(inputFolder, outputFolder);
            return EXIT_SUCCESS;
        } catch (Exception e) {
            System.err.println("Error: Processing failed - " + e.getMessage());
            e.printStackTrace();
            return EXIT_FAILURE;
        }
    }

    /**
     * Parses command-line arguments.
     *
     * @param args Command-line arguments
     * @return true if parsing succeeded, false on error
     */
    private boolean parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            // Input path
            if (arg.equals("--input") || arg.equals("-i")) {
                if (i + 1 >= args.length) {
                    System.err.println("Error: --input requires a value");
                    return false;
                }
                inputPath = args[++i];
            }
            // Output path
            else if (arg.equals("--output") || arg.equals("-o")) {
                if (i + 1 >= args.length) {
                    System.err.println("Error: --output requires a value");
                    return false;
                }
                outputPath = args[++i];
            }
            // JSON export path
            else if (arg.equals("--json") || arg.equals("-j")) {
                if (i + 1 >= args.length) {
                    System.err.println("Error: --json requires a value");
                    return false;
                }
                jsonPath = args[++i];
            }
            // Time threshold
            else if (arg.equals("--time") || arg.equals("-t")) {
                if (i + 1 >= args.length) {
                    System.err.println("Error: --time requires a value");
                    return false;
                }
                try {
                    timeThreshold = Float.parseFloat(args[++i]);
                    if (timeThreshold <= 0) {
                        System.err.println("Error: Time threshold must be positive");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error: Invalid time threshold value: " + args[i]);
                    return false;
                }
            }
            // Similarity threshold
            else if (arg.equals("--similarity") || arg.equals("-s")) {
                if (i + 1 >= args.length) {
                    System.err.println("Error: --similarity requires a value");
                    return false;
                }
                try {
                    similarityThreshold = Float.parseFloat(args[++i]);
                    if (similarityThreshold < 0 || similarityThreshold > 100) {
                        System.err.println("Error: Similarity threshold must be 0-100");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error: Invalid similarity threshold value: " + args[i]);
                    return false;
                }
            }
            // Skip --help and -h (handled in run method)
            else if (arg.equals("--help") || arg.equals("-h")) {
                // Already handled in run(), just skip
            }
            // Unknown argument
            else if (arg.startsWith("-")) {
                System.err.println("Error: Unknown argument: " + arg);
                return false;
            }
        }

        return true;
    }

    /**
     * Executes the main CLI workflow: load photos, generate groups, and export.
     *
     * @param inputFolder Input directory containing photos
     * @param outputFolder Output directory for best takes
     */
    private void executeWorkflow(File inputFolder, File outputFolder) {
        long startTime = System.currentTimeMillis();
        boolean previewMode = (outputFolder == null);

        // Print configuration header
        System.out.println("Cullergrader CLI");
        System.out.println("================");
        System.out.println("Input:  " + inputFolder.getAbsolutePath());
        if (!previewMode) {
            System.out.println("Output: " + outputFolder.getAbsolutePath());
        } else {
            System.out.println("Mode: Preview (no files will be exported)");
        }
        System.out.println("Time threshold: " + timeThreshold + " seconds");
        System.out.println("Similarity threshold: " + similarityThreshold + "%");
        System.out.println();

        // Load and hash photos
        System.out.println("Loading and hashing photos from: " + inputFolder.getAbsolutePath());
        GroupingEngine engine = new GroupingEngine();
        List<Photo> photos = engine.photoListFromFolder(inputFolder);

        if (photos.isEmpty()) {
            System.out.println("No photos found in input directory.");
            return;
        }

        System.out.println("Found " + photos.size() + " photos");
        System.out.println();

        // Generate groups
        System.out.println("Generating groups with thresholds: " + timeThreshold + "s time, " + similarityThreshold + "% similarity");
        List<PhotoGroup> groups = engine.generateGroups(photos, timeThreshold, similarityThreshold);

        System.out.println("Created " + groups.size() + " groups from " + photos.size() + " photos");
        System.out.println();

        // Export JSON if requested
        if (jsonPath != null) {
            File jsonFile = new File(jsonPath);
            System.out.println("Exporting group information to: " + jsonFile.getAbsolutePath());
            FileUtils.exportGroupsJson(groups, jsonFile, timeThreshold, similarityThreshold);
            System.out.println();
        }

        // Export or preview
        if (previewMode) {
            System.out.println("Preview - Best takes that would be exported:");
            System.out.println("--------------------------------------------");
            for (int i = 0; i < groups.size(); i++) {
                PhotoGroup group = groups.get(i);
                Photo bestTake = group.getBestTake();
                if (bestTake != null) {
                    System.out.println("[Group " + i + "] " + bestTake.getFile().getName());
                }
            }
            System.out.println();
            System.out.println("To export these " + groups.size() + " files, run again with --output <path>");
        } else {
            System.out.println("Exporting best takes to: " + outputFolder.getAbsolutePath());
            FileUtils.exportBestTakes(groups, outputFolder);
            System.out.println();
            System.out.println("Successfully exported " + groups.size() + " files");
        }

        // Summary
        long endTime = System.currentTimeMillis();
        long durationMs = endTime - startTime;
        double durationSec = durationMs / 1000.0;
        System.out.println();
        System.out.println("Processing completed in " + String.format("%.2f", durationSec) + " seconds");
    }

    /**
     * Prints help message showing usage and available options.
     */
    private void printHelp() {
        System.out.println("Cullergrader CLI - Photo grouping and export tool");
        System.out.println();
        System.out.println("USAGE:");
        System.out.println("  java -jar cullergrader.jar [OPTIONS]");
        System.out.println();
        System.out.println("  No arguments launches GUI mode");
        System.out.println();
        System.out.println("OPTIONS:");
        System.out.println("  -i, --input <path>         Input folder containing photos (required)");
        System.out.println("  -o, --output <path>        Output folder for best takes (optional, preview mode if omitted)");
        System.out.println("  -j, --json <path>          Export group information to JSON file (optional)");
        System.out.println("  -t, --time <seconds>       Time threshold in seconds (default: " + AppConstants.TIME_THRESHOLD_SECONDS + ")");
        System.out.println("  -s, --similarity <percent> Similarity threshold 0-100 (default: " + AppConstants.SIMILARITY_THRESHOLD_PERCENT + ")");
        System.out.println("  -h, --help                 Show this help message");
        System.out.println();
        System.out.println("EXAMPLES:");
        System.out.println("  # Preview mode (no export)");
        System.out.println("  java -jar cullergrader.jar --input /photos");
        System.out.println();
        System.out.println("  # Export mode");
        System.out.println("  java -jar cullergrader.jar --input /photos --output /export");
        System.out.println();
        System.out.println("  # Export JSON metadata only");
        System.out.println("  java -jar cullergrader.jar --input /photos --json groups.json");
        System.out.println();
        System.out.println("  # Export both files and JSON");
        System.out.println("  java -jar cullergrader.jar -i /photos -o /export --json /export/groups.json");
        System.out.println();
        System.out.println("  # Custom thresholds with export");
        System.out.println("  java -jar cullergrader.jar -i /photos -o /export -t 10 -s 40");
        System.out.println();
    }

    /**
     * Helper method to check if a specific flag is present in arguments.
     * Used by Main.java to detect CLI mode.
     *
     * @param args Command-line arguments
     * @param flag Flag to search for
     * @return true if flag is present, false otherwise
     */
    public static boolean hasArgument(String[] args, String flag) {
        for (String arg : args) {
            if (arg.equals(flag)) {
                return true;
            }
        }
        return false;
    }
}
