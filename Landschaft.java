import GLOOP.*;
public class Landschaft
{
    //objekte werden erzeugt
    private GLLicht licht1,licht2;
    private GLBoden boden;
    private GLHimmel himmel;
    private Schneemann schneemann1,schneemann2;
    private GLTextur textur;  
    private GLTextur himmeltextur;
    private GLQuader wand1,wand2,wand3,wand4;
    
    
    public Landschaft()
    {
        //Lichter werden erzeugt 
        licht1 = new GLLicht();
        licht1.drehe(20, 20, 20);        
        licht2 = new GLLicht();        
        licht2.setzeDrehung(10, 180, 0);
        licht2.setzePosition(100, 100, 100); //habe ein zweites licht für smoothere beleuchtung gemacht
        //objekte werden konstruiert
        textur = new GLTextur("race1.jfif");  //textur für den boden   
        
        himmeltextur = new GLTextur("sky.jpg");
        boden = new GLBoden(textur); //textur wird sofort angewandt
        himmel = new GLHimmel(himmeltextur);

        wand1 = new GLQuader(0, 0, 10000, 20000, 200, 10);
        wand2 = new GLQuader(10000, 0, 0, 10, 200, -20000);
        wand3 = new GLQuader(-10000, 0, 0, 10, 200, 20000);
        wand4 = new GLQuader(0, 0, -10000, -20000, 200, 10);
    }
    
    
}
