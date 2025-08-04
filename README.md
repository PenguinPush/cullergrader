# Cullergrader
**Cullergrader** is a simple Java GUI made for photographers that groups and exports images based on [**perceptual similarity**](https://en.wikipedia.org/wiki/Perceptual_hashing) (and timestamps), allowing users to select the best shots from each set of similar, consecutively taken photos.

Like many photographers, I have the habit of taking the same shot multiple times and selecting the best one to keep. However, when going through thousands of photos, this process of culling images is time-consuming, and tools such as [**Czkawka**](https://github.com/qarmin/czkawka) (a large inspiration for this project) can detect a few _very similar_ images, but don't group _somewhat similar_ bursts.

Cullergrader is named for being a tool that culls and grades* photos. Please note that it doesn't actually _colour grade_ photos.

<sub>* grading photos has yet to be implemented...</sub>

<div style="display: flex; justify-content: center; align-items: center;">
  <figure style="margin: 0 10px;">
    <img src="images/exampleA.JPG" alt="DSC07442.JPG" width="300"/>
    <figcaption>DSC07442.JPG</figcaption>
  </figure>
  <figure style="margin: 0 10px;">
    <img src="images/exampleB.JPG" alt="DSC07443.JPG" width="300"/>
    <figcaption>DSC07443.JPG</figcaption>
  </figure>
</div>
<p align="center">For example: Cullergrader would mark these two images as "similar" (at similarity threshold >= 37%)</p>

## Table of Contents
1. [Features](#features)
2. [Installation](#installation)
3. [How to Use](#how-to-use)
   1. [Open a folder of images](#1-open-a-folder-of-images)
   2. [Calibrate grouping settings](#2-calibrate-grouping-settings)
   3. [View photos and select best takes](#3-view-photos-and-select-best-takes)
   4. [Export your best takes](#4-export-your-best-takes)
4. [Config](#config)

## Features
- 100% free and open-source!
- Configurable options for calibrating your perceptual hash
    - Hash similarity
    - Timestamp difference
- Exports the best takes to any folder on your computer
- Runs on Windows, Mac, Linux, and anything else that supports Java GUIs
- Blazingly-fast thanks to configurable multithreading support
- Caches images -- future scans should be incredibly fast!
- Extra information about images availible on hover
- Runs completely offline, and never connects to the internet
- Logs information to .txt files
- Light/Dark themes
- Configurable:
    - Multithreading
    - Hashing settings
    - Cache options
    - Grouping settings
    - Dark theme
      
![images/group_viewer.png](images/group_viewer.png)


## Installation
Cullergrader requires a Java 8 JRE or newer to run, a prebuilt executable .jar with all required libraries bundled is available for download at [github.com/PenguinPush/cullergrader/releases](https://github.com/PenguinPush/cullergrader/releases). Extract the .zip to any folder and run the .jar to begin using Cullergrader.

Compiling from source code can be done with:
```
mvn clean install
```

## How to Use
### 1. Open a folder of images
Folders can be opened from `File > Open Folder` or with `Ctrl + O`. The first time images are computed, it may take a few minutes (but often less) to hash the images, depending on image count and disk speed. Hashes are cached for future use in `cache.json`, so as long as this file stays intact, future computations of the same images will be nearly instant.

![images/open_folder.png](images/open_folder.png)

### 2. Calibrate grouping settings
Although the default settings are designed to work fine out of the box, depending on many factors in your photo, and your style of photography, manual calibration is often recommended. By adjusting the timestamp and similarity thresholds and hitting `Reload Groups`, you can adjust how your images are grouped until the grouping behaves as expected.
- `Timestamp Threshold` is the amount of seconds between two photos before it counts it as no longer a part of the same photo group, and creates a new group
- `Similarity Threshold` is the percentage of similarity between the hash of two photos. A higher threshold means more tolerance for less similar photos to be in the same group.

![images/grouping_settings.png](images/grouping_settings.png)

### 3. View photos and select best takes
By clicking on a photo, users can access the `Photo Viewer`, bringing up all individual photos in a group, with the best take marked by a star (which by default is the first image in group). By navigating using either mouse or `arrow keys` (left and right to move between photos, up and down to move between groups) to a photo, they can use the `spacebar` or `Controls > Set Best Take` to change a photo to the best take. 

![images/photo_viewer.png](images/photo_viewer.png)

**Tip:** by hovering on a photo in the photo viewer, you can view its name, seconds between the last photo, and similarity % to the last photo. Use this information to help you calibrate the grouper.
![images/photo_info.png](images/photo_info.png)

### 4. Export your best takes
Best takes can be exported to a folder using `File > Export Best Takes` or with `Ctrl + S`. After choosing an export folder, the selected best takes will begin copying to that folder!

![images/export_to.png](images/export_to.png)

## Config

