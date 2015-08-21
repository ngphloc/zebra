;Copyright 2004-2008 John T. Haller of PortableApps.com

;Website: http://PortableApps.com/FirefoxPortable

;This software is OSI Certified Open Source Software.
;OSI Certified is a certification mark of the Open Source Initiative.

;This program is free software; you can redistribute it and/or
;modify it under the terms of the GNU General Public License
;as published by the Free Software Foundation; either version 2
;of the License, or (at your option) any later version.

;This program is distributed in the hope that it will be useful,
;but WITHOUT ANY WARRANTY; without even the implied warranty of
;MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;GNU General Public License for more details.

;You should have received a copy of the GNU General Public License
;along with this program; if not, write to the Free Software
;Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

!define PORTABLEAPPNAME "Mozilla Firefox, Portable Edition"
!define APPNAME "Firefox"
!define NAME "FirefoxPortable"
!define VER "1.6.0.0"
!define WEBSITE "PortableApps.com/FirefoxPortable"
!define DEFAULTEXE "firefox.exe"
!define DEFAULTAPPDIR "firefox"
!define LICENSEVERSION "3"

;=== Program Details
Name "${PORTABLEAPPNAME}"
OutFile "..\..\${NAME}.exe"
Caption "${PORTABLEAPPNAME} | PortableApps.com"
VIProductVersion "${VER}"
VIAddVersionKey ProductName "${PORTABLEAPPNAME}"
VIAddVersionKey Comments "Allows ${APPNAME} to be run from a removable drive.  For additional details, visit ${WEBSITE}"
VIAddVersionKey CompanyName "PortableApps.com"
VIAddVersionKey LegalCopyright "John T. Haller"
VIAddVersionKey FileDescription "${PORTABLEAPPNAME}"
VIAddVersionKey FileVersion "${VER}"
VIAddVersionKey ProductVersion "${VER}"
VIAddVersionKey InternalName "${PORTABLEAPPNAME}"
VIAddVersionKey LegalTrademarks "Firefox is a Registered Trademark of The Mozilla Foundation.  PortableApps.com is a Registered Trademark of Rare Ideas, LLC."
VIAddVersionKey OriginalFilename "${NAME}.exe"
;VIAddVersionKey PrivateBuild ""
;VIAddVersionKey SpecialBuild ""

;=== Runtime Switches
CRCCheck On
;WindowIcon Off
;SilentInstall Silent
AutoCloseWindow True
RequestExecutionLevel user

; Best Compression
SetCompress Auto
SetCompressor /SOLID lzma
SetCompressorDictSize 32
SetDatablockOptimize On

;=== Include
;(Standard NSIS)
!include MUI.nsh
!include Registry.nsh
!include TextFunc.nsh
!insertmacro GetParameters
!insertmacro GetParent

;(NSIS Plugins)
!include TextReplace.nsh

;(Custom)
!include ReplaceInFileWithTextReplace.nsh
!include ReadINIStrWithDefault.nsh
!include SetFileAttributesDirectoryNormal.nsh

;=== Program Icon
Icon "..\..\App\AppInfo\appicon.ico"

;=== Icon & Stye ===
!define MUI_ICON "..\..\App\AppInfo\appicon.ico"
BrandingText "PortableApps.com - Your Digital Life, Anywhere�"
MiscButtonText "" "" "" "$(LauncherNextButton)"
InstallButtonText "$(LauncherNextButton)"

;=== Pages
!define MUI_PAGE_CUSTOMFUNCTION_PRE ShowLicense
!define MUI_LICENSEPAGE_CHECKBOX
!insertmacro MUI_PAGE_LICENSE "EULA.rtf"
Page custom ShowLauncherOptions LeaveLauncherOptions "" 
!insertmacro MUI_PAGE_INSTFILES

;=== Languages
!insertmacro MUI_LANGUAGE "English"
!include PortableApps.comLauncherLANG_ENGLISH.nsh
!include PortableApps.comLauncherOptionsLANG_ENGLISH.nsh

;=== Variables
Var PROGRAMDIRECTORY
Var PROFILEDIRECTORY
Var ORIGINALPROFILEDIRECTORY
Var SETTINGSDIRECTORY
Var PLUGINSDIRECTORY
Var ADDITIONALPARAMETERS
Var ALLOWMULTIPLEINSTANCES
Var SKIPCOMPREGFIX
Var EXECSTRING
Var PROGRAMEXECUTABLE
Var INIPATH
Var DISABLESPLASHSCREEN
Var DISABLEINTELLIGENTSTART
Var LOCALHOMEPAGE
Var ISDEFAULTDIRECTORY
Var RUNLOCALLY
Var WAITFORPROGRAM
Var LASTPROFILEDIRECTORY
Var APPDATAPATH
Var SECONDARYLAUNCH
Var SHOWLICENSE
Var SHOWOPTIONS
Var MOZILLAORGKEYEXISTS
Var MISSINGFILEORPATH
Var CRASHREPORTSDIREXISTS
Var EXTENSIONSDIREXISTS
Var ENABLESESSIONSTORE
Var SESSIONSTORECHANGED

Function ShowLicense
	StrCmp $SHOWLICENSE "true" ShowLicenseEnd
		Abort
		
	ShowLicenseEnd:
FunctionEnd

Function ShowLauncherOptions
	!insertmacro MUI_INSTALLOPTIONS_EXTRACT "PortableApps.comLauncherOptionsForm.ini"
	!insertmacro MUI_HEADER_TEXT "$(LauncherOptionsHeader)" "$(LauncherOptionsHeader2)"
	WriteINIStr $PLUGINSDIR\PortableApps.comLauncherOptionsForm.ini "Field 1" "Text" "$(LauncherOptionsIntro)"
	WriteINIStr $PLUGINSDIR\PortableApps.comLauncherOptionsForm.ini "Field 2" "Text" "$(LauncherOptionsOption1)"
	WriteINIStr $PLUGINSDIR\PortableApps.comLauncherOptionsForm.ini "Field 3" "Text" "$(LauncherOptionsOption2)"
	WriteINIStr $PLUGINSDIR\PortableApps.comLauncherOptionsForm.ini "Field 4" "Text" "$(LauncherOptionsOption1Description)"
	WriteINIStr $PLUGINSDIR\PortableApps.comLauncherOptionsForm.ini "Field 5" "Text" "$(LauncherOptionsOption2Description)"
	WriteINIStr $PLUGINSDIR\PortableApps.comLauncherOptionsForm.ini "Field 6" "Text" "$(LauncherOptionsOutro)"	
	InstallOptions::InitDialog /NOUNLOAD "$PLUGINSDIR\PortableApps.comLauncherOptionsForm.ini"
    Pop $R0
    InstallOptions::Show
FunctionEnd

Function LeaveLauncherOptions
	ReadINIStr $0 "$PLUGINSDIR\PortableApps.comLauncherOptionsForm.ini" "Field 2" "State"
	StrCpy $SESSIONSTORECHANGED "true"
	
	StrCmp $0 "1" "" LeaveLauncherOptionsDisable
		StrCpy $ENABLESESSIONSTORE "true"
		Goto LeaveLauncherOptionsEnd

	LeaveLauncherOptionsDisable:
		StrCpy $ENABLESESSIONSTORE "false"
	
	LeaveLauncherOptionsEnd:
FunctionEnd

Function .onInit
	${GetParameters} $0
	StrCmp "$0" "SHOWLICENSE" CheckWhatToShow LicenseDone

	CheckWhatToShow:
		IfFileExists "$EXEDIR\Data\settings\FirefoxPortableSettings.ini" "" ShowBoth
			ReadINIStr $0 "$EXEDIR\Data\settings\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "AgreedToLicense"
			StrCmp $0 "${LICENSEVERSION}" "" ShowBoth
	
	;ShowOptionsOnly:
		StrCpy $SHOWOPTIONS "true"
		StrCpy $SHOWLICENSE "false"
		Goto onInitDone	
	
	ShowBoth:
		StrCpy $SHOWOPTIONS "true"
		StrCpy $SHOWLICENSE "true"
		Goto onInitDone

	LicenseDone:
		SetSilent silent
		Goto onInitDone

	onInitDone:
FunctionEnd


Section "Main"
	;=== Setup variables
	ReadEnvStr $APPDATAPATH "APPDATA"

	;=== Find the INI file, if there is one
		IfFileExists "$EXEDIR\${NAME}.ini" "" NoINI
			StrCpy "$INIPATH" "$EXEDIR"

		;=== Read the parameters from the INI file
		${ReadINIStrWithDefault} $0 "$INIPATH\${NAME}.ini" "${NAME}" "${APPNAME}Directory" "App\${DEFAULTAPPDIR}"
		StrCpy $PROGRAMDIRECTORY "$EXEDIR\$0"
		${ReadINIStrWithDefault} $0 "$INIPATH\${NAME}.ini" "${NAME}" "ProfileDirectory" "Data\profile"
		StrCpy $PROFILEDIRECTORY "$EXEDIR\$0"
		${ReadINIStrWithDefault} $0 "$INIPATH\${NAME}.ini" "${NAME}" "SettingsDirectory" "Data\settings"
		StrCpy $SETTINGSDIRECTORY "$EXEDIR\$0"
		${ReadINIStrWithDefault} $0 "$INIPATH\${NAME}.ini" "${NAME}" "PluginsDirectory" "Data\plugins"
		StrCpy $PLUGINSDIRECTORY "$EXEDIR\$0"
		${ReadINIStrWithDefault} $ADDITIONALPARAMETERS "$INIPATH\${NAME}.ini" "${NAME}" "AdditionalParameters" ""
		${ReadINIStrWithDefault} $ALLOWMULTIPLEINSTANCES "$INIPATH\${NAME}.ini" "${NAME}" "AllowMultipleInstances" "false"
		${ReadINIStrWithDefault} $SKIPCOMPREGFIX "$INIPATH\${NAME}.ini" "${NAME}" "SkipCompregFix" "false"
		${ReadINIStrWithDefault} $PROGRAMEXECUTABLE "$INIPATH\${NAME}.ini" "${NAME}" "${APPNAME}Executable" "${DEFAULTEXE}"
		${ReadINIStrWithDefault} $WAITFORPROGRAM "$INIPATH\${NAME}.ini" "${NAME}" "WaitFor${APPNAME}" "false"
		${ReadINIStrWithDefault} $DISABLESPLASHSCREEN "$INIPATH\${NAME}.ini" "${NAME}" "DisableSplashScreen" "false"
		${ReadINIStrWithDefault} $DISABLEINTELLIGENTSTART "$INIPATH\${NAME}.ini" "${NAME}" "DisableIntelligentStart" "false"
		${ReadINIStrWithDefault} $LOCALHOMEPAGE "$INIPATH\${NAME}.ini" "${NAME}" "LocalHomepage" ""
		${ReadINIStrWithDefault} $RUNLOCALLY "$INIPATH\${NAME}.ini" "${NAME}" "RunLocally" "false"
		StrCmp $RUNLOCALLY "true" "" CheckIfDefaultDirectories
			StrCpy $WAITFORPROGRAM "true"

	CheckIfDefaultDirectories:
		;=== Check if default directories
		StrCmp $PROGRAMDIRECTORY "$EXEDIR\App\${DEFAULTAPPDIR}" "" EndINI
		StrCmp $PROFILEDIRECTORY "$EXEDIR\Data\profile" "" EndINI
		StrCmp $PLUGINSDIRECTORY "$EXEDIR\Data\plugins" "" EndINI
		StrCmp $SETTINGSDIRECTORY "$EXEDIR\Data\settings" "" EndINI
		StrCpy $ISDEFAULTDIRECTORY "true"
	
	EndINI:
		IfFileExists "$PROGRAMDIRECTORY\$PROGRAMEXECUTABLE" FoundProgramEXE NoProgramEXE

	NoINI:
		;=== No INI file, so we'll use the defaults
		StrCpy $ADDITIONALPARAMETERS ""
		StrCpy $ALLOWMULTIPLEINSTANCES "false"
		StrCpy $SKIPCOMPREGFIX "false"
		StrCpy $WAITFORPROGRAM "false"
		StrCpy $PROGRAMEXECUTABLE "${DEFAULTEXE}"
		StrCpy $DISABLESPLASHSCREEN "false"
		StrCpy $DISABLEINTELLIGENTSTART "false"

		IfFileExists "$EXEDIR\App\${DEFAULTAPPDIR}\${DEFAULTEXE}" "" CheckPortableProgramDIR
			StrCpy $PROGRAMDIRECTORY "$EXEDIR\App\${DEFAULTAPPDIR}"
			StrCpy $PROFILEDIRECTORY "$EXEDIR\Data\profile"
			StrCpy $PLUGINSDIRECTORY "$EXEDIR\Data\plugins"
			StrCpy $SETTINGSDIRECTORY "$EXEDIR\Data\settings"
			StrCpy $ISDEFAULTDIRECTORY "true"
			Goto FoundProgramEXE
	
	CheckPortableProgramDIR:
			IfFileExists "$EXEDIR\${NAME}\App\${DEFAULTAPPDIR}\${DEFAULTEXE}" "" NoProgramEXE
			StrCpy $PROGRAMDIRECTORY "$EXEDIR\${NAME}\App\${DEFAULTAPPDIR}"
			StrCpy $PROFILEDIRECTORY "$EXEDIR\${NAME}\Data\profile"
			StrCpy $PLUGINSDIRECTORY "$EXEDIR\${NAME}\Data\plugins"
			StrCpy $SETTINGSDIRECTORY "$EXEDIR\${NAME}\Data\settings"
			Goto FoundProgramEXE

	NoProgramEXE:
		;=== Program executable not where expected
		StrCpy $MISSINGFILEORPATH $PROGRAMEXECUTABLE
		MessageBox MB_OK|MB_ICONEXCLAMATION `$(LauncherFileNotFound)`
		Abort

	FoundProgramEXE:
		StrCpy $ORIGINALPROFILEDIRECTORY $PROFILEDIRECTORY
		IfFileExists "$APPDATA\Mozilla\Firefox\*.*" CheckIfRunning
			StrCpy $WAITFORPROGRAM "true"
			${registry::KeyExists} "HKEY_CURRENT_USER\Software\mozilla.org" $R0
			StrCmp $R0 "-1" CheckIfRunning ;=== If it doesn't exist, skip the next line
			StrCpy $MOZILLAORGKEYEXISTS "true"

	CheckIfRunning:
		;=== Check if running
		StrCmp $ALLOWMULTIPLEINSTANCES "true" ProfileWork
		FindProcDLL::FindProc "firefox.exe"
		StrCmp $R0 "1" "" CheckForCrashReports
			;=== Already running, check if it is using the portable profile
			IfFileExists "$PROFILEDIRECTORY\parent.lock" "" WarnAnotherInstance
				StrCpy $SECONDARYLAUNCH "true"
				Goto RunProgram

	WarnAnotherInstance:
		MessageBox MB_OK|MB_ICONINFORMATION `$(LauncherAlreadyRunning)`
		Abort
	
	CheckForCrashReports:
		IfFileExists "$APPDATA\Mozilla\Firefox\Crash Reports\*.*" "" CheckForExtensionsDirectory
			Rename "$APPDATA\Mozilla\Firefox\Crash Reports" "$APPDATA\Mozilla\Firefox\Crash Reports-BackupByFirefoxPortable"
			StrCpy $CRASHREPORTSDIREXISTS "true"
			StrCpy $WAITFORPROGRAM "true"

	CheckForExtensionsDirectory:
		IfFileExists "$APPDATA\Mozilla\Extensions\*.*" "" ProfileWork
			Rename "$APPDATA\Mozilla\Extensions" "$APPDATA\Mozilla\Extensions-BackupByFirefoxPortable"
			StrCpy $EXTENSIONSDIREXISTS "true"
			StrCpy $WAITFORPROGRAM "true"
	
	ProfileWork:
		;=== Check for an existing profile
		IfFileExists "$PROFILEDIRECTORY\prefs.js" ProfileFound
			;=== No profile was found
			StrCmp $ISDEFAULTDIRECTORY "true" CopyDefaultProfile CreateProfile
	
	CopyDefaultProfile:
		CreateDirectory "$EXEDIR\Data"
		CreateDirectory "$EXEDIR\Data\plugins"
		CreateDirectory "$EXEDIR\Data\profile"
		CreateDirectory "$EXEDIR\Data\settings"
		CopyFiles /SILENT $EXEDIR\App\DefaultData\plugins\*.* $EXEDIR\Data\plugins
		CopyFiles /SILENT $EXEDIR\App\DefaultData\profile\*.* $EXEDIR\Data\profile
		GoTo ProfileFound
	
	CreateProfile:
		IfFileExists "$PROFILEDIRECTORY\*.*" ProfileFound
		CreateDirectory "$PROFILEDIRECTORY"

	ProfileFound:
		IfFileExists "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" SettingsFound
			CreateDirectory "$SETTINGSDIRECTORY"
			FileOpen $R0 "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" w
			FileClose $R0
			WriteINIStr "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "LastProfileDirectory" "NONE"

	SettingsFound:
		StrCmp $SHOWLICENSE "true" WriteSettingsOut
		StrCmp $SHOWOPTIONS "true" WriteSettingsOut CheckForLicense

	WriteSettingsOut:
		WriteINIStr "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "AgreedToLicense" "${LICENSEVERSION}"
		WriteINIStr "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "EnableSessionStore" "$ENABLESESSIONSTORE"
		StrCmp $SESSIONSTORECHANGED "true" "" TheEnd
			IfFileExists "$PROFILEDIRECTORY\prefs.js" "" TheEnd
				FileOpen $0 "$PROFILEDIRECTORY\prefs.js" a
				FileSeek $0 0 END
				FileWriteByte $0 "13"
				FileWriteByte $0 "10"
				FileWrite $0 `user_pref("browser.sessionstore.enabled", $ENABLESESSIONSTORE);`
				FileWriteByte $0 "13"
				FileWriteByte $0 "10"
			Goto TheEnd
	
	CheckForLicense:
		ReadINIStr $0 "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "AgreedToLicense"
		StrCmp $0 "${LICENSEVERSION}" "" RelaunchWithLicense
		ReadINIStr $0 "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "EnableSessionStore"
		StrCmp $0 "" RelaunchWithLicense
		StrCpy $ENABLESESSIONSTORE $0
		Goto LicenseAgreedTo

	RelaunchWithLicense:
		System::Call 'kernel32::GetModuleFileNameA(i 0, t .R0, i 1024) i r1'
		ExecWait `$R0 SHOWLICENSE`
		ReadINIStr $0 "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "AgreedToLicense"
		StrCmp $0 "${LICENSEVERSION}" LicenseAgreedTo TheEnd
	
	LicenseAgreedTo:
		;=== Check for read/write
		StrCmp $RUNLOCALLY "true" DisplaySplash
		ClearErrors
		FileOpen $R0 "$PROFILEDIRECTORY\writetest.temp" w
		IfErrors "" WriteSuccessful
			;== Write failed, so we're read-only
			MessageBox MB_YESNO|MB_ICONQUESTION `$(LauncherAskCopyLocal)` IDYES SwitchToRunLocally
			MessageBox MB_OK|MB_ICONINFORMATION `$(LauncherNoReadOnly)`
			Abort

	SwitchToRunLocally:
		StrCpy $RUNLOCALLY "true"
		StrCpy $WAITFORPROGRAM "true"
		Goto DisplaySplash
	
	WriteSuccessful:
		FileClose $R0
		Delete "$PROFILEDIRECTORY\writetest.temp"
	
	DisplaySplash:
		StrCmp $DISABLESPLASHSCREEN "true" SkipSplashScreen
			;=== Show the splash screen before processing the files
			InitPluginsDir
			File /oname=$PLUGINSDIR\splash.jpg "${NAME}.jpg"
			newadvsplash::show /NOUNLOAD 2000 0 0 -1 /L $PLUGINSDIR\splash.jpg

	SkipSplashScreen:
		;=== Run locally if needed (aka Portable Firefox Live)
		StrCmp $RUNLOCALLY "true" "" CompareProfilePath
		RMDir /r "$TEMP\${NAME}\"
		CreateDirectory $TEMP\${NAME}\profile
		CreateDirectory $TEMP\${NAME}\plugins
		CreateDirectory $TEMP\${NAME}\program
		CopyFiles /SILENT $PROFILEDIRECTORY\*.* $TEMP\${NAME}\profile
		StrCpy $PROFILEDIRECTORY $TEMP\${NAME}\profile
		CopyFiles /SILENT $PLUGINSDIRECTORY\*.* $TEMP\${NAME}\plugins
		StrCpy $PLUGINSDIRECTORY $TEMP\${NAME}\plugins
		CopyFiles /SILENT $PROGRAMDIRECTORY\*.* $TEMP\${NAME}\program
		StrCpy $PROGRAMDIRECTORY $TEMP\${NAME}\program
		${SetFileAttributesDirectoryNormal} "$TEMP\${NAME}"

	CompareProfilePath:
		ReadINIStr $LASTPROFILEDIRECTORY "$SETTINGSDIRECTORY\${NAME}Settings.ini" "${NAME}Settings" "LastProfileDirectory"
		StrCmp $PROFILEDIRECTORY $LASTPROFILEDIRECTORY "" RememberProfilePath
			StrCmp $DISABLEINTELLIGENTSTART "true" RememberProfilePath
				StrCpy $SKIPCOMPREGFIX "true"
	
	RememberProfilePath:
		WriteINIStr "$SETTINGSDIRECTORY\${NAME}Settings.ini" "${NAME}Settings" "LastProfileDirectory" "$PROFILEDIRECTORY"

	;FixPrefsJs:
		IfFileExists "$PROFILEDIRECTORY\prefs.js" "" FixMimeTypes
		StrCmp $LASTPROFILEDIRECTORY "NONE" FixPrefsJsPart2
		StrCpy $2 $LASTPROFILEDIRECTORY 1 ;Last drive letter
		StrCpy $3 $PROFILEDIRECTORY 1 ;Current drive letter
		StrCmp $2 $3 FixPrefsJsPart2 ;If no change, move on
		
		;=== Replace drive letters without impacting other instances of the letter in prefs.js
		${ReplaceInFileCS} "$PROFILEDIRECTORY\prefs.js" `file:///$2` `file:///$3`
		${ReplaceInFileCS} "$PROFILEDIRECTORY\prefs.js" `", "$2:\\` `", "$3:\\`
	
	FixPrefsJsPart2:
		;=== Be sure the default browser check is disabled
		FileOpen $0 "$PROFILEDIRECTORY\prefs.js" a
		FileSeek $0 0 END
		FileWriteByte $0 "13"
		FileWriteByte $0 "10"
		FileWrite $0 `user_pref("browser.shell.checkDefaultBrowser", false);`
		FileWriteByte $0 "13"
		FileWriteByte $0 "10"
		StrCmp "$LOCALHOMEPAGE" "" FixPrefsJsClose
		FileWrite $0 `user_pref("browser.startup.homepage", "file:///$EXEDIR/$LOCALHOMEPAGE");`
		FileWriteByte $0 "13"
		FileWriteByte $0 "10"

	FixPrefsJsClose:
		FileClose $0 

	FixMimeTypes:
		IfFileExists "$PROFILEDIRECTORY\mimeTypes.rdf" "" RunProgram
		StrCmp $LASTPROFILEDIRECTORY "NONE" RunProgram
		${GetParent} $LASTPROFILEDIRECTORY $0
		${GetParent} $0 $0
		${GetParent} $0 $0
		StrCpy $0 '$0\' ;last PortableApps directory
		${GetParent} $ORIGINALPROFILEDIRECTORY $1
		${GetParent} $1 $1
		${GetParent} $1 $1
		StrCpy $1 '$1\' ;current PortableApps directory
		StrCmp $0 $1 RunProgram
		${ReplaceInFile} "$PROFILEDIRECTORY\mimeTypes.rdf" $0 $1
	
	RunProgram:
		StrCmp $SKIPCOMPREGFIX "true" GetPassedParameters

		;=== Delete component registry to ensure compatibility with all extensions
		Delete $PROFILEDIRECTORY\compreg.dat

	GetPassedParameters:
		;=== Get any passed parameters
		${GetParameters} $0
		StrCmp "'$0'" "''" "" LaunchProgramParameters

		;=== No parameters
		StrCpy $EXECSTRING `"$PROGRAMDIRECTORY\$PROGRAMEXECUTABLE" -profile "$PROFILEDIRECTORY"`
		Goto CheckMultipleInstances

	LaunchProgramParameters:
		StrCpy $EXECSTRING `"$PROGRAMDIRECTORY\$PROGRAMEXECUTABLE" -profile "$PROFILEDIRECTORY" $0`

	CheckMultipleInstances:
		StrCmp $ALLOWMULTIPLEINSTANCES "true" "" AdditionalParameters
		System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i("MOZ_NO_REMOTE", "1").r0'

	AdditionalParameters:
		StrCmp $ADDITIONALPARAMETERS "" PluginsEnvironment

		;=== Additional Parameters
		StrCpy $EXECSTRING `$EXECSTRING $ADDITIONALPARAMETERS`

	PluginsEnvironment:
		;=== Set the plugins directory if we have a path
		StrCmp $PLUGINSDIRECTORY "" LaunchNow
		IfFileExists "$PLUGINSDIRECTORY\*.*" "" LaunchNow
		System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i("MOZ_PLUGIN_PATH", "$PLUGINSDIRECTORY").r0'

	LaunchNow:
		StrCmp $SECONDARYLAUNCH "true" StartProgramAndExit
		StrCmp $WAITFORPROGRAM "true" "" StartProgramAndExit
		ExecWait $EXECSTRING

	CheckRunning:
		Sleep 2000
		StrCmp $ALLOWMULTIPLEINSTANCES "true" CheckIfRemoveLocalFiles
		FindProcDLL::FindProc "firefox.exe"                  
		StrCmp $R0 "1" CheckRunning CleanupRunLocally
	
	StartProgramAndExit:
		Exec $EXECSTRING
		Goto TheEnd
	
	CleanupRunLocally:
		StrCmp $RUNLOCALLY "true" "" CheckIfRemoveLocalFiles
		RMDir /r "$TEMP\${NAME}\"

	CheckIfRemoveLocalFiles:
		FindProcDLL::FindProc "firefox.exe"
		Pop $R0
		StrCmp $R0 "1" TheEnd RemoveLocalFiles

	RemoveLocalFiles:
		StrCmp $ALLOWMULTIPLEINSTANCES "true" RemoveLocalFiles2
		RMDir /r "$APPDATA\Mozilla\Firefox\Crash Reports\"
		Rename "$APPDATA\Mozilla\Firefox\Crash Reports-BackupByFirefoxPortable" "$APPDATA\Mozilla\Firefox\Crash Reports"
		
	RemoveLocalFiles2:
		StrCmp $ALLOWMULTIPLEINSTANCES "true" RemoveLocalFiles3
		RMDir /r "$APPDATA\Mozilla\Extensions\"
		Rename "$APPDATA\Mozilla\Extensions-BackupByFirefoxPortable" "$APPDATA\Mozilla\Extensions"
		
	RemoveLocalFiles3:
		Delete "$APPDATA\Mozilla\Firefox\pluginreg.dat"
		RMDir "$APPDATA\Mozilla\Firefox\" ;=== Will only delete if empty (no /r switch)
		RMDir "$APPDATA\Mozilla\" ;=== Will only delete if empty (no /r switch)
		StrCmp $MOZILLAORGKEYEXISTS "true" TheEnd
			${registry::DeleteKey} "HKEY_CURRENT_USER\Software\mozilla.org" $R0

	TheEnd:
		${registry::Unload}
		newadvsplash::stop /WAIT
SectionEnd