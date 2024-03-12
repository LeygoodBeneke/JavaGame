#!/usr/bin/zsh

# This is simple script to compile, package and run the game.
# It is not intended to be used in production environment.
# Usage: ./run.sh <size> <wallsCount> <enemiesCount> <profile>

# Compile the game
mvn clean compile

# Package the game
mvn package

# Run the game
java -cp target/game-folder-0.1.jar edu.school21.App --size=$1 --wallsCount=$2 --enemiesCount=$3 --profile=$4