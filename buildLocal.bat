@echo off
timeout /t 2 /nobreak > nul

set "jarPath=build\libs\furnaceexpmixins-2.0.0.jar"
set "destinationPath=C:\Users\micha\OneDrive\Documents\Docker\furnaceexpmixins\server\mods\furnaceexpmixins-2.0.0.jar"

echo Deleting "%jarPath%"
del "%jarPath%" 2>nul || echo File not found or unable to delete.

echo Building "%jarPath%"
call gradlew.bat build --no-daemon
if errorlevel 1 (
    echo Build failed.
    pause
    exit /b 1
)

echo Deploying "%jarPath%" to "%destinationPath%"
copy /Y "%jarPath%" "%destinationPath%" || (
    echo Copy failed.
    pause
    exit /b 1
)

echo Deployment successful.

echo startiing server.

cd server
java -Xmx2048M -Xms2048M -jar forge-1.12.2-14.23.5.2860-universal.jar -o true nogui

