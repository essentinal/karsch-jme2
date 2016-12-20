Karsch The Pig 
Version 1.0 Build 20090210

Benutzerhandbuch
	
1.1 Das Spiel starten

Das Spiel wird gestartet, indem man die jeweilige Startdatei ausf�hrt:

Unter Windows: 		karsch-windows.bat
Unter Linux:		karsch-linux.sh
Unter Mac OS:		karsch-macos.sh.command

Alternative Startmethode (empfohlen):	Auf www.rolla-nappelz.de �ber JavaWebStart starten

Beim Starten des Spiels kommt ein Auswahlfenster, wo Sie die Aufl�sung, Farbtiefe, Auffrischungsrate und den Renderer ausw�hlen k�nnen.
Empfohlene Einstellungen sind 1024x768, 32 bpp(bits per pixel), Fullscreen.

Derzeit wird nur der LWJGL-Renderer unterst�tzt.

1.2 Ziel des Spiels

Ziel des Spiels ist es, alle Babies zu finden/zu befreien. Daf�r Schl�ssel gefunden, T�ren ge�ffnet, Labyrinthe durchquert und Fallen und verr�ckten K�hen ausgewichen werden.

1.3 Tastenbelegung

Karsch the Pig kommt nur mit wenigen Tasten aus:


Pfeiltasten (Arrow Keys)

-Im Men�: 	Hoch/Runter 	Men�punkt wechseln
		Rechts/Links 	In den Audioeinstellungen Lautst�rke hoch/runter
		
-Im Spiel:	Karsch auf/ab/links/rechts bewegen


Enter (Return)
		
-Im Men�: 	Angew�hlten Men�punkt aktivieren

		
Leertaste (Space)
		
-Im Spiel: 	Mit dem Objekt interagieren, das sich vor Karsch befindet

		
Escape

-Im Men�: 	eine Men�ebene hoch / Men� verlassen
		
-Im Spiel: 	Das Spielmen� aufrufen

		
F1
		
-Im Spiel:	Hilfe zur Steuerung aufrufen


1.4 Mit Objekten interagieren

Karsch kann mit Objekten interagieren, die direkt vor ihm sind. Dazu muss sich Karsch direkt auf dem Nachbarfeld des Objekts befinden und in dessen Richtung sehen.

Objekte/Personen, mit denen Karsch interagieren kann, sind Mrs. Karsch, Gunther, K�he, Schafe, T�ren und Hebel.

1.5 Fehlerbehebung

Eventuell treten bei der Ausf�hrung des Spiels Fehler auf.  Hier ist eine �bersicht der h�ufigsten Fehlerursachen:


-Das Spiel startet nicht

	Sie versuchen, die Jar-Datei direkt zu starten

		Starten Sie das Spiel �ber die jeweilige Script-Datei (siehe 1.1 Das Spiel starten)

	Java ist nicht installiert oder nicht aktuell	

		Java SE 6 bei java.sun.com herunterladen und installieren

	(Linux MacOS)
	Shell-Script ist nicht ausf�hrbar

		Stellen sie sicher, dass das Dateiattribut �Executable� f�r die Datei karsch.sh gesetzt ist

-Der Konfigurationsbildschirm erscheint, bei Best�tigung startet aber das Spiel nicht

		Falscher Renderer ausgew�hlt

			Im Konfigurationsbildschirm �LWJGL� als Renderer ausw�hlen

	Ihre Grafikkartentreiber unterst�tzen kein OpenGL 2.1

		Aktuelle Treiber des Grafikkartenherstellers installieren

	(Linux)
	Sie haben kein Direct Rendering	

		Die Grafikkartentreiber korrekt f�r Direct Rendering konfigurieren
-Der Sound funktioniert nicht

	Sound und Musik sind im Men� ausgeschaltet 

		Sound und Musik im Men� auf �ON� stellen

	Ihre Soundkartentreiber unterst�tzen kein OpenAL

		Dieser Fehler kann derzeit nicht behoben werden

-Das Spiel l�uft langsam

	Grafikeinstellungen sind zu hoch gew�hlt

		Im Konfigurationsbildschirm niedrigere Aufl�sung und eventuell niedrigere Farbtiefe (16bit) ausw�hlen

	Spiel wurde im Fenstermodus gestartet

		Im Konfigurationsbildschirm unter �Fullscreen� das H�kchen setzen.
		Das Spiel kann nur im Vollbildmodus seine volle Leistung erreichen


1.6 Kontakt

	Stephan		bluestorm@web.de

	David		userdave@gmx.de
	