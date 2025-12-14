# Cullergrader
**Cullergrader** is a simple Java GUI made for photographers that groups and exports images based on <a href="https://en.wikipedia.org/wiki/Perceptual_hashing" target="_blank">**perceptual similarity**</a> (and timestamps), allowing users to select the best shots from each set of similar, consecutively taken photos.

Like many photographers, I have the habit of taking the same shot multiple times and selecting the best one to keep. However, when going through thousands of photos, this process of culling images is time-consuming, and tools such as <a href="https://github.com/qarmin/czkawka" target="_blank">**Czkawka**</a> (a large inspiration for this project) can detect a few _very similar_ images, but isn't sensitive enough to group _somewhat similar_ bursts.

Cullergrader is named for being a tool that culls and grades* photos. Please note that it doesn't actually _colour grade_ photos.

<sub>* grading photos has yet to be implemented...</sub>

<div style="display: flex; justify-content: center; align-items: center; gap: 20px;">
    <img src="images/exampleA.JPG" alt="DSC07442.JPG" width="300"/>
    <img src="images/exampleB.JPG" alt="DSC07443.JPG" width="300"/>
</div>
<p align="left">For example: Cullergrader would mark these two images as "similar" (at similarity threshold >= 37%)</p>

## Table of Contents
1. [Features](#features)
2. [Installation](#installation)
    1. [Prebuilt Executable](#prebuilt-executable)
    2. [Compiling from Source](#compiling-from-source)
3. [How to Use](#how-to-use)
   1. [Open a Folder of Images](#1-open-a-folder-of-images)
   2. [Calibrate Grouping Settings](#2-calibrate-grouping-settings)
   3. [View Photos and Select Takes](#3-view-photos-and-select-takes)
   4. [Export Selected Takes](#4-export-selected-takes)
4. [Config](#config)
   1. [Default Config](#default-config)
   2. [Config Settings Explained](#config-settings-explained)
5. [Contributing](#contributing)
6. [License](#license)

## Features
- 100% free and open-source!
- Configurable options for calibrating your perceptual hash
    - Hash similarity
    - Timestamp difference
- Exports the best takes to any folder on your computer
- Runs on Windows, Mac, Linux, and anything else that supports Java GUIs
- Blazingly-fast thanks to configurable multithreading support
- Caches images -- future scans should be incredibly fast!
- Extra information about images available on hover
- Runs completely offline, and never connects to the internet
- Logs information to .txt files
- Light/Dark themes from FlatLaf
- Configurable:
    - Multithreading
    - Hashing settings
    - Cache options
    - Grouping settings
    - Dark theme
      
![images/group_viewer.png](images/group_viewer.png)


## Installation

### Prebuilt Executable
1. Cullergrader requires a Java 8 JRE or newer to run
2. A prebuilt executable `.jar` with all libraries bundled is available for download at [GitHub Releases](https://github.com/PenguinPush/cullergrader/releases)
3. Extract the `.zip` file to any folder and run `cullergrader-<version>.jar` file to begin using Cullergrader

**Note**:
- If you want to view logs in console, please use `run.bat` or `run.sh` depending on your operating system
- **Using console is highly recommended for large batches of images (or on slow drives), as there is no other way to monitor slow hashing progress!**

### Compiling from Source
1. Ensure you have the following installed:
    - Java Development Kit (JDK) 8 or newer
    - Apache Maven
2. Clone the repository with:
   ```bash
   git clone https://github.com/PenguinPush/cullergrader.git
   cd cullergrader
   ```
3. Build the project with the following command:
    ```
    mvn clean install
    ```
4. Extract the generated `cullergrader-version.zip` and run as described above

## How to Use
### 1. Open a Folder of Images
Folders can be opened from `File > Open Folder` or with `Ctrl + O`. The first time images are computed, it may take a few minutes (but often less) to hash the images, depending on image count and disk speed. Hashes are cached for future use in `cache.json`, so as long as this file stays intact, future computations of the same images will be nearly instant.

![images/open_folder.png](images/open_folder.png)

### 2. Calibrate Grouping Settings
Although the default settings are designed to work fine out of the box, depending on many factors in your photo, and your style of photography, manual calibration is often recommended. By adjusting the timestamp and similarity thresholds and hitting `Reload Groups`, you can adjust how your images are grouped until the grouping behaves as expected.
- `Timestamp Threshold` is the amount of seconds between two photos before it counts it as no longer a part of the same photo group, and creates a new group
- `Similarity Threshold` is the percentage of similarity between the hash of two photos. A higher threshold means more tolerance for less similar photos to be in the same group.

![images/grouping_settings.png](images/grouping_settings.png)

### 3. View Photos and Select Takes
By clicking on a photo, users can access the `Photo Viewer`, bringing up all individual photos in a group, with selected takes marked by a star. By navigating using either mouse or `arrow keys` (left and right to move between photos, up and down to move between groups), users can press `spacebar` or use `Controls > Set as Selected Take` to toggle photo selection. Multiple photos can be selected per group, and groups with 0 selections will not be exported. 

![images/photo_viewer.png](images/photo_viewer.png)

**Tip:** by hovering on a photo in the photo viewer, you can view its name, seconds between the last photo, and similarity % to the last photo. Use this information to help you calibrate the grouper.

![images/photo_info.png](images/photo_info.png)

### 4. Export Selected Takes
Selected takes can be exported to a folder using `File > Export Selected Takes` or with `Ctrl + S`. After choosing an export folder, all selected takes from each group will be copied to that folder. Groups with no selections will be skipped.

![images/export_to.png](images/export_to.png)

## Config
### Default Config
```json
{
    "DARK_THEME": true,
    "CACHE_FILE": "hashes.json",
    "LOG_DIRECTORY": "logs",
    "DEFAULT_FOLDER_PATH": "",
    "HASHING_ENABLED": true,
    "EXECUTOR_TIMEOUT_MINUTES": 60,
    "MAX_THREADS_RATIO": 2,
    "HASHED_WIDTH": 8,
    "HASHED_HEIGHT": 8,
    "TIME_THRESHOLD_SECONDS": 15,
    "SIMILARITY_THRESHOLD_PERCENT": 45,
    "DEFAULT_SELECTION_STRATEGY": "first"
}
```

### Config Settings Explained
| Setting                        | Description                                                                                                                                                                                                                                | Variable Type |
|--------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|
| `DARK_THEME`                   | Toggles the dark and light FlatLaf themes. On by default (obviously)                                                                                                                                                                       | `boolean`     |
| `CACHE_FILE`                   | The name of the *file* used to store hash caches. Passed as a pathname in a File constructor                                                                                                                                               | `String`      |
| `LOG_DIRECTORY`                | The name of the *folder* used to store logs. Passed as a pathname in a File constructor                                                                                                                                                    | `String`      |
| `DEFAULT_FOLDER_PATH`          | The default folder opened when importing images. Passed as a pathname in a File constructor. When empty, uses the systems `user.home` property                                                                                             | `String`      |
| `HASHING_ENABLED`              | Whether or not hashing is used in grouping photos                                                                                                                                                                                          | `boolean`     |
| `EXECUTOR_TIMEOUT_MINUTES`     | The amount of time, in minutes, before the hash manager times out and stops hashing photos                                                                                                                                                 | `int`         |
| `MAX_THREADS_RATIO`            | The fraction of CPU threads the hasher is allowed to multithread. 2 means half your threads, 3 means a third, etc.                                                                                                                         | `int`         |
| `HASHED_WIDTH`                 | The width that images are computed at before hashing, higher values mean more accurate similarity checks at the cost of performance                                                                                                        | `int`         |
| `HASHED_HEIGHT`                | The height that images are computed at before hashing, higher values mean more accurate similarity checks at the cost of performance                                                                                                       | `int`         |
| `TIME_THRESHOLD_SECONDS`       | The default amount of seconds between photos (from the timestamp) before they're counted as a new group. Editable in-app, but will not change the default stored here                                                                      | `float`       |
| `SIMILARITY_THRESHOLD_PERCENT` | The default similarity between two photo hashes before they're counted as a new group. Higher values means more lenience in image similarity (larger groups, less in number). Editable in-app, but will not change the default stored here | `float`       |
| `DEFAULT_SELECTION_STRATEGY`   | The automatic selection strategy when creating groups. Options: `"first"` (select first photo), `"last"` (select last photo), `"first_and_last"` (select both), `"all"` (select all), `"none"` (no automatic selection)                   | `String`      |

Note: More config options are technically functional, such as `PLACEHOLDER_THUMBNAIL_PATH`, `KEYBIND_TOGGLE_SELECTION`, or `GRIDMEDIA_LABEL_TEXT_COLOR`, but are not documented here and aren't editable by default due to their configurability not significantly impacting program function. Users are free to explore the source code and add these into `config.json` themselves, and they should work as intended. 

## Contributing
Contributions to Cullergrader are **greatly appreciated**, as a tool made from one photographer to another, the best way Cullergrader can improve is through continued feedback and contributions.

Any and all kinds of contributions to Cullergrader are welcome, please fork the repo and make a pull request! For suggestions without code, feel free to open an issue with an "enhancement" tag. Don't forget to give the project a star! 🙏🙏
1. Fork the Project to your own GitHub account
2. Create your Feature Branch with `git checkout -b feature/<your-awesome-feature>`
3. Commit your changes with `git commit -m "Add <a description of your change>"`
4. Push your changes with `git push origin feature/<your-awesome-feature>`
5. Open a Pull Request at the original repository here, don't forget to describe what you added!


## License
Distributed under the MIT License, see [LICENSE](https://github.com/PenguinPush/cullergrader/blob/main/LICENSE) for more info.
