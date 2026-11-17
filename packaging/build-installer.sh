#!/usr/bin/env bash
# Builds a native installer for the current OS, with a Java runtime bundled in,
# so users don't need to install Java themselves. Requires JDK 17+ (for jpackage).
#
# Usage:
#   mvn clean package
#   bash packaging/build-installer.sh [dmg|pkg|msi|exe|deb|rpm|app-image]
#
# The type defaults to dmg on macOS, msi on Windows and deb on Linux.
# .msi/.exe need the WiX Toolset v3 installed; .rpm needs rpm-build.
# Output goes to target/installer/.
set -euo pipefail
cd "$(dirname "$0")/.."

TYPE="${1:-}"
if [ -z "$TYPE" ]; then
    case "$(uname -s)" in
        Darwin) TYPE=dmg ;;
        MINGW* | MSYS* | CYGWIN*) TYPE=msi ;;
        *) TYPE=deb ;;
    esac
fi

ZIP=$(ls target/cullergrader-*.zip 2>/dev/null | head -n 1 || true)
if [ -z "$ZIP" ]; then
    echo "No target/cullergrader-*.zip found. Run 'mvn clean package' first." >&2
    exit 1
fi
VERSION=$(basename "$ZIP" .zip)
VERSION=${VERSION#cullergrader-}

# jpackage wants a folder holding the main jar and its lib/ dependencies, which is what the release zip contains
STAGING=target/jpackage-input
rm -rf "$STAGING" target/installer
mkdir -p "$STAGING"
(cd "$STAGING" && jar xf "../../$ZIP")
rm -f "$STAGING/run.sh" "$STAGING/run.bat"

ARGS=(
    --type "$TYPE"
    --name Cullergrader
    --app-version "$VERSION"
    --vendor PenguinPush
    --description "Groups similar photos so you can pick the best takes"
    --input "$STAGING"
    --main-jar "cullergrader-$VERSION.jar"
    --main-class com.penguinpush.cullergrader.Main
    --dest target/installer
)

case "$TYPE" in
    dmg | pkg)
        ARGS+=(--mac-package-identifier com.penguinpush.cullergrader)
        ;;
    msi | exe)
        # keep the upgrade UUID fixed forever, so new versions replace old installs instead of installing side by side
        ARGS+=(--win-menu --win-shortcut --win-dir-chooser --win-upgrade-uuid 5c3bd07d-9731-42dd-863e-424a43cc3e48)
        ;;
    deb | rpm)
        ARGS+=(--linux-package-name cullergrader --linux-shortcut --linux-menu-group Graphics)
        ;;
esac

echo "Building $TYPE for Cullergrader $VERSION"
jpackage "${ARGS[@]}"
echo "Done:"
ls -1 target/installer
