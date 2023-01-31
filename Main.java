import java.util.Random;

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

    Fenster debugFenster;
    Stift s,s2;
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
        for (int i = 0; i < 50; i++) { // Populate List
            enemy = new Schneemann(
            -50000 + spieler.getx() + rand.nextInt(100000),
            50,
            -50000 + spieler.getz() + rand.nextInt(100000)
            );
            enemylist.append(enemy);
        }
        this.fuehreaus();
    }

    private void setupdebugfenster() {
        debugFenster = new Fenster("DEBUG",1000,300);
        gametick = 0;
        s = new Stift(debugFenster);
        s2 = new Stift(debugFenster);

        s2.hoch();
        s2.setzeFarbe(Farbe.BLAU);
        s2.bewegeBis(10, 20);
        s2.schreibe("Beta");
        drawlines();
        s2.bewegeBis(0, 300);
        s2.runter();
        
        s.hoch();
        s.setzeFarbe(Farbe.ROT);
        s.bewegeBis(10, 10);
        s.schreibe("Speed");
        drawlines();
        s.bewegeBis(0, 300);
        s.runter();
    }

    private void drawlines() {
        s.setzeFarbe(Farbe.HELLGRAU);                
        for (int i = 0; i <= 20; i++) {
            s.hoch();
            s.bewegeBis(i*50, 300);
            s.runter();
            s.bewegeBis(i*50, 10);
            s.schreibe(Integer.toString(i));
        }
        s.hoch();
        s.setzeFarbe(Farbe.ROT);
    }

    private void fuehreaus(){
        running = true;
        Random rand = new Random();
        System.out.println("Im Spiel + / - drücken, um durch Infos zu stöbern.");
        
        while(running){
            //DebugFenster
            s.runter();
            s2.runter();
            gametick++;
            if(gametick>1000){
                debugFenster.loescheAlles();
                gametick=0;
                drawlines();
                s.hoch();
                s2.hoch();
            }
            s.bewegeBis(gametick, 300-spieler.getvhor());
            s2.bewegeBis(gametick, 150-3*spieler.getbeta());

            //if(gametick==100)spieler.bremse();
            Hilfe.pause(20);
            
            //in zufälligen abständen werden schneemänner zufällig entfernt
            if(rand.nextInt(1000) == 0){
                enemylist.toFirst();
                enemy = new Schneemann(
                -5000 + spieler.getx() + rand.nextInt(10000),
                50,
                -5000 + spieler.getz() + rand.nextInt(10000)
                ); //Neuer Schneemann wird erzeugt
                for (int i = 0; i < 0 + rand.nextInt(49); i++){
                    enemylist.next();
                }
                enemylist.getContent().delete();
                enemylist.setContent(enemy);
            }
            
            //Kamera wird bewegt
            spieler.bewegdich();
            enemylist.toFirst();
            //Es wird geguckt ob die Schneemänner in einem Radius von 500 vom flugzeug sind
            
            /*
            while (enemylist.hasAccess()) {
                //System.out.println(enemylist.getContent().getx());
                if(spieler.getx() <= enemylist.getContent().getx()+500
                && spieler.getx() >= enemylist.getContent().getx()-500
                && spieler.gety() <= enemylist.getContent().gety()+500
                && spieler.gety() >= enemylist.getContent().gety()-500 
                && spieler.getz() <= enemylist.getContent().getz()+500
                && spieler.getz() >= enemylist.getContent().getz()-500){
                   Sys.erstelleAusgabe("Gratulation"); 
                }
                enemylist.next();
            }
            */
            
            //Einzelne Konsolenseiten werden angezeigt(zum debugging)
            switch(seite)
            {
                case 4:
                System.out.println();
                System.out.println();
                System.out.println("Koordinaten:");
                System.out.println(spieler.getx());
                System.out.println(spieler.gety());
                System.out.println(spieler.getz());
                break;

                case 1:
                System.out.println();
                System.out.println();
                System.out.println("Geschwindigkeiten:");
                System.out.println("Power:");
                System.out.println(spieler.getpower());
                System.out.println("Horizontal/temp:");
                System.out.println(spieler.getvhor()+ " " +spieler.gettemp());
                break;

                case 2:
                System.out.println();
                System.out.println();
                System.out.println("Beschleunigungen:");
                System.out.println();
                System.out.println("Horizonatal / Seitlich:");
                System.out.println(spieler.gethorbeschl()+ " " + spieler.getsidebeschl());
                System.out.println();
                break;

                case 3:
                System.out.println();
                System.out.println();
                System.out.println("Winkel:");
                System.out.println("Alpha / Beta / Gamma:");
                System.out.println(spieler.getalpha() + " " + spieler.getbeta() + " " + spieler.getgamma());
                System.out.println(" Horizonatal:");
                System.out.println(spieler.gethorwinkelbewegung());
                break;

                case 0:
                System.out.println();
                System.out.println();
                
                break;
            }
            
            if(t.wurdeGedrueckt()){       //Steuerung
                switch(t.gibZeichen()){
                    case 'a':
                        spieler.yaw(-0.5);
                        break;
                    case 'd':
                        spieler.yaw(0.5);
                        break;
                    case 'q':
                        spieler.yaw(-1);
                        break;
                    case 'e':
                        spieler.yaw(1);
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
        Sys.beenden();
    }
}
