#!/bin/bash

echo "Building Music Royalties Collection System..."
echo

echo "Cleaning previous build..."
mvn clean

echo
echo "Building project..."
mvn install

echo
echo "Build completed!"
