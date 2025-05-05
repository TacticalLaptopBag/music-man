#!/bin/bash
set -e

if [ -z "${INSTALL_DIR}" ]; then
  INSTALL_DIR=$HOME/.local/opt/music-man
fi

if [ -z "${BIN_DIR}" ]; then
  BIN_DIR=$HOME/.local/bin
fi

echo "Installing music-man to $INSTALL_DIR"

./gradlew installDist --quiet || (echo "Please run this script in the project root" && exit 1)
mkdir -p "$INSTALL_DIR"
cp -rf build/install/music-man/* "$INSTALL_DIR"

echo "Creating symlinks for installed binary in $BIN_DIR"
rm -f "$BIN_DIR/music-man" "$BIN_DIR/mm"
ln -s "$INSTALL_DIR/bin/music-man" "$BIN_DIR/music-man"
ln -s "$INSTALL_DIR/bin/music-man" "$BIN_DIR/mm"

echo "music-man installed. Use 'music-man' or 'mm'"
