package com.penguinpush.cullergrader.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;

public class AppConstants {
    private static final DefaultAppConstants config;

    static {
        DefaultAppConstants loaded = new DefaultAppConstants();
        try {
            Gson gson = new GsonBuilder().create();
            loaded = gson.fromJson(new FileReader("config.json"), DefaultAppConstants.class);
            System.out.println("loaded config.json");

        } catch (IOException e) {
            System.err.println("couldn't load config, using default values");
        }
        config = loaded;
    }

    public static final boolean DARK_THEME = config.DARK_THEME;
    public static final String CACHE_FILE = config.CACHE_FILE;
    public static final String LOG_DIRECTORY = config.LOG_DIRECTORY;
    public static final String DEFAULT_FOLDER_PATH = config.DEFAULT_FOLDER_PATH;

    public static final boolean HASHING_ENABLED = config.HASHING_ENABLED;
    public static final int EXECUTOR_TIMEOUT_MINUTES = config.EXECUTOR_TIMEOUT_MINUTES;
    public static final int MAX_THREADS_RATIO = config.MAX_THREADS_RATIO;
    public static final int HASHED_WIDTH = config.HASHED_WIDTH;
    public static final int HASHED_HEIGHT = config.HASHED_HEIGHT;
    public static final float TIME_THRESHOLD_SECONDS = config.TIME_THRESHOLD_SECONDS;
    public static final float SIMILARITY_THRESHOLD_PERCENT = config.SIMILARITY_THRESHOLD_PERCENT;

    public static final int MAX_PRIORITY = config.MAX_PRIORITY;
    public static final int IMAGE_PRIORITY = config.IMAGE_PRIORITY;
    public static final int PHOTO_ONSCREEN_PRIORITY = config.PHOTO_ONSCREEN_PRIORITY;
    public static final int GROUP_ONSCREEN_PRIORITY = config.GROUP_ONSCREEN_PRIORITY;
    public static final int GROUP_OFFSCREEN_PRIORITY = config.GROUP_OFFSCREEN_PRIORITY;
    public static final int PHOTO_OFFSCREEN_PRIORITY = config.PHOTO_OFFSCREEN_PRIORITY;

    public static final int THUMBNAIL_ICON_WIDTH = config.THUMBNAIL_ICON_WIDTH;
    public static final int THUMBNAIL_ICON_HEIGHT = config.THUMBNAIL_ICON_HEIGHT;

    public static final int GRIDMEDIA_PHOTO_WIDTH = config.GRIDMEDIA_PHOTO_WIDTH;
    public static final int GRIDMEDIA_PHOTO_HEIGHT = config.GRIDMEDIA_PHOTO_HEIGHT;
    public static final int GRIDMEDIA_GROUP_WIDTH = config.GRIDMEDIA_GROUP_WIDTH;
    public static final int GRIDMEDIA_GROUP_HEIGHT = config.GRIDMEDIA_GROUP_HEIGHT;
    public static final int GRIDMEDIA_VGAP = config.GRIDMEDIA_VGAP;
    public static final int GRIDMEDIA_HGAP_MIN = config.GRIDMEDIA_HGAP_MIN;

    public static final int SCROLL_BAR_INCREMENT = config.SCROLL_BAR_INCREMENT;
    public static final String PLACEHOLDER_THUMBNAIL_PATH = config.PLACEHOLDER_THUMBNAIL_PATH;

    public static final float GRIDMEDIA_LABEL_FONT_SIZE = config.GRIDMEDIA_LABEL_FONT_SIZE;
    public static final int GRIDMEDIA_LABEL_HEIGHT = config.GRIDMEDIA_LABEL_HEIGHT;
    public static final float GRIDMEDIA_LABEL_OPACITY = config.GRIDMEDIA_LABEL_OPACITY;
    public static final Color GRIDMEDIA_LABEL_TEXT_COLOR = Color.decode(config.GRIDMEDIA_LABEL_TEXT_COLOR);
    public static final Color GRIDMEDIA_LABEL_BACKGROUND_COLOR = Color.decode(config.GRIDMEDIA_LABEL_BACKGROUND_COLOR);

    public static final String KEYBIND_PHOTO_PREVIOUS = config.KEYBIND_PHOTO_PREVIOUS;
    public static final String KEYBIND_PHOTO_NEXT = config.KEYBIND_PHOTO_NEXT;
    public static final String KEYBIND_GROUP_PREVIOUS = config.KEYBIND_GROUP_PREVIOUS;
    public static final String KEYBIND_GROUP_NEXT = config.KEYBIND_GROUP_NEXT;
    public static final String KEYBIND_SET_BESTTAKE = config.KEYBIND_SET_BESTTAKE;

    public static final String BESTTAKE_LABEL_TEXT = config.BESTTAKE_LABEL_TEXT;
}