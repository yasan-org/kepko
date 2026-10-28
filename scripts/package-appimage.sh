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

# Pinned to releases and checked against their SHA-256, so a replaced download is never used.
APPIMAGETOOL_VERSION="1.9.1"
RUNTIME_VERSION="20251108"
case "$(uname -m)" in
  x86_64)
    ARCH=x86_64
    APPIMAGETOOL_SHA256="ed4ce84f0d9caff66f50bcca6ff6f35aae54ce8135408b3fa33abfc3cb384eb0"
    RUNTIME_SHA256="2fca8b443c92510f1483a883f60061ad09b46b978b2631c807cd873a47ec260d"
    ;;
  aarch64 | arm64)
    ARCH=aarch64
    APPIMAGETOOL_SHA256="f0837e7448a0c1e4e650a93bb3e85802546e60654ef287576f46c71c126a9158"
    RUNTIME_SHA256="00cbdfcf917cc6c0ff6d3347d59e0ca1f7f45a6df1a428a0d6d8a78664d87444"
    ;;
  *) echo "❌ Error: unsupported architecture $(uname -m)"; exit 1 ;;
esac
export ARCH

# Downloads a file unless it is already there, failing if it doesn't match the expected SHA-256.
# Usage: download_verified <name> <url> <sha256> <file>
download_verified() {
  if [[ -f "$4" ]]; then
    return
  fi
  mkdir -p "$(dirname "$4")"
  echo "⬇️  Downloading $1"
  curl -fsSL -o "$4.part" "$2"
  if ! echo "$3  $4.part" | sha256sum -c - >/dev/null 2>&1; then
    rm -f "$4.part"
    echo "❌ Error: $1 download does not match its expected SHA-256."
    exit 1
  fi
  mv "$4.part" "$4"
}

# Always the pinned appimagetool, never one already on PATH, so every build uses the same tool.
APPIMAGETOOL="$BUILD_DIR/tool/appimagetool-$APPIMAGETOOL_VERSION-$ARCH.AppImage"
download_verified "appimagetool $APPIMAGETOOL_VERSION ($ARCH)" \
  "https://github.com/AppImage/appimagetool/releases/download/$APPIMAGETOOL_VERSION/appimagetool-$ARCH.AppImage" \
  "$APPIMAGETOOL_SHA256" "$APPIMAGETOOL"
chmod +x "$APPIMAGETOOL"

# appimagetool would otherwise embed whatever runtime the mutable "continuous" release has.
RUNTIME="$BUILD_DIR/tool/runtime-$RUNTIME_VERSION-$ARCH"
download_verified "AppImage runtime $RUNTIME_VERSION ($ARCH)" \
  "https://github.com/AppImage/type2-runtime/releases/download/$RUNTIME_VERSION/runtime-$ARCH" \
  "$RUNTIME_SHA256" "$RUNTIME"

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
"$APPIMAGETOOL" --appimage-extract-and-run --no-appstream --runtime-file "$RUNTIME" "$APP_DIR" "$OUTPUT"

echo "✅ AppImage created: $OUTPUT"
