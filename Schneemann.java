import GLOOP.*;
import basis.*;

public class Schneemann
{
    //objekte werden erzeugt
    private GLKugel kugel1;
    private GLKugel kugel2;
    private GLKugel kugel3;
    private GLKugel auge1;
    private GLKugel auge2;
    private GLZylinder zylinder1;
    private GLZylinder zylinder2;
    private GLKegel kegel;
    private GLTextur textur;
    private GLTextur texturleder;
    private GLTextur texturkarotte;
    private GLTextur texturstein;
    
    int x;
    int y;
    int z;
    double winkel;
    
    
    public Schneemann(int x,int y,int z)
    {
        //ints werden übergeben
        this.x = x;
        this.y = y;
        this.z = z;
        
        //texturen werden erzeugt
        textur = new GLTextur("schnee.jfif");
        texturleder = new GLTextur("leder.jfif");
        texturkarotte = new GLTextur("karotte.jfif");
        texturstein = new GLTextur("stein.jfif");
        //3d-objekte werden erzeugt
        kugel1 = new GLKugel(x,y,z,35);        
        kugel2 = new GLKugel(x,y+45.00,z,20);
        kugel3 = new GLKugel(x,y+70.00,z,10);
        auge1 = new GLKugel(x+4.00,y+73.00,z-9.00,1); //bei den augen sind sehr genaue werte nötig
        auge2 = new GLKugel(x-4.00,y+73.00,z-9.00,1);
        kegel = new GLKegel(x,y+70.00,z-10.00,4,20);
        zylinder1 = new GLZylinder(x,y+82.00,z,10,10);
        zylinder2 = new GLZylinder(x,y+77.00,z,20,1);
        //zylinder wird gedreht
        zylinder1.drehe(90, 0, 0);
        zylinder2.drehe(90, 0, 0);
        //texturen werden auf den objekten angewandt
        zylinder1.setzeTextur(texturleder);
        zylinder2.setzeTextur(texturleder);
        kegel.setzeTextur(texturkarotte);
        kugel1.setzeTextur(textur);
        kugel2.setzeTextur(textur);
        kugel3.setzeTextur(textur);
        auge1.setzeTextur(texturstein);
        auge2.setzeTextur(texturstein);
    }    
    
    public int getx()
    {
        return x;
    }
    
    public int gety()
    {
        return y;
    }
    
    public int getz()
    {
        return z;
    }
    
    public void dreheum(double w) {
        winkel = winkel + w;
        auge1.drehe(0, -w, 0);
        auge2.drehe(0, -w, 0);
        kegel.drehe(0, -w, 0);
        auge1.setzePosition(x + Math.sin(Math.toRadians(winkel + 8.44)) * 10 , y + 73.00 , z - Math.cos(Math.toRadians(winkel + 8.44)) * 10);
        auge2.setzePosition(x + Math.sin(Math.toRadians(winkel - 8.44)) * 10 , y + 73.00 , z - Math.cos(Math.toRadians(winkel - 8.44)) * 10);
        kegel.setzePosition(x + Math.sin(Math.toRadians(winkel)) * 10, y + 70.00 , z - Math.cos(Math.toRadians(winkel)) * 10);
    }

    public void drehebis(double w) {
        double deltaw = w - winkel;
        winkel = w;
        auge1.drehe(0, -deltaw, 0);
        auge2.drehe(0, -deltaw, 0);
        kegel.drehe(0, -deltaw, 0);
        auge1.setzePosition(x + Math.sin(Math.toRadians(winkel + 8.44)) * 10 , y + 73.00 , z - Math.cos(Math.toRadians(winkel + 8.44)) * 10);
        auge2.setzePosition(x + Math.sin(Math.toRadians(winkel - 8.44)) * 10 , y + 73.00 , z - Math.cos(Math.toRadians(winkel - 8.44)) * 10);
        kegel.setzePosition(x + Math.sin(Math.toRadians(winkel)) * 10, y+70.00,  z - Math.cos(Math.toRadians(winkel)) * 10);
    }

    public void delete(){
        // GLOOP aktualisiert beim Loschen nicht die Sichtbarkeit
        kugel1.setzeSichtbarkeit(false);
        kugel2.setzeSichtbarkeit(false);
        kugel3.setzeSichtbarkeit(false);
        auge1.setzeSichtbarkeit(false);
        auge2.setzeSichtbarkeit(false);
        kegel.setzeSichtbarkeit(false);
        zylinder1.setzeSichtbarkeit(false);
        zylinder2.setzeSichtbarkeit(false);
        Hilfe.pause(5); // Sonst ist das Unsichtbar machen noch nicht fertig. (Wussten nicht, dass java multithreaded ist!?)
        kugel1.loesche();
        kugel2.loesche();
        kugel3.loesche();
        auge1.loesche();
        auge2.loesche();
        kegel.loesche();
        zylinder1.loesche();
        zylinder2.loesche();
        
        // Garbage Collector manuell dazu anregen seinen Job zu machen. (Hat FPS massiv verbessert)
    }
}
