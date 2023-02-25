import GLOOP.*;
public class Landschaft
{
    //objekte werden erzeugt
    private GLLicht licht1;
    private GLLicht licht2;
    private GLBoden boden;
    private GLHimmel himmel;
    private Schneemann schneemann1;
    private Schneemann schneemann2;
    private GLTextur textur;  
    private GLTextur himmeltextur;
    private GLTextur wandtexture;
    private GLQuader wand1;
    private GLQuader wand2;
    private GLQuader wand3;
    private GLQuader wand4;
    
    
    public Landschaft()
    {
        //Lichter werden erzeugt 
        licht1 = new GLLicht();
        licht1.drehe(20, 20, 20);        
        licht2 = new GLLicht();        
        licht2.setzeDrehung(10, 180, 0);
        licht2.setzePosition(100, 100, 100); //habe ein zweites licht für smoothere beleuchtung gemacht
        //objekte werden konstruiert
        textur = new GLTextur("asphalt.jpg");  //textur für den boden   
        
        himmeltextur = new GLTextur("sky.jpg");
        boden = new GLBoden(textur); //textur wird sofort angewandt
        himmel = new GLHimmel(himmeltextur);
        
        wandtexture = new GLTextur("concrete.jpg");
        wand1 = new GLQuader(0, 0, 10000, 20000, 200, 10);
        wand1.setzeTextur(wandtexture);
        wand2 = new GLQuader(10000, 0, 0, 10, 200, -20000);
        wand2.setzeTextur(wandtexture);
        wand3 = new GLQuader(-10000, 0, 0, 10, 200, 20000);
        wand3.setzeTextur(wandtexture);
        wand4 = new GLQuader(0, 0, -10000, -20000, 200, 10);
        wand4.setzeTextur(wandtexture);
    }
    
    
}
