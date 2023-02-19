import GLOOP.*;
public class Player
{   
    // Objekte
    GLKamera kamera0;
    
    private double x;
    private double y;
    private double z;
    private int winkvertglob,masse;
    private double ywinkel,vvert,vside,vhor,horwinkelbewegung,beta,zeit,temp,winkhorglob;
    private long letztezeit,diesezeit;
    private boolean bodenkontakt, bremsen;
    
    double power;
    public Player()
    {
        // Variablen werden initialisiert
        x = -5000;
        y = 30;
        z = 0;
        winkvertglob = 0;
        winkhorglob = 0;
        vhor = 100;
        vvert = 0;
        vside = 0;
        masse = 1000;
        power = 100000;
        horwinkelbewegung = 0;
        bodenkontakt = true;
        letztezeit = System.currentTimeMillis();
        
        // Objekte erzeugen        
        // Kamera:
        kamera0 = new GLKamera(1000,1000);
        
        kamera0.setzePosition(x, y, z);
        kamera0.zeigeAchsen(true);   
        kamera0.setzeBlickpunkt(0, 100, 0);

        
        //kamera0.setzeStereomodus(true); // Stereo!
    }
    
    public void bewegezu(double tempx, double tempy, double tempz){
        kamera0.setzePosition(tempx, tempy, tempz);
        x = tempx;
        y = tempy;
        z = tempz;
    }
    
    public void bewegeum(double tempx, double tempy, double tempz){
        kamera0.setzePosition(x+tempx, y+tempy, z+tempz);
        x = x+tempx;
        y = y+tempy;
        z = z+tempz;
        
    }
    
    public void gehe(double amount){
        kamera0.vor(amount);
        x=kamera0.gibX();
        y=kamera0.gibY();
        z=kamera0.gibZ();
        
    }

    public void bremse() {
        if(bremsen)bremsen = false;
        else bremsen = true;
    }

    public double bremsrate() {
        //Experimentell herausgefundende Werte
        if (vhor>110) {
            temp=0.002;
        } else if(vhor>100) {
            temp=0.005;
        } else if(vhor>70) {
            temp=0.01;
        } else if(vhor>50) {
            temp=0.02;
        } else if(vhor>30) {
            temp=0.03;
        } else if(vhor<8){
            temp=0.0001;  //Bugverhinderung
        } else {
            temp=0.1;
        }
        return temp;
    }
    
    private void neuehorgesch(){
        vhor = Math.sqrt(Math.pow(vhor,2)+Math.pow(vside,2));
        //Beta-Berechnung:
        horwinkelbewegung = horwinkelbewegung + Math.toDegrees(Math.atan(vside/vhor)) 
                                                + ((Math.abs(beta)<1) 
                                                    ?(beta/15) 
                                                    :((Math.abs(beta)<17&&bodenkontakt) 
                                                        ? Math.signum(beta)/4 
                                                        : 0)) 
                                                + ((vhor<160&&bodenkontakt)
                                                    ?Math.signum(beta)/4
                                                    :0);
        beta = winkhorglob - horwinkelbewegung;
        //Beta-Berechnung Ende
        vside = 0;
    }
    
    //Fahrzeugphysics Start
    
    private double horbeschl(){
        return (    ((bremsen)?0:power())
                    - 0.30625 * Math.pow(vhor,2)
                    )
                    /masse;
    }

    public double power() {
        return power * Math.exp(-vhor/200);
        
    }
    
    private double sidebeschl(){
        //Diese Methode dreht die bew. richt. in kam. richt.
        beta = winkhorglob - horwinkelbewegung;
        return (Math.sin(Math.toRadians(beta))*20   
        );
    }
    
    // Überprüft Bodenkontakt
    private boolean abgehoben(){
        if(bodenkontakt){
            if( (beta<-45||beta>45)||  beta<-25&&bremsen||beta>25&&bremsen) return false;
            else return true;
        }
        else{
            if(vhor<5||beta<0.5&&beta>-0.5) return true;
            else return false;
        }
    }
    
    //Fahrzeugphysics Ende
    
    public void bewegdich(){ //Flugzeug wird bewegt
        
        diesezeit = System.currentTimeMillis();
        zeit = diesezeit - letztezeit; //zeit gibt die zeitlichen abstände zwischen durchgängen an, um die Physik akkurat darzustellen

        bodenkontakt = abgehoben();
        vvert = 0;
        
        vhor = (vhor 
                + ( (bodenkontakt)
                    ? (Math.abs(Math.cos(Math.toRadians(beta/2))) * horbeschl() * (zeit/1000)) 
                    : 0) 
                - ( Math.abs(Math.sin(Math.toRadians(beta))))*2
                - ( Math.abs(Math.sin(Math.toRadians(beta/2))) * horbeschl() * (zeit/1000)))
                - 200 * ((bremsen)?bremsrate():0)
                ;

        if(vhor<0.00001&&vhor>-1)vhor=0.00001;
        vside = vside + sidebeschl()*(zeit/1000); 
        
        neuehorgesch();
        
        bewegezu(x + Math.cos(Math.toRadians(horwinkelbewegung))*vhor*(zeit/1000),
        y + vvert*(zeit/1000),
        z  + Math.sin(Math.toRadians(horwinkelbewegung))*vhor*(zeit/1000)
        ); //Die eigentliche Bewegung der Kamera
        
        kamera0.setzeBlickpunkt(x + Math.cos(Math.toRadians(winkhorglob)) * Math.cos(Math.toRadians(winkvertglob)) *100,
                               Math.sin(Math.toRadians(winkvertglob)) * 100 + y, 
                               z  + Math.sin(Math.toRadians(winkhorglob)) * Math.cos(Math.toRadians(winkvertglob)) *100); //Einstellung des Blickwinkels der Kamera
        x=kamera0.gibX();
        y=kamera0.gibY();
        z=kamera0.gibZ(); //Kamerakoordinaten werden regelmäßig wieder abgefragt
        letztezeit = System.currentTimeMillis();
    }
    
    //Winkel der Kamera Methoden
    
    public void steer(double winkel) {
        if(vhor<10)return;
        yaw(winkel);
    }

    public void yaw(double winkel){
        
        winkhorglob = winkhorglob + winkel;
        ywinkel = Math.sin(Math.toRadians(winkvertglob)) * 100 + y;
        kamera0.setzeBlickpunkt(x+ Math.cos(Math.toRadians(winkhorglob))* Math.cos(Math.toRadians(winkvertglob)) *100,ywinkel, z + Math.sin(Math.toRadians(winkhorglob))* Math.cos(Math.toRadians(winkvertglob))*100);
        x=kamera0.gibX();
        y=kamera0.gibY();
        z=kamera0.gibZ();
    }

    //Die methode bleibt weil debuggen
    public void pitch(double winkel){
        
        winkvertglob = winkvertglob + (int)winkel;
        ywinkel = Math.sin(Math.toRadians(winkvertglob)) * 100 + y;
        kamera0.setzeBlickpunkt(x + Math.cos(Math.toRadians(winkhorglob)) * Math.cos(Math.toRadians(winkvertglob)) *100,
                               Math.sin(Math.toRadians(winkvertglob)) * 100 + y, 
                               z  + Math.sin(Math.toRadians(winkhorglob)) * Math.cos(Math.toRadians(winkvertglob)) *100);
        x=kamera0.gibX();
        y=kamera0.gibY();
        z=kamera0.gibZ();
    }

    public boolean kollision() {
        if(x<-9997||x>9997||z<-9997||z>9997) return true;
        else return false;
    }

    
    // Getter
    // Um Probleme mit Variablentypen vorzubeugen
    
    public int getx(){
        return (int) x;
    }
    
    public int gety(){
        return (int) y;
    }
    
    public int getz(){
        return (int) z;
    }
    
    public double getvvert(){
        return  vvert;
    }
    
    public double getvhor(){
        return  vhor;
    }
    
     public double getbeta(){
        return  beta;
    }
    
     public double gettemp(){
        return  temp;
    }
    
    
     public double gethorwinkelbewegung(){
        return  horwinkelbewegung;
    }
    
     public double getvside(){
        return  vside;
    }
    
    public double getpower(){
        return  power;
    }
    
    public double gethorbeschl(){
        return horbeschl();
    }
    
    public double getsidebeschl(){
        return sidebeschl();
    }

    public double getwinkhorglob(){
        return winkhorglob;
    }
    
    
    //<s>ein</s> zwei setter
    public void setpower(double temp){
        power = temp;
    }

    public void setvhor(double temp) {
        vhor = temp;
    }
    
    public void setx(double temp) {
        x = temp;
    }
    
    public void setz(double temp) {
        z = temp;
    }
}
