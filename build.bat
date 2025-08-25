@echo off
echo Building Music Royalties Collection System...
echo.

echo Cleaning previous build...
call mvn clean

echo.
echo Building project...
call mvn install

echo.
echo Build completed!
pause
