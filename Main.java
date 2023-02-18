import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import GLOOP.*;
import basis.*;

public class Main
{   
    // Objekte
    Landschaft landschaft;
    Player spieler;
    Schneemann enemy;
    Sys sys;
    Sound ferrari;
    Clip clip;
    static Thread musicthread;
    AudioInputStream inputStream;
    Random rand;

    Fenster debugFenster;
    Fenster musicPlayer;
    Fenster minimap;
    Bild spielericon;
    TextFeld nexttrack;
    TextFeld trackname;
    TextBereich playing;
    TextBereich next;
    Button b1,b2,b3;
    Stift s1;
    Stift s2;
    Stift sm;
    // io

    GLTastatur t; // Tastatur
    GLMaus m;

    // Variablen
    volatile boolean stopthread;
    boolean musicpaused;
    boolean running;
    int gametick;
    int seite;
    int buttoncooldown;
    String currentsong;

    // Schneemannliste
    List<Schneemann> enemylist;
    List<String> musiklist;

    public static void main(String[] args)
    {
      Main main = new Main();
      main.setup();
    }

    private void setup()
    {   
        spieler = new Player();
        landschaft = new Landschaft();
        t = new GLTastatur();
        rand = new Random();
        
        setupminimap();
        setupdebugfenster();
        setupmusicplayer();
        enemylist = new List<>();
        for (int i = 0; i < 50; i++) { // Populate List
            enemylist.append(newSchneemann(-10000, 10000));
        }
        fuehreaus();
    }

    private void setupaudio(String url, int loops) {
        try {
            clip = AudioSystem.getClip();
            inputStream = AudioSystem.getAudioInputStream(
                Main.class.getResourceAsStream(url));
            playSound(loops);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private synchronized void playSound(int loops) {
        musicthread = new Thread(new Runnable() {
          public void run() {
            try {
                stopthread = false;
                clip.open(inputStream);
                clip.loop(loops);
                clip.start();
                while(!stopthread)
                {
                    Thread.sleep(500);
                }
                clip.close();
                stopthread = false;
            } catch (Exception e) {
              System.err.println(e.getMessage());
            }
          }
        });
        musicthread.start();
    }

    private void setupminimap() {
        minimap = new Fenster(400,400);
        minimap.setzePosition(1000, 600);
        sm = new Stift(minimap);
        sm.runter();
        sm.setzeFarbe(Farbe.SCHWARZ);
        sm.fuelleMitFarbe(Farbe.WEISS);
        sm.setzeLinienBreite(4);
        sm.bewegeBis(400, 0);
        sm.bewegeBis(400, 400);
        sm.bewegeBis(0, 400);
        sm.bewegeBis(0, 0);
        sm.setzeLinienBreite(1);
        sm.setzeFarbe(Farbe.BLAU);
        spielericon = new Bild("playericon.png");
        spielericon.setzeGroesse(10, 10);
    }

    private void setupdebugfenster() {
        debugFenster = new Fenster("DEBUG",1000,300);
        debugFenster.setzePosition(1000, 0);
        gametick = 0;
        s1 = new Stift(debugFenster);
        s2 = new Stift(debugFenster);

        s2.hoch();
        s2.setzeFarbe(Farbe.BLAU);
        s2.bewegeBis(10, 20);
        s2.schreibe("Beta");
        drawlines();
        s2.bewegeBis(0, 300);
        s2.runter();
        
        s1.hoch();
        s1.setzeFarbe(Farbe.ROT);
        s1.bewegeBis(10, 10);
        s1.schreibe("Speed");
        drawlines();
        s1.bewegeBis(0, 300);
        s1.runter();
    }

    private void setupmusicplayer() {
        musicPlayer = new Fenster("Music",350,100);
        musicPlayer.setzePosition(1000, 400);
        b1 = new Button( 10 ,10, 20, 20, "buttons/play.png");
        b2 = new Button( 10 ,30, 20, 20, "buttons/pause.png");
        b3 = new Button( 10 ,50, 20, 20, "buttons/next.png");
        playing = new TextBereich(50, 20, 150, 20, musicPlayer);
        playing.entferneRand();
        trackname = new TextFeld(50, 40, 300, 20, musicPlayer);
        next = new TextBereich(50, 60, 150, 20, musicPlayer);
        next.entferneRand();
        nexttrack = new TextFeld(50, 80, 300, 20, musicPlayer);
        
        musiklist = new List<>();
        musiklist.append("racing.wav");
        musiklist.append("ac-dc-highway-to-hell-official-video.wav");
        musiklist.append("rankka-insane.wav");
        musiklist.append("herzbeben.wav");

        musiklist.toFirst();
        currentsong = musiklist.getContent();
        setupaudio(musiklist.getContent(), 1);
        playing.setzeText("Now playing:");
        trackname.setzeText(musiklist.getContent());
        next.setzeText("Next:");
        musiklist.next();
        nexttrack.setzeText(musiklist.getContent());

        buttoncooldown = 0;
        stopthread = false;
        musicpaused = false;
    }

    private void drawlines() {
        s1.setzeFarbe(Farbe.HELLGRAU);                
        for (int i = 0; i <= 20; i++) {
            s1.hoch();
            s1.bewegeBis(i*50, 300);
            s1.runter();
            s1.bewegeBis(i*50, 10);
            s1.schreibe(Integer.toString(i));
        }
        s1.hoch();
        s1.setzeFarbe(Farbe.ROT);
    }

    private void checksnowmen() {
        //Es wird geguckt ob die Schneemänner in einem Radius von 50 vom Auto sind
        enemylist.toFirst();
        while (enemylist.hasAccess()) {
            if(spieler.getx() <= enemylist.getContent().getx()+50
            && spieler.getx() >= enemylist.getContent().getx()-50
            && spieler.getz() <= enemylist.getContent().getz()+50
            && spieler.getz() >= enemylist.getContent().getz()-50){
                spieler.setvhor(0);
                deletecurrentSchneemann();
            }
            double IXZI = Math.sqrt(
                                Math.pow(enemylist.getContent().getx()-spieler.getx(), 2)
                              + Math.pow(enemylist.getContent().getz()-spieler.getz(), 2));
            double IZI = enemylist.getContent().getz()-spieler.getz();
            enemylist.getContent().drehebis(Math.toDegrees(Math.asin(IZI/IXZI))-90);
            sm.fuelle(Farbe.BLAU, Farbe.BLAU);
            sm.kreis(enemylist.getContent().getx()/50+200, enemylist.getContent().getz()/50+200, 1);
            enemylist.next();
        }
    }

    private Schneemann newSchneemann(int min, int max) {
        return new Schneemann(
            min + rand.nextInt(Math.abs(min)+max),
            30,
            min + rand.nextInt(Math.abs(min)+max));
    }

    private void deletecurrentSchneemann() {
        sm.setzeFarbe(Farbe.WEISS);
        sm.setzeLinienBreite(3);
        sm.rechteck(enemylist.getContent().getx()/50+198, enemylist.getContent().getz()/50+198, 4, 4);
        sm.setzeLinienBreite(1);
        sm.setzeFarbe(Farbe.BLAU);
        enemylist.getContent().delete();
        enemylist.setContent(newSchneemann(-5000,5000));
    }

    private void deleterandomSchneemann(Random rand) {
        enemylist.toFirst();
        for (int i = 0; i < rand.nextInt(49); i++){
            enemylist.next();
        }
        deletecurrentSchneemann();
    }

    private void fuehreaus(){
        running = true;
        System.out.println("Im Spiel + / - drücken, um durch Infos zu stöbern.");
        while(running){
            //DebugFenster
            s1.runter();
            s2.runter();
            gametick++;
            if(gametick>1000){
                debugFenster.loescheAlles();
                gametick=0;
                drawlines();
                s1.hoch();
                s2.hoch();
            }
            s1.bewegeBis(gametick, 300-spieler.getvhor());
            s2.bewegeBis(gametick, 150-2.5*spieler.getbeta());

            Hilfe.pause(20);
            
            //in zufälligen abständen werden schneemänner zufällig entfernt
            if(rand.nextInt(1000) == 0){
                deleterandomSchneemann(rand);
            }
            
            //Kamera wird bewegt
            spieler.bewegdich();
            spielericon.setzePosition(spieler.getx()/50+200, spieler.getz()/50+200);
            spielericon.setzeBildWinkelOhneGroessenAnpassung(-spieler.getwinkhorglob()); //ich liebe diesen methodennamen
            checksnowmen();

            if(spieler.kollision()){
                spieler.setx(0);
                spieler.setz(0);
                spieler.setpower(0);
                spieler.setvhor(0);
            }

            konsole();
            musicplayerbuttons();
            tastatur();
        }
        Sys.beenden();
    }

    private void konsole() {
        //Einzelne Konsolenseiten werden angezeigt(zum debugging)
        if(gametick%50==0)
        {
            if(seite!=0)
            {
                for (int i = 0; i < 5; i++) {
                    System.out.println();
                }
            }
            switch(seite)
            {
                case 4:
                System.out.println("Koordinaten:");
                System.out.println(spieler.getx());
                System.out.println(spieler.gety());
                System.out.println(spieler.getz());
                break;

                case 1:
                System.out.println("Geschwindigkeiten:");
                System.out.println("Power:");
                System.out.println(spieler.getpower());
                System.out.println("Horizontal/temp:");
                System.out.println(spieler.getvhor()+ " " +spieler.gettemp());
                break;

                case 2:
                System.out.println("Beschleunigungen:");
                System.out.println();
                System.out.println("Horizonatal / Seitlich:");
                System.out.println(spieler.gethorbeschl()+ " " + spieler.getsidebeschl());
                System.out.println();
                break;

                case 3:
                System.out.println("Winkel:");
                System.out.println("Beta");
                System.out.println(spieler.getbeta());
                System.out.println(" Horizonatal:");
                System.out.println(spieler.gethorwinkelbewegung());
                break;

                default:
                break;
            }
        }
    }

    private void tastatur() {
        //Steuerung
        if(t.wurdeGedrueckt())
        { 
            switch(t.gibZeichen())
            {
                case 'a':
                    spieler.steer(-0.5);
                    break;
                case 'd':
                    spieler.steer(0.5);
                    break;
                case 'q':
                    spieler.steer(-1);
                    break;
                case 'e':
                    spieler.steer(1);
                    break;
                case 'o':
                    if(spieler.getpower() < 100000){
                        spieler.setpower(spieler.power+10000);
                    }
                    break;
                case 'l':
                    if(spieler.getpower() >= 10000){
                        spieler.setpower(spieler.power-10000);
                    }
                    break;
                case ' ': //programm wird per "@" geschlossen
                    spieler.bremse();
                    break;
                case '+':
                    seite++;
                    if(seite>4)seite=0;
                    break;
                case '-':
                    seite--;
                    if(seite<0)seite=4;
                    break;
                case '@': //programm wird per "@" geschlossen
                    running=false;
                    break;
                
                default:
                Sys.erstelleAusgabe("Kein gültiges Zeichen");
                break;
            }
        }
    }

    private void musicplayerbuttons() {
        if(b1.pressed()&&buttoncooldown==0&&musicpaused==true)
            {
                musicpaused = false;
                setupaudio(currentsong, 1);
                buttoncooldown++;
            }
        if(b2.pressed()&&buttoncooldown==0&&musicpaused==false)
            {
                stopthread = true;
                musicpaused = true;
                buttoncooldown++;
            }
        if(b3.pressed()&&buttoncooldown==0)
            {
                trackname.setzeText(musiklist.getContent());
                currentsong = musiklist.getContent();
                musiklist.next();
                if(musiklist.getContent()==null)musiklist.toFirst();
                nexttrack.setzeText(musiklist.getContent());
                buttoncooldown++;
            }
        if(buttoncooldown>0)buttoncooldown++;
        if(buttoncooldown>30)buttoncooldown=0;
    }
}