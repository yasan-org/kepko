#!/usr/bin/env bash

# Packages the sample app as a Linux AppImage.
#
# jpackage (which Compose Multiplatform's nativeDistributions wraps) can produce
# deb and rpm but not AppImage, so this takes the `createDistributable` output
# and wraps it into an AppDir that appimagetool can turn into a single binary.
#
# Usage: scripts/package-appimage.sh [output.AppImage]

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

APP_NAME="Kepko"
APP_ID="glass.yasan.kepko.sample"
DIST_DIR="$PROJECT_DIR/sample/composeApp/build/compose/binaries/main/app/$APP_NAME"
ICON_FILE="$PROJECT_DIR/sample/composeApp/src/commonMain/composeResources/drawable/app_icon.png"
BUILD_DIR="$PROJECT_DIR/sample/composeApp/build/appimage"
APP_DIR="$BUILD_DIR/$APP_NAME.AppDir"
OUTPUT="${1:-$BUILD_DIR/$APP_NAME-$(uname -m).AppImage}"

if [[ "$(uname -s)" != "Linux" ]]; then
  echo "❌ Error: AppImages can only be built on Linux."
  exit 1
fi

if [[ ! -d "$DIST_DIR" ]]; then
  echo "❌ Error: distributable not found at $DIST_DIR"
  echo "   Run ./gradlew :sample:composeApp:createDistributable first."
  exit 1
fi

case "$(uname -m)" in
  x86_64) ARCH=x86_64 ;;
  aarch64 | arm64) ARCH=aarch64 ;;
  *) echo "❌ Error: unsupported architecture $(uname -m)"; exit 1 ;;
esac
export ARCH

# Fetch appimagetool unless one is already on PATH.
APPIMAGETOOL="$(command -v appimagetool || true)"
if [[ -z "$APPIMAGETOOL" ]]; then
  # Kept out of $BUILD_DIR so it is never mistaken for a build output.
  APPIMAGETOOL="$BUILD_DIR/tool/appimagetool-$ARCH.AppImage"
  if [[ ! -x "$APPIMAGETOOL" ]]; then
    mkdir -p "$BUILD_DIR/tool"
    echo "⬇️  Downloading appimagetool ($ARCH)"
    curl -fsSL -o "$APPIMAGETOOL" \
      "https://github.com/AppImage/appimagetool/releases/download/continuous/appimagetool-$ARCH.AppImage"
    chmod +x "$APPIMAGETOOL"
  fi
fi

rm -rf "$APP_DIR"
mkdir -p "$APP_DIR"

# jpackage's launcher resolves lib/ relative to itself, so the bin/ and lib/
# layout has to be preserved verbatim.
cp -a "$DIST_DIR/." "$APP_DIR/"

# The desktop entry and icon are required at the AppDir root; the copies under
# usr/share are what desktop environments pick up once the AppImage is
# integrated (e.g. by AppImageLauncher).
mkdir -p "$APP_DIR/usr/share/applications" \
         "$APP_DIR/usr/share/icons/hicolor/512x512/apps"

cp "$ICON_FILE" "$APP_DIR/$APP_ID.png"
cp "$ICON_FILE" "$APP_DIR/usr/share/icons/hicolor/512x512/apps/$APP_ID.png"

cat > "$APP_DIR/$APP_ID.desktop" <<DESKTOP
[Desktop Entry]
Type=Application
Name=$APP_NAME
Comment=An opinionated design system for Compose Multiplatform
Exec=$APP_NAME
Icon=$APP_ID
Categories=Development;
Terminal=false
DESKTOP
cp "$APP_DIR/$APP_ID.desktop" "$APP_DIR/usr/share/applications/"

cat > "$APP_DIR/AppRun" <<'APPRUN'
#!/usr/bin/env bash
set -euo pipefail
APPDIR="${APPDIR:-$(cd "$(dirname "$(readlink -f "$0")")" && pwd)}"
exec "$APPDIR/bin/Kepko" "$@"
APPRUN
chmod +x "$APP_DIR/AppRun"

mkdir -p "$(dirname "$OUTPUT")"
rm -f "$OUTPUT"

# --appimage-extract-and-run avoids needing FUSE, which is unavailable in most
# CI containers.
"$APPIMAGETOOL" --appimage-extract-and-run --no-appstream "$APP_DIR" "$OUTPUT"

echo "✅ AppImage created: $OUTPUT"
