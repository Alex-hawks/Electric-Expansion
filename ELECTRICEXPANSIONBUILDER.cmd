::Elecric Expansion BUILDER
@echo off
echo Promotion Type?
set /p PROMOTION=

echo FTP Username?
set /p USERNAME=

echo FTP Password?
set /p PASSWORD=



set /p MODVERSION=<modversion.txt
set /p CurrentBuild=<buildnumber.txt
set /a BUILD_NUMBER=%CurrentBuild%+1
echo %BUILD_NUMBER% >buildnumber.txt

set FILE_NAME=ElectricExpansion_v%MODVERSION%.%BUILD_NUMBER%.jar

echo Starting to build %FILE_NAME%

::BUILD
runtime\bin\python\python_mcp runtime\recompile.py %*
runtime\bin\python\python_mcp runtime\reobfuscate.py %*

::Delete Basic Components
cd reobf\minecraft
"rmdir basiccomponents/"


::ZIP-UP
cd reobf\minecraft\
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "*"
cd ..\..\

cd textures\
"..\..\7za.exe" a "..\builds\%FILE_NAME%" "*"
cd ..\

::UPDATE INFO FILE
echo %PROMOTION% %FILE_NAME%>>info.txt

::GENERATE FTP Script
echo open www.calclavia.com>ftpscript.txt
echo %USERNAME%>>ftpscript.txt
echo %PASSWORD%>>ftpscript.txt
echo binary>>ftpscript.txt
echo put "builds\%FILE_NAME%">>ftpscript.txt
echo put info.txt>>ftpscript.txt
echo quit>>ftpscript.txt
ftp.exe -s:ftpscript.txt
del ftpscript.txt

echo Done building %FILE_NAME%

pause