package com.penguinpush.cullergrader.logic;

import com.penguinpush.cullergrader.config.AppConstants;
import com.penguinpush.cullergrader.media.Photo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.*;

public class ImageLoader {
    private final PriorityBlockingQueue<ImageLoadTask> taskQueue;
    private final ExecutorService executor;

    public ImageLoader() {
        taskQueue = new PriorityBlockingQueue<>();
        int maxThreads = Math.max(1, Runtime.getRuntime().availableProcessors() / AppConstants.MAX_THREADS_RATIO);
        executor = Executors.newWorkStealingPool(maxThreads);

        for (int i = 0; i < maxThreads; i++) {
            executor.submit(this::processQueue);
        }
    }

    public void loadImage(Photo photo, int priority, boolean fullImage, ImageLoadCallback callback) {
        taskQueue.add(new ImageLoadTask(photo, priority, fullImage, callback));
    }

    private void processQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ImageLoadTask task = taskQueue.take();
                BufferedImage image;
                if (!task.isFullImage()) {
                    image = task.photo.getThumbnail();
                } else {
                    image = ImageIO.read(task.photo.getFile());
                }
                task.callback.onImageLoaded(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }

    private static class ImageLoadTask implements Comparable<ImageLoadTask> {
        private final Photo photo;
        private int priority;
        private final ImageLoadCallback callback;
        private boolean fullImage;

        public ImageLoadTask(Photo photo, int priority, boolean fullImage, ImageLoadCallback callback) {
            this.photo = photo;
            this.priority = priority;
            this.fullImage = fullImage;
            this.callback = callback;
        }

        public boolean isFullImage() {
            return fullImage;
        }

        @Override
        public int compareTo(ImageLoadTask other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    public interface ImageLoadCallback {
        void onImageLoaded(BufferedImage image);
    }

    public void updatePriority(Photo photo, int priority) {
        synchronized (taskQueue) {
            ImageLoadTask targetTask = null;

            for (ImageLoadTask task : taskQueue) {
                if (task.photo.equals(photo)) {
                    targetTask = task;
                    break;
                }
            }

            if (targetTask != null) {
                // remove and readd the task to update the priority
                taskQueue.remove(targetTask);
                targetTask.priority = priority;
                taskQueue.add(targetTask);
            }
        }
    }
}