<h1>Dies ist das Informatik Projekt von Michael und Lukas</h1>

- PROJEKTBEZEICHNUNG:             Racing
- PROJEKTZWECK:                   Informatik Abgabe  
- VERSION:                        V.0.5.2  
- WIE IST DAS PROJEKT ZU STARTEN: Visual Studio Code, nicht vergessen die Bibliotheken korrekt zu importieren  
- AUTOREN:                        Michael, Lukas  
- BENUTZERHINWEISE:               Sie müssen fahren können  

Programmiert mit GLOOP:
Funktioniert mit etwas Glück auch auf ihrem PC!

<h2>Spielidee:</h2>

Man steuert ein Auto durch eine 10000 mal 10000 Einheiten große Fläche.
Dieses Auto kann driften.
Außerdem hat man einen Musikplayer, in dem man Musik pausieren, abspielen und wechseln kann.
Die im Projekt benutzte Musik wurde aus rein demonstrativen Zwecken in einem privaten Umfeld benutzt. Veröffentlichung und Weitergabe ist aus diesem Grunde NICHT erlaubt.


Man steuert dieses Auto durch eine traumhafte Landschaft. Um diese spannender zu machen  werden Schneemänner zufällig um den Spieler erzeugt. Das erste Element der Liste wird gelöscht und durch Zufall neu um den Spieler platziert. Der neue Scheemann wird ans Ende der Liste gehangen. Im Laufe des Spiels kommen immer mehr Schneemänner dazu.
Außerdem: Blöcke, denen man ausweichen muss!

<h2>Benutzerhinweise</h2>

Die Steuerung mag einem anfangs etwas komisch vorkommen, da man ziemlich einfach anfängt zu driften. Wir hoffen, dass es nicht zu schlimm ist.

Außerdem gibt es kein traditionelles "Ziel" oder auch "Goal" dieses Spiels, genießen sie einfach ihre Fahrt.

<h2>Steuerung:</h2>

Motor beschleunigen: o  
Motor entschleunigen: l  
Lenken: 
Schwach: a - d, 
Stark: q - e
Bremsen: SPACE
Infos: + / - 
Programm schließen: z

<h2>Konsole</h2>

Wenn man im Hauptfenster + oder - drückt, ändern sich die angezeigten Werte. Es gibt: 

- Koordinaten
- Geschwindigkeiten
- Beschleunigungen
- Winkel



<h2>Code-Einblick</h2>

Wir laden sie dazu ein, sich den Code anzusehen.
Wir hoffen, dass dieser trotz der eher spärlichen Kommentare verständlich ist.


<h2>Probleme</h2>
Eine große Herausforderung war die GLOOP Bibliothek. Diese bietet KEINE möglichkeit die Kamera um ihre eigene Achse zu drehen. (Die Rotieren-Funktion funktioniert einfach nicht)
Selbst das Kamera auszurichten bereitete aufgrund dazu fehlender Funktionen (im Gegensatz zu Objekten) noch größere Probleme.
Außerdem funktioniert die Bibliothek auf einigen Computern nicht. Woran es genau liegt, bleibt ein Rätsel. Wir hoffen, dass es auf ihrem PC funktioniert.
Eventuell hat es mit der Reihenfolge der importierten Bibliotheken zu tun, versuchen sie es bei Nichtfunktionieren der GLOOP-Bibliothek damit:

[SCREENSHOT](https://github.com/LukasGasp/racing/files/Screenshot.png)
