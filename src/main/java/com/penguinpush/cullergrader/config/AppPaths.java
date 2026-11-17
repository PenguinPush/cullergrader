package com.penguinpush.cullergrader.config;

import java.io.File;

/**
 * Resolves where Cullergrader keeps its per-user data (config, hash cache, logs, saved sessions),
 * so the app behaves the same no matter which directory it's launched from.
 *
 * Default locations:
 *   macOS:   ~/Library/Application Support/Cullergrader
 *   Windows: %APPDATA%\Cullergrader
 *   Linux:   $XDG_CONFIG_HOME/cullergrader (usually ~/.config/cullergrader)
 *
 * Set the CULLERGRADER_HOME environment variable (or the cullergrader.home system property)
 * to use a different folder, e.g. for a portable install.
 *
 * Note: this class must not use Logger, since Logger depends on AppConstants, which depends on this.
 */
public final class AppPaths {

    private static final String APP_NAME = "Cullergrader";
    private static final File DATA_DIR = resolveDataDir();

    private AppPaths() {
    }

    private static File resolveDataDir() {
        File dir = getOverrideDir();

        if (dir == null) {
            String os = System.getProperty("os.name", "").toLowerCase();
            String home = System.getProperty("user.home");

            if (os.contains("win")) {
                String appData = System.getenv("APPDATA");
                File base = isBlank(appData) ? new File(home, "AppData" + File.separator + "Roaming") : new File(appData);
                dir = new File(base, APP_NAME);
            } else if (os.contains("mac")) {
                dir = new File(home, "Library" + File.separator + "Application Support" + File.separator + APP_NAME);
            } else {
                String xdgConfig = System.getenv("XDG_CONFIG_HOME");
                File base = isBlank(xdgConfig) ? new File(home, ".config") : new File(xdgConfig);
                dir = new File(base, APP_NAME.toLowerCase());
            }
        }

        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("couldn't create data directory: " + dir.getAbsolutePath());
        }
        return dir;
    }

    private static File getOverrideDir() {
        String override = System.getProperty("cullergrader.home");
        if (isBlank(override)) {
            override = System.getenv("CULLERGRADER_HOME");
        }
        return isBlank(override) ? null : new File(override).getAbsoluteFile();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static File getDataDir() {
        return DATA_DIR;
    }

    /**
     * Resolves a path from config: absolute paths are used as-is, relative paths are placed inside the data directory.
     */
    public static File resolve(String path) {
        File file = new File(path);
        return file.isAbsolute() ? file : new File(DATA_DIR, path);
    }

    public static File getConfigFile() {
        return new File(DATA_DIR, "config.json");
    }

    public static File getSessionsDir() {
        return new File(DATA_DIR, "sessions");
    }
}
