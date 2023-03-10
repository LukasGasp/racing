<h1>Dies ist das Informatik Projekt von Michael und Lukas</h1>

- PROJEKTBEZEICHNUNG:             Racing
- PROJEKTZWECK:                   Informatik Abgabe  
- VERSION:                        V.0.5.2  
- WIE IST DAS PROJEKT ZU STARTEN: Visual Studio Code, nicht vergessen die Bibliotheken korrekt zu importieren  
- AUTOREN:                        Michael, Lukas  
- BENUTZERHINWEISE:               Sie müssen fahren können  

Programmiert mit <b>GLOOP</b>:
Funktioniert mit etwas Glück auch auf ihrem PC!

<h2>Spielidee:</h2>

Man steuert ein Auto durch eine 20000 mal 20000 Einheiten große Fläche. <br>
Dieses Auto kann driften. <br>
Außerdem hat man einen Musikplayer, in dem man Musik pausieren, abspielen und wechseln kann. <br>
Die im Projekt benutzte Musik wurde aus rein demonstrativen Zwecken in einem privaten Umfeld benutzt. Veröffentlichung und Weitergabe ist aus diesem Grunde NICHT erlaubt. <br>


Man steuert sein Auto durch eine traumhafte Landschaft. Um diese spannender zu machen, werden Schneemänner zufällig um den Spieler herum erzeugt.  <br>
Außerdem gibt Betonblöcke, denen man ausweichen muss! <br>
Bei Kollision mit Blöcken oder der Weltbarriere wird man an die Anfangsposition gesetzt. <br>
Blöcke, Schneemänner und Weltbarriere werden auf einer Minimap angezeigt. <br>

Für die Schneemänner, die Blöcke und die Musik wird die Datenstruktur list verwendet. <br>
Grob zusammengefasst funktioniert die Schneemann-Liste so: <br>
Ein zufälliges Element der Liste wird gelöscht. Dann wird ein neuer, an zufälliger Position platzierter Scheemann ans Ende der Liste gehangen. Im Laufe des Spiels kommen immer mehr Schneemänner dazu. <br>

<h2>Benutzerhinweise</h2>

Die Steuerung mag einem anfangs etwas komisch vorkommen, da man ziemlich einfach anfängt zu driften. Wir hoffen, dass es nicht zu schlimm ist. <br>

Außerdem gibt es kein traditionelles "Ziel" oder auch "Goal" dieses Spiels, genießen Sie einfach ihre Fahrt. <br>

<h2>Steuerung:</h2>

- Motor beschleunigen: o
- Motor entschleunigen: l
- Lenken schwach: a - d
- Lenken stark: q - e
- Bremsen: SPACE 
- Infos: + / - 
- Programm schließen: z

<h4>Musikplayer-Steuerung</h4>

- <b>PAUSE</b>-Button drücken um den derzeitigen Track zu stoppen
- <b>PLAY</b>-Button drücken um den derzeitigen, pausierten Track von Anfang an zu starten
- <b>SKIP</b>-Button drücken um vom derzeitigen, pausierten Track zum Nächsten zu springen

<h2>Konsole</h2>

Wenn man im Hauptfenster + oder - drückt, ändern sich die in der Konsole angezeigten Werte. Es gibt: 

- Koordinaten
- Geschwindigkeiten
- Beschleunigungen
- Winkel


<h2>Code-Einblick</h2>

Wir laden Sie dazu ein, sich den Code anzusehen. <br>
Wir hoffen, dass dieser einigermaßen verständlich ist. <br>


<h2>Probleme</h2>
Eine große Herausforderung war die GLOOP Bibliothek. Diese bietet KEINE Möglichkeit die Kamera um ihre eigene Achse zu drehen. (Die Rotieren-Funktion funktioniert grundsätzlich nicht) <br>
Selbst das Kamera Ausrichten bereitete aufgrund dazu fehlender Funktionen (im Gegensatz zu Objekten) noch größere Probleme. <br>
Außerdem funktioniert die Bibliothek auf einigen Computern nicht. Woran es genau liegt, bleibt ein Rätsel. Wir hoffen, dass es auf ihrem PC funktioniert. <br>
Eventuell hat es mit der Reihenfolge der importierten Bibliotheken zu tun, versuchen Sie es bei Nichtfunktionieren der GLOOP-Bibliothek damit: <br>

[SCREENSHOT](https://github.com/LukasGasp/racing/blob/main/Screenshot.png)
