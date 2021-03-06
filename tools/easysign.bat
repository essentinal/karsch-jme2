@echo off
REM THIS BATCH FILE AUTOMATES THE PROCESS OF SIGNING JARS

REM LOCATION OF JAVA BINARIES (JARSIGNER AND KEYSTORE)
set pathentry="C:\Program Files\Java\jdk1.6.0_13\bin"

REM KEYSTORE INFO
set keyname="keystore"
set keypass=stephan
set storepass=stephan

REM YOUR PERSONAL INFO
set alias=Stephan
set name=Stephan
set organizational_unit=Stephan
set organization=Stephan
set city=Berlin
set state=Berlin
set country=Germany

REM TEH CODE
echo Adding Path Entry %pathentry%...
set path=%path%;%pathentry%
set basedir=%cd%
:menu
if not "%basedir%"=="%cd%" cd "%basedir%"
echo GotCake's Jar Signing Automator:
echo 1: Create Keystore
echo 2: Create Keystore And Sign Jars
echo 3: Sign Jars
echo 4: Exit
set /p ans=
if %ans% equ 1 goto createkeystore
if %ans% equ 2 goto createkeystore
if %ans% equ 3 goto signjar
goto exit

:createkeystore
echo Checking Keystore...
if exist %keyname% goto keyexists
goto makekey

:keyexists
echo Keystore found.
set /p overwrite=Overwrite existing keystore? [y/n]:
if "%overwrite%"=="y" goto delkey
if %ans% equ 2 goto signjar
goto menu

:delkey
echo Deleting existing key...
del "%keyname%"

:makekey
echo Creating New Keystore...
keytool -genkey -alias %alias% -validity 10000 -keypass %keypass% -storepass %storepass% -keystore %keyname% -dname "CN=%name%, OU=%organizational_unit%, O=%organization%, L=%city%, S=%state%, C=%country%"
if %ans% equ 2 goto signjar
echo Done Creating Keystore.
goto menu

:genkey
set ans=2
goto createkeystore

:keynotexist
echo Key Does Not Exist.
set /p makestore=Generate A Keystore? [y/n]:
if "%makestore%"=="y" goto genkey
goto menu

:changedir
set /p newdir=Enter another directory:
cd %newdir%
goto signfinal

:signjar
if not exist %keyname% goto keynotexist
set /p usecurdir=Sign Jars In "%cd%"? [y/n]:
if "%usecurdir%"=="n" goto changedir
echo Signing All Jars In "%CD%"...
:signfinal
for /f "tokens=*" %%a in ('dir /b *.jar') do jarsigner -keystore %keyname% -storepass %storepass% -keypass %keypass% %%a %alias%
echo Done Signing Jars.
goto menu

:exit