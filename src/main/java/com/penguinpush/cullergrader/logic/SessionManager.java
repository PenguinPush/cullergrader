package com.penguinpush.cullergrader.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.penguinpush.cullergrader.config.AppPaths;
import com.penguinpush.cullergrader.media.Photo;
import com.penguinpush.cullergrader.media.PhotoGroup;

import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Saves and restores culling progress (which photos are selected, and the thresholds used) per photo folder.
 *
 * Sessions live in the data directory under sessions/, one JSON file per folder, named by a hash of the
 * folder's path. They're never written into the user's photo folder, which may be read-only (e.g. an SD card).
 */
public class SessionManager {

    private static final int SESSION_VERSION = 1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static class Session {
        int version = SESSION_VERSION;
        String folder;
        long savedAt;
        Float timeThresholdSeconds;
        Float similarityThresholdPercent;
        // filename -> selected; every photo is recorded so deliberate de-selections are restored too
        Map<String, Boolean> selections = new LinkedHashMap<>();

        public Float getTimeThresholdSeconds() {
            return timeThresholdSeconds;
        }

        public Float getSimilarityThresholdPercent() {
            return similarityThresholdPercent;
        }
    }

    /**
     * @return the saved session for this folder, or null if there isn't one (or it can't be read)
     */
    public static Session load(File folder) {
        File sessionFile = sessionFileFor(folder);
        if (!sessionFile.isFile()) {
            return null;
        }

        try (Reader reader = Files.newBufferedReader(sessionFile.toPath(), StandardCharsets.UTF_8)) {
            Session session = GSON.fromJson(reader, Session.class);
            if (session == null || session.selections == null) {
                return null;
            }
            logMessage("loaded saved progress for: " + folder.getAbsolutePath());
            return session;
        } catch (IOException | JsonParseException e) {
            logMessage("couldn't read saved progress, ignoring it: " + sessionFile.getAbsolutePath());
            return null;
        }
    }

    public static void save(File folder, List<PhotoGroup> groups, float timeThreshold, float similarityThreshold) {
        if (folder == null || groups == null) {
            return;
        }

        Session session = new Session();
        session.folder = folder.getAbsolutePath();
        session.savedAt = System.currentTimeMillis();
        session.timeThresholdSeconds = timeThreshold;
        session.similarityThresholdPercent = similarityThreshold;
        for (PhotoGroup group : groups) {
            for (Photo photo : group.getPhotos()) {
                session.selections.put(photo.getFile().getName(), photo.isSelected());
            }
        }

        File sessionFile = sessionFileFor(folder);
        try {
            Files.createDirectories(sessionFile.getParentFile().toPath());

            // write to a temp file first, so a crash mid-write can't corrupt existing progress
            Path temp = new File(sessionFile.getPath() + ".tmp").toPath();
            try (Writer writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                GSON.toJson(session, writer);
            }
            try {
                Files.move(temp, sessionFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, sessionFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            logMessage("couldn't save progress: " + e.getMessage());
        }
    }

    /**
     * Restores saved selections onto freshly generated groups.
     *
     * Groups containing any photo from the saved session take their selections from it (so picks survive
     * re-grouping with different thresholds). Groups made entirely of photos added since the last save
     * keep the default selection strategy.
     */
    public static void apply(Session session, List<PhotoGroup> groups) {
        if (session == null || groups == null) {
            return;
        }

        for (PhotoGroup group : groups) {
            boolean seenBefore = false;
            for (Photo photo : group.getPhotos()) {
                if (session.selections.containsKey(photo.getFile().getName())) {
                    seenBefore = true;
                    break;
                }
            }
            if (!seenBefore) {
                continue;
            }

            group.clearSelections();
            for (Photo photo : group.getPhotos()) {
                if (Boolean.TRUE.equals(session.selections.get(photo.getFile().getName()))) {
                    group.addSelectedTake(photo);
                }
            }
        }
    }

    private static File sessionFileFor(File folder) {
        String key;
        try {
            key = folder.getCanonicalPath();
        } catch (IOException e) {
            key = folder.getAbsolutePath();
        }
        return new File(AppPaths.getSessionsDir(), sha256Prefix(key) + ".json");
    }

    private static String sha256Prefix(String text) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                hex.append(String.format("%02x", digest[i]));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed on every JVM, but fall back to something stable just in case
            return Integer.toHexString(text.hashCode());
        }
    }
}
