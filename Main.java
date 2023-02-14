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
    int gametick;

    Fenster debugFenster, musicPlayer;
    Bild b1,b2,b3;
    Stift s1;
    Stift s2;
    // io

    GLTastatur t; // Tastatur
    GLMaus m;

    // Variablen

    boolean running;
    int seite;

    // Schneemannliste

    List<Schneemann> enemylist;

    public static void main(String[] args)
    {
      Main main = new Main();
      main.setup();
    }

    private void setup()
    {   
        setupdebugfenster();

        spieler = new Player();
        landschaft = new Landschaft();
        t = new GLTastatur();
        Random rand = new Random();
        
        enemylist = new List<Schneemann>();
        
        Bild b1 = new Bild();

        for (int i = 0; i < 50; i++) { // Populate List
            enemylist.append(newSchneemann(-10000, 10000));
        }
        playSound("racing.wav", 999);
        
        fuehreaus(rand);
    }

    public static synchronized void playSound(final String url, int loops) {
        new Thread(new Runnable() {
          public void run() {
            try {
              Clip clip = AudioSystem.getClip();
              AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                Main.class.getResourceAsStream(url));
              clip.open(inputStream);
              clip.loop(loops);
              clip.start(); 

            } catch (Exception e) {
              System.err.println(e.getMessage());
            }
          }
        }).start();
    }

    private void setupdebugfenster() {
        debugFenster = new Fenster("DEBUG",1000,300);
        musicPlayer = new Fenster("Music",300,300);
        
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
               enemylist.getContent().delete();
               enemylist.setContent(newSchneemann(-5000,5000));
            }
            double IXZI = Math.sqrt(Math.pow(enemylist.getContent().getx()-spieler.getx(), 2) + Math.pow(enemylist.getContent().getz()-spieler.getz(), 2));
            double IZI = enemylist.getContent().getz()-spieler.getz();
            enemylist.getContent().drehebis(Math.toDegrees(Math.asin(IZI/IXZI)));
            enemylist.next();
            
        }
    }

    private Schneemann newSchneemann(int min, int max) {
        Random rand = new Random();
        return new Schneemann(
                min + rand.nextInt(Math.abs(min)+max),
                30,
                min + rand.nextInt(Math.abs(min)+max)
                );
    }

    private void deleterandomSchneemann(Random rand) {
        enemylist.toFirst();
                for (int i = 0; i < rand.nextInt(49); i++){
                    enemylist.next();
                }
                enemylist.getContent().delete();
                enemylist.setContent(newSchneemann(-5000,5000));
    }

    private void fuehreaus(Random rand){
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
            
            checksnowmen();

            if(spieler.kollision()){
                spieler.setx(0);
                spieler.setz(0);
                spieler.setpower(0);
                spieler.setvhor(0);
            }

            //Einzelne Konsolenseiten werden angezeigt(zum debugging)
            if(gametick%50==0)
            {
                konsole();
            }

            tastatur();
            

        }
        Sys.beenden();
    }

    private void konsole() {
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
}
