<h1>Dies ist das Informatik Projekt von Michael und Lukas</h1>

- PROJEKTBEZEICHNUNG:             Flieger  
- PROJEKTZWECK:                   Informatik Arbeit  
- VERSION:                        V.0.5.2  
- WIE IST DAS PROJEKT ZU STARTEN: BlueJ  
- AUTOREN:                        Michael, Lukas  
- BENUTZERHINWEISE:               Sie müssen fliegen können  

<h2>Spielidee:</h2>

Man steuert ein exakt 2000 kg schweres auto, welches eine Kraft von 30000 Newton aufbringen kann. Gemäß folgender Veröffentlichungen der NASA haben wir Formeln extrahiert, verarbeitet und implementiert.

[Informatik.pdf](https://github.com/LukasGasp/flieger/files/7875854/Informatik.pdf)

Man steuert dieses auto durch eine traumhafte Schneelandschaft. Um diese spannender zu machen (und den Arbeitsauftrag zu erfüllen) werden Schneemänner zufällig um den Spieler erzeugt. Das erste Element der Liste wird gelöscht und durch Zufall neu um den Spieler platziert. Der neue Scheemann wird ans Ende der Liste gehangen. Im laufe des Spiels kommen immer mehr Schneemänner dazu. (Bis zu 50). So gibt es Möglichkeit Speedruns zu machen.

<h2>Benutzerhinweise</h2>

<s>Bitte 3D Brille tragen.</s>

<h2>Steuerung:</h2>

<s>Wir gehen davon aus, dass dem Spieler die Gesetzte der Aerodynamik bekannt sind. Hier eine kurze Erinnerung an Kräfte, welche auf ein Flugzeug wirken:</s>

motor beschleunigen: o  
motor entschleunigen: l  
Steuerung: w, a, s, d  
Infos: + / - 
Programm schließen: z

<h2>Konsole</h2>

**WIR EMPFEHLEN UNBEDINGT DIE KONSOLE OFFEN ZU HALTEN!**  
Wenn man im Hauptfenster + oder - drückt ändern sich die anezeigten Werte. Es gibt: 

- Koordinaten
- Geschwindigkeiten
- Beschleunigungen
- Winkel

Diese Werte müssen ALLE vom Spieler im Auge behalten werden. Anderfalls kommt es zu einem kritischen Absturz.

<h2>Code-Einblick</h2>
Eine der Funktionen. <s>Natürlich werden hier Gravitation, Auftrieb, Masse, etc. beachtet. Alles ist relativ,</s>



<h2>Probleme</h2>
Eine große Herausforderung war die GLOOP Bibliothek. Diese bietet KEINE möglichkeit die Kamera um ihre eigene Achse zu drehen. (Die rotieren Funktion funktioniert einfach nicht)
Selbst das Kamera ausrichten bereitete Aufgrund dazu fehlender Funktionen (im Gegensatz zu Objekten) noch größere Probleme.
Wir würden uns freuen für das nächste Projekt eine andere Bibliothek nutzen zu dürfen. 
Trotzdem vielen Dank, dass wir von Basis zu Gloop wechseln durften.
