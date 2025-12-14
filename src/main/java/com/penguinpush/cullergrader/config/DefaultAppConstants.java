package com.penguinpush.cullergrader.config;

public class DefaultAppConstants {
    public boolean DARK_THEME = true;
    public String CACHE_FILE = "cache.json";
    public String LOG_DIRECTORY = "logs";
    public String DEFAULT_FOLDER_PATH = "";

    public boolean HASHING_ENABLED = true;
    public int EXECUTOR_TIMEOUT_MINUTES = 60;
    public int MAX_THREADS_RATIO = 2;
    public int HASHED_WIDTH = 8;
    public int HASHED_HEIGHT = 8;
    public float TIME_THRESHOLD_SECONDS = 15;
    public float SIMILARITY_THRESHOLD_PERCENT = 45;
    public String DEFAULT_SELECTION_STRATEGY = "first";

    public int MAX_PRIORITY = 0;
    public int IMAGE_PRIORITY = 1;
    public int PHOTO_ONSCREEN_PRIORITY = 2;
    public int GROUP_ONSCREEN_PRIORITY = 3;
    public int GROUP_OFFSCREEN_PRIORITY = 4;
    public int PHOTO_OFFSCREEN_PRIORITY = 4;

    public int THUMBNAIL_ICON_WIDTH = 240;
    public int THUMBNAIL_ICON_HEIGHT = 160;

    public int GRIDMEDIA_PHOTO_WIDTH = 120;
    public int GRIDMEDIA_PHOTO_HEIGHT = 80;
    public int GRIDMEDIA_GROUP_WIDTH = 120;
    public int GRIDMEDIA_GROUP_HEIGHT = 80;
    public int GRIDMEDIA_VGAP = 10;
    public int GRIDMEDIA_HGAP_MIN = 10;

    public int SCROLL_BAR_INCREMENT = 20;
    public String PLACEHOLDER_THUMBNAIL_PATH = "placeholder.jpg";

    public float GRIDMEDIA_LABEL_FONT_SIZE = 14f;
    public int GRIDMEDIA_LABEL_HEIGHT = 20;
    public float GRIDMEDIA_LABEL_OPACITY = 0.7f;
    public String GRIDMEDIA_LABEL_TEXT_COLOR = "#FFFFFF";
    public String GRIDMEDIA_LABEL_BACKGROUND_COLOR = "#000000";

    public String KEYBIND_PHOTO_PREVIOUS = "LEFT";
    public String KEYBIND_PHOTO_NEXT = "RIGHT";
    public String KEYBIND_GROUP_PREVIOUS = "UP";
    public String KEYBIND_GROUP_NEXT = "DOWN";
    public String KEYBIND_TOGGLE_SELECTION = "SPACE";

    public String SELECTED_LABEL_TEXT = "★";
}
