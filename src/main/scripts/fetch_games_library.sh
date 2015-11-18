#!/bin/bash

mkdir -p import
cd import

PLAYERS_BASE=$(curl http://www.pgnmentor.com/files.html | grep "players/.*zip" | sed -e 's/\.zip.*//' -e 's/.*href="players\///' | grep -v Paehtz)

for base in $PLAYERS_BASE
do
    echo "Fetching $base"
    curl -o ${base}.zip http://www.pgnmentor.com/players/${base}.zip
    unzip ${base}.zip
    rm -f ${base}.zip
done