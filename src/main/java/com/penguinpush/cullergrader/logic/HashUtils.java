package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.media.PhotoUtils;
import java.awt.image.BufferedImage;
import java.util.*;

public class HashUtils {
    public static String computeChanneledAverageHash(BufferedImage image) {
        // computes the appended hash of three hsv channels, returns string size width * height * 3
        // only ever takes already scaled input
        float[][][] channels = splitToChannels(image);

        int width = AppConstants.HASHED_WIDTH;
        int height = AppConstants.HASHED_HEIGHT;
        StringBuilder hash = new StringBuilder();

        for (float[][] channel : channels) {
            float total = 0f;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    total += channel[y][x];
                }
            }

            float avg = total / (width * height);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    hash.append(channel[y][x] >= avg ? "1" : "0");
                }
            }
        }

        return hash.toString();
    }

    public static int hammingDistance(String h1, String h2) {
        if (h1.length() != h2.length()) {
            // different lengths, as distant as possible
            return Integer.MAX_VALUE;
        }
        int distance = 0;
        for (int i = 0; i < h1.length(); i++) {
            if (h1.charAt(i) != h2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    private static float[][][] splitToChannels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        float[][][] channels = new float[3][height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                float[] labValues = PhotoUtils.rgbToHsv(r, g, b);
                channels[0][y][x] = labValues[0]; // L
                channels[1][y][x] = labValues[1]; // a
                channels[2][y][x] = labValues[2]; // b
            }
        }
        return channels;
    }
}
