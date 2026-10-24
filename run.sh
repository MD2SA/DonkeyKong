#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")"

if ! command -v javac >/dev/null 2>&1; then
    echo "Error: javac not found. Install Java 21 or newer." >&2
    exit 1
fi

echo "Compiling sources..."
rm -rf bin
mkdir -p bin

sources=()
while IFS= read -r source; do
    sources+=("$source")
done < <(find src -name '*.java' -print | sort)
javac -d bin -sourcepath src "${sources[@]}"

echo "Starting Donkey Kong 2D..."
java -cp bin pt.iscte.poo.game.Main "$@"
