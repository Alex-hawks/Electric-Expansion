::UNIVERSAL ELECTRICITY BUILD SCRIPT
@echo off
echo Promotion Type?
set /p PROMOTION=


set /p MODVERSION=<modversion.txt
set /p CurrentBuild=<buildnumber.txt
set /a BUILD_NUMBER=%CurrentBuild%+1
echo %BUILD_NUMBER% >buildnumber.txt

if %PROMOTION%==* (
	echo %MODVERSION% >recommendedversion.txt
)

set FILE_NAME=BasicComponents_v%MODVERSION%.%BUILD_NUMBER%.jar
set BACKUP_NAME=UniversalElectricity_v%MODVERSION%.%BUILD_NUMBER%_backup.zip
set API_NAME=UniversalElectricity_v%MODVERSION%.%BUILD_NUMBER%_api.zip

echo Starting to build %FILE_NAME%

::BUILD
runtime\bin\python\python_mcp runtime\recompile.py %*
runtime\bin\python\python_mcp runtime\reobfuscate.py %*

::ZIP-UP
cd reobf\minecraft\
7za.exe a "..\..\builds\%FILE_NAME%" "*"
cd ..\..\
cd resources\
7za.exe a "..\builds\%FILE_NAME%" "*"
7za.exe a "..\builds\%BACKUP_NAME%" "*"
cd ..\
cd src\
7za.exe a "..\builds\%API_NAME%" "*"

7za.exe a "..\builds\%BACKUP_NAME%" "*"

cd ..\


echo Done building %FILE_NAME%

pause