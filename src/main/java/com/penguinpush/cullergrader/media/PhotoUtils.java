package com.penguinpush.cullergrader.media;

import static com.penguinpush.cullergrader.utils.Logger.logMessage;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;

public class PhotoUtils {

    public static BufferedImage readLowResImage(File file, int targetWidth, int targetHeight) throws Exception {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                return null;
            }

            ImageReader reader = readers.next();
            reader.setInput(iis, true);

            int fullWidth = reader.getWidth(0);
            int fullHeight = reader.getHeight(0);

            int xStep = Math.max(1, fullWidth / targetWidth);
            int yStep = Math.max(1, fullHeight / targetHeight);

            ImageReadParam parameters = reader.getDefaultReadParam();
            parameters.setSourceSubsampling(xStep, yStep, xStep / 2, yStep / 2); // read every n-th pixel

            return reader.read(0, parameters);
        }
    }

    public static boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg")
                || name.endsWith(".png") || name.endsWith(".bmp")
                || name.endsWith(".gif") || name.endsWith(".webp");
    }

    public static long extractTimestamp(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (directory != null) {
                Date date = directory.getDateOriginal();
                if (date != null) {
                    return date.getTime(); // convert to unix time
                }
            }
        } catch (Exception e) {
            logMessage("couldn't find exif for: " + file.getName() + ", defaulting to last modified");
        }

        return file.lastModified(); // fallback
    }

    public static ImageIcon getScaledIcon(BufferedImage img, int width, int height) {
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_FAST);
        return new ImageIcon(scaled);
    }
    
    public static float[] rgbToLab(int red, int green, int blue) {
        // convoluted algorithm to convert rgb color space to lab
        
        float rNorm = red / 255f;
        float gNorm = green / 255f;
        float bNorm = blue / 255f;

        rNorm = (rNorm > 0.04045f) ? (float) Math.pow((rNorm + 0.055f) / 1.055f, 2.4f) : rNorm / 12.92f;
        gNorm = (gNorm > 0.04045f) ? (float) Math.pow((gNorm + 0.055f) / 1.055f, 2.4f) : gNorm / 12.92f;
        bNorm = (bNorm > 0.04045f) ? (float) Math.pow((bNorm + 0.055f) / 1.055f, 2.4f) : bNorm / 12.92f;

        float x = (rNorm * 0.4124f + gNorm * 0.3576f + bNorm * 0.1805f) / 0.95047f;
        float y = (rNorm * 0.2126f + gNorm * 0.7152f + bNorm * 0.0722f) / 1.00000f;
        float z = (rNorm * 0.0193f + gNorm * 0.1192f + bNorm * 0.9505f) / 1.08883f;

        x = (x > 0.008856f) ? (float) Math.pow(x, 1.0 / 3.0) : (7.787f * x + 16f / 116f);
        y = (y > 0.008856f) ? (float) Math.pow(y, 1.0 / 3.0) : (7.787f * y + 16f / 116f);
        z = (z > 0.008856f) ? (float) Math.pow(z, 1.0 / 3.0) : (7.787f * z + 16f / 116f);

        float luminance = 116f * y - 16f;
        float a = 500f * (x - y);
        float b = 200f * (y - z);

        return new float[]{luminance, a, b};
    }
    
    public static float[] rgbToHsv(int r, int g, int b) {
        float[] hsv = new float[3];

        float rNormalized = r / 255f;
        float gNormalized = g / 255f;
        float bNormalized = b / 255f;

        float cMax = Math.max(rNormalized, Math.max(gNormalized, bNormalized));
        float cMin = Math.min(rNormalized, Math.min(gNormalized, bNormalized));
        float delta = cMax - cMin;

        float h = 0;
        if (delta != 0) {
          if (cMax == rNormalized) {
              h = 60 * (((gNormalized - bNormalized) / delta) % 6);
          } else if (cMax == gNormalized) {
              h = 60 * (((bNormalized - rNormalized) / delta) + 2);
          } else if (cMax == bNormalized) {
              h = 60 * (((rNormalized - gNormalized) / delta) + 4);
          }
      }

        if(h < 0)
          h += 360;


        float s = (cMax == 0) ? 0 : delta / cMax;
        float v = cMax;

        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;
        return hsv;
    }
}
