Karsch The Pig 
Version 1.0 Build 20090210

Benutzerhandbuch
	
1.1 Das Spiel starten

Das Spiel wird gestartet, indem man die jeweilige Startdatei ausführt:

Unter Windows: 		karsch-windows.bat
Unter Linux:		karsch-linux.sh
Unter Mac OS:		karsch-macos.sh.command

Alternative Startmethode (empfohlen):	Auf www.rolla-nappelz.de über JavaWebStart starten

Beim Starten des Spiels kommt ein Auswahlfenster, wo Sie die Auflösung, Farbtiefe, Auffrischungsrate und den Renderer auswählen können.
Empfohlene Einstellungen sind 1024x768, 32 bpp(bits per pixel), Fullscreen.

Derzeit wird nur der LWJGL-Renderer unterstützt.

1.2 Ziel des Spiels

Ziel des Spiels ist es, alle Babies zu finden/zu befreien. Dafür Schlüssel gefunden, Türen geöffnet, Labyrinthe durchquert und Fallen und verrückten Kühen ausgewichen werden.

1.3 Tastenbelegung

Karsch the Pig kommt nur mit wenigen Tasten aus:


Pfeiltasten (Arrow Keys)

-Im Menü: 	Hoch/Runter 	Menüpunkt wechseln
		Rechts/Links 	In den Audioeinstellungen Lautstärke hoch/runter
		
-Im Spiel:	Karsch auf/ab/links/rechts bewegen


Enter (Return)
		
-Im Menü: 	Angewählten Menüpunkt aktivieren

		
Leertaste (Space)
		
-Im Spiel: 	Mit dem Objekt interagieren, das sich vor Karsch befindet

		
Escape

-Im Menü: 	eine Menüebene hoch / Menü verlassen
		
-Im Spiel: 	Das Spielmenü aufrufen

		
F1
		
-Im Spiel:	Hilfe zur Steuerung aufrufen


1.4 Mit Objekten interagieren

Karsch kann mit Objekten interagieren, die direkt vor ihm sind. Dazu muss sich Karsch direkt auf dem Nachbarfeld des Objekts befinden und in dessen Richtung sehen.

Objekte/Personen, mit denen Karsch interagieren kann, sind Mrs. Karsch, Gunther, Kühe, Schafe, Türen und Hebel.

1.5 Fehlerbehebung

Eventuell treten bei der Ausführung des Spiels Fehler auf.  Hier ist eine Übersicht der häufigsten Fehlerursachen:


-Das Spiel startet nicht

	Sie versuchen, die Jar-Datei direkt zu starten

		Starten Sie das Spiel über die jeweilige Script-Datei (siehe 1.1 Das Spiel starten)

	Java ist nicht installiert oder nicht aktuell	

		Java SE 6 bei java.sun.com herunterladen und installieren

	(Linux MacOS)
	Shell-Script ist nicht ausführbar

		Stellen sie sicher, dass das Dateiattribut „Executable“ für die Datei karsch.sh gesetzt ist

-Der Konfigurationsbildschirm erscheint, bei Bestätigung startet aber das Spiel nicht

		Falscher Renderer ausgewählt

			Im Konfigurationsbildschirm “LWJGL” als Renderer auswählen

	Ihre Grafikkartentreiber unterstützen kein OpenGL 2.1

		Aktuelle Treiber des Grafikkartenherstellers installieren

	(Linux)
	Sie haben kein Direct Rendering	

		Die Grafikkartentreiber korrekt für Direct Rendering konfigurieren
-Der Sound funktioniert nicht

	Sound und Musik sind im Menü ausgeschaltet 

		Sound und Musik im Menü auf „ON“ stellen

	Ihre Soundkartentreiber unterstützen kein OpenAL

		Dieser Fehler kann derzeit nicht behoben werden

-Das Spiel läuft langsam

	Grafikeinstellungen sind zu hoch gewählt

		Im Konfigurationsbildschirm niedrigere Auflösung und eventuell niedrigere Farbtiefe (16bit) auswählen

	Spiel wurde im Fenstermodus gestartet

		Im Konfigurationsbildschirm unter „Fullscreen“ das Häkchen setzen.
		Das Spiel kann nur im Vollbildmodus seine volle Leistung erreichen


1.6 Kontakt

	Stephan		bluestorm@web.de

	David		userdave@gmx.de
	