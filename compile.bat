@echo off
mkdir out
javac -cp lib\sqlite-jdbc.jar -d out src\Main.java src\db\*.java src\model\*.java src\dao\*.java
echo Compiled successfully. Run run.bat to start.
