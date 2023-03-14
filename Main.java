import java.util.Random;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import GLOOP.*;
import basis.*;

public class Main
{   
    // Objekte
    private Landschaft landschaft;
    private Player spieler;
    private Clip clip;
    private AudioInputStream inputStream;
    private Random rand;

    private Fenster debugFenster;
    private JFrame intro;
    private Bild spielericon;
    private JLabel trackname;
    private JLabel nexttrack;
    private Button b1;
    private Button b2;
    private Button b3;
    private Stift s1;
    private Stift s2;
    private Stift sm;

    // io

    private GLTastatur t; // Tastatur

    // Variablen
    private boolean debug;
    private volatile boolean stopthread;
    private boolean musicpaused;
    private boolean running;
    private int gametick;
    private int seite;
    private int buttoncooldown;
    private int schneemannnumber;
    private int buildingnumber;
    private String currentsong;

    //Listen
    private List<Schneemann> enemylist;
    private List<String> musiklist;
    private List<Building> buildinglist;

    public static void main(String[] args)
    {
      Main main = new Main();
      main.setup();
    }

    private void intro() {
        intro = new JFrame();
        intro.setSize(1920,1080);
        intro.setAlwaysOnTop(true);
        intro.setUndecorated(true);

        Image importedImage = null;
        try {
            importedImage = ImageIO.read(new File("racing.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage buffered = (BufferedImage) importedImage;
        int scaleX = (int) (1920);
        int scaleY = (int) (1080);
        Image finalImage = buffered.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);

        JLabel picLabel = new JLabel(new ImageIcon(finalImage), JLabel.CENTER);

        intro.add(picLabel);
        intro.setVisible(true);
    }
    
    
    private void setup()
    {   
        intro();
        
        // CMD Output verhindern
        //System.setOut(null);   Crasht
        //System.setErr(null);   Crasht
        
        Hilfe.pause(1000);
        t = new GLTastatur();
        rand = new Random();
        
        setupminimap();
        intro.toFront();
        setupdebugfenster();
        intro.toFront();
        setupmusicplayer();
        

        spieler = new Player();
        intro.toFront();
        Hilfe.pause(500);
        landschaft = new Landschaft();
        
        debug = false;
        schneemannnumber = 50;
        buildingnumber = 50;

        enemylist = new List<>();
        for (int i = 0; i < schneemannnumber; i++) { // Populate List
            enemylist.append(newSchneemann(-10000, 10000));
        }
        buildinglist = new List<>();
        for (int i = 0; i < buildingnumber; i++) { // Populate List
            buildinglist.append(newBuilding(-10000, 10000));
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
        Thread musicthread = new Thread(new Runnable() {
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
        Fenster minimap = new Fenster(400,400);
        minimap.setzeTitel("Minimap");
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
        debugFenster.setzeSichtbar(debug);
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

        b1 = new Button(20, 20, "buttons/play.png");
        b2 = new Button(20, 20, "buttons/pause.png");
        b3 = new Button(20, 20, "buttons/next.png");

        musiklist = new List<>();
        musiklist.append("racing.wav");
        musiklist.append("hotline-bling.wav");
        musiklist.append("ac-dc-highway-to-hell-official-video.wav");
        musiklist.append("rankka-insane.wav");
        musiklist.append("herzbeben.wav");

        JDialog musicframe = new JDialog();
        musicframe.setUndecorated(true);
        musicframe.setSize(400, 200);
        musicframe.setLocation(1000, 400);
        musicframe.setVisible(true);
        
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());

        c.gridy = 0;
        leftPanel.add(b1.add(),c);
        c.gridy = 1;
        leftPanel.add(b2.add(),c);
        c.gridy = 2;
        leftPanel.add(b3.add(),c);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        
        c.gridx = 0;
        c.gridy = 0;
        
        centerPanel.add(new JLabel("Now playing:"),c);
        musiklist.toFirst();
        trackname = new JLabel(musiklist.getContent());
        c.gridy = 1;
        centerPanel.add(trackname,c);
        c.gridy = 2;
        centerPanel.add(new JLabel("Next:"),c);
        musiklist.next();
        nexttrack = new JLabel(musiklist.getContent());
        c.gridy = 3;
        centerPanel.add(nexttrack,c);

        musicframe.add(leftPanel,BorderLayout.WEST);
        musicframe.add(centerPanel,BorderLayout.CENTER);
        musicframe.setVisible(true);


        buttoncooldown = 0;
        stopthread = false;
        musicpaused = false;
    }

    private void kollision(){
        
        buildinglist.toFirst();
        while (buildinglist.hasAccess()) {
            if( Math.abs(spieler.getx() - buildinglist.getContent().getx()) <= 250
            && Math.abs(spieler.getz() - buildinglist.getContent().getz()) <= 250){
                spieler.setx(0);
                spieler.setz(0);
                spieler.setpower(0);
                spieler.sethorizontalvelocity(0);
            }
            buildinglist.next();
        }
        
        if(spieler.kollision()){
            spieler.setx(0);
            spieler.setz(0);
            spieler.setpower(0);
            spieler.sethorizontalvelocity(0);
        }
    
    }
    
    
    private void fuehreaus(){
        running = true;
        System.out.println("Im Spiel + / - drücken, um durch Infos zu stöbern.");
        intro.dispose();
        while(running){
            
            gametick++;
            if(debug){
                debugfenster();
            }
            
            //in zufälligen zeitlichen Abständen werden Schneemänner zufällig entfernt
            if(rand.nextInt(1000) == 0){
                deleterandomSchneemann(rand);
            }
            
            //Kamera wird bewegt
            spieler.bewegdich();
            spielericon.setzePosition(
                            spieler.getx()/50.00 + 200 - spielericon.breite()/2.00, 
                            spieler.getz()/50.00 + 200 - spielericon.hoehe()/2.00);
            spielericon.setzeBildWinkelOhneGroessenAnpassung(-spieler.gethorizontalcameraangle());

            checkSchneemann();
            kollision();
            konsole();
            musicplayerbuttons();
            tastatur();
            Hilfe.pause(20);
        }
        Sys.beenden();
    }

    private void drawlines() {
        s1.setzeFarbe(Farbe.HELLGRAU);                
        for (int i = 0; i <= 20; i++) {
            s1.hoch();
            s1.bewegeBis(i*50.00, 300);
            s1.runter();
            s1.bewegeBis(i*50.00, 10);
            s1.schreibe(Integer.toString(i));
        }
        s1.hoch();
        s1.setzeFarbe(Farbe.ROT);
    }

    private void checkSchneemann() {
        //Es wird geguckt ob die Schneemänner in einem Radius von 50 vom Auto sind
        enemylist.toFirst();
        while (enemylist.hasAccess()) {
            if( Math.abs(spieler.getx() - enemylist.getContent().getx()) <= 50
            && Math.abs(spieler.getz() - enemylist.getContent().getz()) <= 50){
                spieler.sethorizontalvelocity(0);
                deletecurrentSchneemann();
            }
            double dz = (double)enemylist.getContent().getz()-spieler.getz();
            double dx = (double)enemylist.getContent().getx()-spieler.getx();
            double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
            double angle = Math.toDegrees(Math.asin(dz/distance));
            
            enemylist.getContent().drehebis((dx>=0)?angle-90:180-angle-90);
            enemylist.next();
        }
    }

    private Schneemann newSchneemann(int min, int max) {
        int x = min + rand.nextInt(Math.abs(min)+max);
        int z = min + rand.nextInt(Math.abs(min)+max);
        sm.kreis(x/50.00+200, z/50.00+200, 1);
        Schneemann neu = new Schneemann(x,30,z);
        if(rand.nextInt(10)<=1)neu.dreheopferum();
        return neu;
    }
    
    private Building newBuilding(int min, int max) {
        int x = min + rand.nextInt(Math.abs(min)+max);
        int z = min + rand.nextInt(Math.abs(min)+max);
        sm.rechteck(x/50.00+195, z/50.00+195, 10,10);
        return new Building(x,30,z);
    }

    private void deletecurrentSchneemann() {
        sm.setzeFarbe(Farbe.WEISS);
        sm.setzeLinienBreite(3);
        sm.rechteck(enemylist.getContent().getx()/50.00+198, enemylist.getContent().getz()/50.00+198, 4, 4);
        sm.setzeLinienBreite(1);
        sm.setzeFarbe(Farbe.BLAU);
        enemylist.getContent().delete();
        enemylist.setContent(newSchneemann(-5000,5000));
    }

    private void deleterandomSchneemann(Random rand) {
        enemylist.toFirst();
        for (int i = 0; i < rand.nextInt(schneemannnumber); i++){
            enemylist.next();
        }
        deletecurrentSchneemann();
    }

    private void debugfenster() {
        s1.runter();
        s2.runter();
        if(gametick>1000){
            debugFenster.loescheAlles();
            gametick=0;
            drawlines();
            s1.hoch();
            s2.hoch();
        }
        s1.bewegeBis(gametick, 300-spieler.gethorizontalvelocity());
        s2.bewegeBis(gametick, 150-2.5*spieler.getbeta());
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
                System.out.println(spieler.gethorizontalvelocity()+ " " +spieler.gettemp());
                break;

                case 2:
                System.out.println("Beschleunigungen:");
                System.out.println();
                System.out.println("Horizonatal / Seitlich:");
                System.out.println(spieler.gethorbeschl()+ " " + spieler.getperpendicularacc());
                System.out.println();
                break;

                case 3:
                System.out.println("Winkel:");
                System.out.println("Beta");
                System.out.println(spieler.getbeta());
                System.out.println(" Horizonatal:");
                System.out.println(spieler.gethorizontalmovementangle());
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
                case ' ':
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
        if(b1.getpressed()&&buttoncooldown==0&&musicpaused)
            {
                b1.setPressed(false);
                musicpaused = false;
                System.out.println(currentsong);
                setupaudio(currentsong, 1);
                buttoncooldown++;
            }
        if(b2.getpressed()&&buttoncooldown==0&&!musicpaused)
            {
                b2.setPressed(false);
                stopthread = true;
                musicpaused = true;
                buttoncooldown++;
            }
        if(b3.getpressed()&&buttoncooldown==0&&musicpaused)
            {
                b3.setPressed(false);
                trackname.setText(musiklist.getContent());
                currentsong = musiklist.getContent();
                musiklist.next();
                if(musiklist.getContent()==null)musiklist.toFirst();
                nexttrack.setText(musiklist.getContent());
                buttoncooldown++;
            }
        if(buttoncooldown>0)buttoncooldown++;
        if(buttoncooldown>30)buttoncooldown=0;
    }
}
