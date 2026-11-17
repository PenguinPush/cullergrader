# Changelog

All notable changes to Cullergrader are documented here.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project uses [Semantic Versioning](https://semver.org/).

## [1.1.0] - Unreleased

### Added
- **Native installers** for macOS (`.dmg`), Windows (`.msi`) and Debian/Ubuntu (`.deb`). Java is bundled in, so you no longer need to install it yourself. The portable `.zip` is still available for other systems.
- **Your progress is saved automatically.** Selections and threshold settings are saved every time you select a photo or reload groups, and restored when you open the same folder again, even after closing Cullergrader.
- **Select multiple takes per group.** `Space` now toggles a photo's selection instead of replacing the best take, so you can keep as many photos from a group as you like. Groups with nothing selected are dimmed and skipped on export, and the photo you're viewing is outlined in red. ([#5](https://github.com/PenguinPush/cullergrader/pull/5))
- `DEFAULT_SELECTION_STRATEGY` config option to choose which photos start selected in each group: `first` (default), `last`, `first_and_last`, `all`, or `none`. ([#5](https://github.com/PenguinPush/cullergrader/pull/5))
- **Experimental RAW support** for Sony (`.arw`, `.sr2`), Canon (`.cr2`, `.cr3`, `.crw`), Nikon (`.nef`, `.nrw`), Fujifilm (`.raf`), Olympus (`.orf`, `.ori`), Pentax (`.pef`, `.ptx`), Panasonic (`.rw2`), Hasselblad (`.3fr`), and Adobe DNG (`.dng`) files, using the JPEG preview embedded in each RAW file. ([#4](https://github.com/PenguinPush/cullergrader/pull/4))
- In-memory thumbnail cache, so reloading groups and scrolling through large folders is faster. Its size is set by the new `IMAGE_PREVIEW_CACHE_SIZE_MB` config option (default 2048). ([#4](https://github.com/PenguinPush/cullergrader/pull/4))
- **Command-line mode** for scripts and batch jobs: `--input`, `--output`, `--time`, `--similarity`, and `--help`. Without `--output`, it previews which files would be exported. ([#2](https://github.com/PenguinPush/cullergrader/pull/2))
- **Export group information as JSON**, with every group, photo, timestamp, hash, similarity score, and selection. Available from `File > Export Group Information (JSON)` or the `--json` CLI option. ([#3](https://github.com/PenguinPush/cullergrader/pull/3))
- `CULLERGRADER_HOME` environment variable to store Cullergrader's files in a folder of your choice, e.g. for a portable install.

### Changed
- **Cullergrader's files have moved** to a per-user data folder, so the app works the same no matter where it's launched from:
  - macOS: `~/Library/Application Support/Cullergrader`
  - Windows: `%APPDATA%\Cullergrader`
  - Linux: `~/.config/cullergrader`

  This folder holds `config.json`, `hashes.json`, `logs/`, and saved progress. A default `config.json` is created there on first run.
- Default thresholds are now **5 seconds** and **30% similarity** (previously 15 seconds and 45%), which produce tighter groups.
- "Best take" is now "selected take" throughout the app: `File > Export Best Takes` is now `File > Export Selected Takes`, and `Controls > Set Best Take` is now `Controls > Set as Selected Take`. ([#5](https://github.com/PenguinPush/cullergrader/pull/5))
- Config keys renamed to match: `KEYBIND_SET_BESTTAKE` is now `KEYBIND_TOGGLE_SELECTION`, and `BESTTAKE_LABEL_TEXT` is now `SELECTED_LABEL_TEXT`. ([#5](https://github.com/PenguinPush/cullergrader/pull/5))
- A group's thumbnail is now always its first photo, rather than its best take. ([#5](https://github.com/PenguinPush/cullergrader/pull/5))
- Only the 20 most recent log files are kept.
- The portable `.zip` no longer includes a `config.json`; the default config is now built into the app.
- Updated `metadata-extractor` from 2.18.0 to 2.19.0. ([#4](https://github.com/PenguinPush/cullergrader/pull/4))

### Fixed
- Opening a folder containing files Cullergrader can't read (such as `.webp` images, or RAW files without a preview) no longer crashes grouping. Those files are now skipped. ([#4](https://github.com/PenguinPush/cullergrader/pull/4))
- Selections are no longer reset when you change thresholds and click `Reload Groups`.
- A mistake in `config.json` no longer stops Cullergrader from starting; it falls back to the default settings and prints a warning instead.
- The placeholder thumbnail shown while photos load was missing from release downloads.

### Upgrading from 1.0.0
- Settings in a `config.json` next to the old `.jar` are **no longer read**. Copy any settings you changed into the new `config.json` in the data folder listed above.
- If you customized `KEYBIND_SET_BESTTAKE` or `BESTTAKE_LABEL_TEXT`, rename them to `KEYBIND_TOGGLE_SELECTION` and `SELECTED_LABEL_TEXT`.
- Your old `hashes.json` isn't carried over; hashes are recomputed (and cached again) the first time you open each folder.

### Known issues
- RAW photos show a thumbnail in the grid but appear blank in the full-size photo viewer.
- The installers aren't code-signed yet, so macOS and Windows show a security warning on first launch. See the README for how to open the app anyway.
- The macOS installer is for Apple Silicon only. Intel Macs can use the portable `.zip`.
- On Windows, the installed app doesn't show console output, so use the portable `.zip` for command-line mode.

Thanks to [shipurjan](https://github.com/shipurjan) for contributing CLI mode, JSON export, RAW support, and multiple selections!

## [1.0.0] - Initial release

- Groups photos by perceptual similarity and timestamp, with adjustable thresholds
- Photo viewer with keyboard navigation for choosing the best take in each group
- Exports best takes to a folder
- Multithreaded hashing with a hash cache, so repeat scans are fast
- Light and dark themes

[1.1.0]: https://github.com/PenguinPush/cullergrader/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/PenguinPush/cullergrader/releases/tag/v1.0.0
