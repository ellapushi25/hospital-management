#!/bin/bash
# Compiles the Hospital Management System
mkdir -p out
javac -cp lib/sqlite-jdbc.jar -d out src/Main.java src/db/*.java src/model/*.java src/dao/*.java
echo "Compiled successfully. Run ./run.sh to start."
