import GLOOP.*;
public class Player
{   
    // Objekte
    GLKamera kamera; //Dieses Objekt darf nicht private sein
    
    private double x;
    private double y;
    private double z;
    private int winkvertglob;
    private int masse;
    private double ywinkel;
    private double vvert;
    private double perpendicularvelocity;
    private double horizontalvelocity;
    private double horizontalmovementangle;
    private double beta;
    private double temp;
    private double horizontalcameraangle;
    private long letztezeit;
    private boolean bodenkontakt;
    private boolean bremsen;
    
    double power;
    public Player()
    {
        // Variablen werden initialisiert
        x = -5000;
        y = 30;
        z = 0;
        winkvertglob = 0;
        horizontalcameraangle = 0;
        horizontalvelocity = 10;
        vvert = 0;
        perpendicularvelocity = 0;
        masse = 1000;
        power = 0;
        horizontalmovementangle = 0;
        bodenkontakt = true;
        
        
        // Objekte erzeugen        
        // Kamera:
        kamera = new GLKamera(1000,1000);
        
        kamera.setzePosition(x, y, z);
        kamera.zeigeAchsen(false);   
        kamera.setzeBlickpunkt(0, 100, 0);


        //Jetzt neu: Veränderte Variablennamen
        //Die folgenden Zeilen sind Orientierungshilfen fuer uns
        //horwinkelbewegung = horizontalmovementangle
        //vhor = horizontalvelocity
        //vside = perpendicularvelocity
        //sidebeschl = perpendicularacc
        //winkhorglob = horizontalcameraangle

        letztezeit = System.currentTimeMillis();
    }
    
    public void bewegezu(double tempx, double tempy, double tempz){
        kamera.setzePosition(tempx, tempy, tempz);
        x = tempx;
        y = tempy;
        z = tempz;
    }
    
    public void bewegeum(double tempx, double tempy, double tempz){
        kamera.setzePosition(x+tempx, y+tempy, z+tempz);
        x = x+tempx;
        y = y+tempy;
        z = z+tempz;
        
    }
    
    public void gehe(double amount){
        kamera.vor(amount);
        x=kamera.gibX();
        y=kamera.gibY();
        z=kamera.gibZ();
        
    }

    public void bremse() {
        if(bremsen)bremsen = false;
        else bremsen = true;
    }

    public double bremsrate() {
        //Experimentell herausgefundende Werte
        if (horizontalvelocity>110) {
            temp=0.002;
        } else if(horizontalvelocity>100) {
            temp=0.005;
        } else if(horizontalvelocity>70) {
            temp=0.01;
        } else if(horizontalvelocity>50) {
            temp=0.02;
        } else if(horizontalvelocity>30) {
            temp=0.03;
        } else if(horizontalvelocity<8){
            temp=0.0001;  //Bugverhinderung
        } else {
            temp=0.1;
        }
        return temp;
    }
    
    private void neuehorgesch(){
        horizontalvelocity = Math.sqrt(Math.pow(horizontalvelocity,2)+Math.pow(perpendicularvelocity,2));
        //Beta-Berechnung:

        double betaacceleration;

        if ((Math.abs(beta)<1)) {

            betaacceleration = (beta/15);

        } else {
            
            if (Math.abs(beta)<17 && bodenkontakt) {
                betaacceleration = Math.signum(beta)/4;
            } else {
                betaacceleration = 0;
            }

        }

        if (horizontalvelocity<160 && bodenkontakt) {
            betaacceleration = betaacceleration + Math.signum(beta)/4;
        }

        horizontalmovementangle = horizontalmovementangle + Math.toDegrees(Math.atan(perpendicularvelocity/horizontalvelocity)) + betaacceleration ;
                                                
        beta = horizontalcameraangle - horizontalmovementangle;
        //Beta-Berechnung Ende
        perpendicularvelocity = 0;
    }
    
    //Fahrzeugphysics Start
    
    private double horbeschl(){
        return (    ((bremsen)?0:power())
                    - 0.30625 * Math.pow(horizontalvelocity,2)
                    )
                    /masse;
    }

    public double power() {
        return power * Math.exp(-horizontalvelocity/200);
        
    }
    
    private double perpendicularacc(){
        //Diese Methode dreht die bew. richt. in kam. richt.
        beta = horizontalcameraangle - horizontalmovementangle;
        return (Math.sin(Math.toRadians(beta))*20   
        );
    }
    
    // Überprüft Bodenkontakt
    private boolean abgehoben(){
        if(bodenkontakt){
            return !((beta<-45||beta>45)||  beta<-25&&bremsen||beta>25&&bremsen);
        }
        else{
            return (horizontalvelocity<5||beta<0.5&&beta>-0.5);
        }
    }
    
    //Fahrzeugphysics Ende
    
    public void bewegdich(){ //Flugzeug wird bewegt
        
        double diesezeit = System.currentTimeMillis();
        double zeit = diesezeit - letztezeit; //zeit gibt die zeitlichen abstände zwischen durchgängen an, um die Physik akkurat darzustellen

        bodenkontakt = abgehoben();
        vvert = 0;
        
        double acceleration = ((bodenkontakt)
                            ? (Math.abs(Math.cos(Math.toRadians(beta/2))) * horbeschl() * (zeit/1000)) 
                            : 0);



        horizontalvelocity = (horizontalvelocity 
                + acceleration
                - ( Math.abs(Math.sin(Math.toRadians(beta)))) * 2
                - ( Math.abs(Math.sin(Math.toRadians(beta/2))) * horbeschl() * (zeit/1000)))
                - 200 * ((bremsen)?bremsrate():0)
                ;

        if (horizontalvelocity<0.00001&&horizontalvelocity>-1) horizontalvelocity=0.00001;
        perpendicularvelocity = perpendicularvelocity + perpendicularacc()*(zeit/1000); 
        
        neuehorgesch();
        
        bewegezu(x + Math.cos(Math.toRadians(horizontalmovementangle))*horizontalvelocity*(zeit/1000),
        y + vvert*(zeit/1000),
        z  + Math.sin(Math.toRadians(horizontalmovementangle))*horizontalvelocity*(zeit/1000)
        ); //Die eigentliche Bewegung der Kamera
        
        kamera.setzeBlickpunkt(x + Math.cos(Math.toRadians(horizontalcameraangle)) * Math.cos(Math.toRadians(winkvertglob)) *100,
                               Math.sin(Math.toRadians(winkvertglob)) * 100 + y, 
                               z  + Math.sin(Math.toRadians(horizontalcameraangle)) * Math.cos(Math.toRadians(winkvertglob)) *100); //Einstellung des Blickwinkels der Kamera
        x=kamera.gibX();
        y=kamera.gibY();
        z=kamera.gibZ(); //Kamerakoordinaten werden regelmäßig wieder abgefragt
        letztezeit = System.currentTimeMillis();
    }
    
    //Winkel der Kamera Methoden
    
    public void steer(double winkel) {
        if(horizontalvelocity<10)return;
        yaw(winkel);
    }

    public void yaw(double winkel){
        
        horizontalcameraangle = horizontalcameraangle + winkel;
        ywinkel = Math.sin(Math.toRadians(winkvertglob)) * 100 + y;
        kamera.setzeBlickpunkt(x+ Math.cos(Math.toRadians(horizontalcameraangle))* Math.cos(Math.toRadians(winkvertglob)) *100,ywinkel, z + Math.sin(Math.toRadians(horizontalcameraangle))* Math.cos(Math.toRadians(winkvertglob))*100);
        x=kamera.gibX();
        y=kamera.gibY();
        z=kamera.gibZ();
    }

    //Die methode bleibt weil debuggen
    public void pitch(double winkel){
        
        winkvertglob = winkvertglob + (int)winkel;
        ywinkel = Math.sin(Math.toRadians(winkvertglob)) * 100 + y;
        kamera.setzeBlickpunkt(x + Math.cos(Math.toRadians(horizontalcameraangle)) * Math.cos(Math.toRadians(winkvertglob)) *100,
                               Math.sin(Math.toRadians(winkvertglob)) * 100 + y, 
                               z  + Math.sin(Math.toRadians(horizontalcameraangle)) * Math.cos(Math.toRadians(winkvertglob)) *100);
        x=kamera.gibX();
        y=kamera.gibY();
        z=kamera.gibZ();
    }

    public boolean kollision() {
        return (x<-9997||x>9997||z<-9997||z>9997);
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
    
    public double gethorizontalvelocity(){
        return  horizontalvelocity;
    }
    
     public double getbeta(){
        return  beta;
    }
    
     public double gettemp(){
        return  temp;
    }
    
     public double gethorizontalmovementangle(){
        return  horizontalmovementangle;
    }
    
     public double getperpendicularvelocity(){
        return  perpendicularvelocity;
    }
    
    public double getpower(){
        return  power;
    }
    
    public double gethorbeschl(){
        return horbeschl();
    }
    
    public double getperpendicularacc(){
        return perpendicularacc();
    }

    public double gethorizontalcameraangle(){
        return horizontalcameraangle;
    }
    
    
    //<s>ein</s> zwei setter
    public void setpower(double power){
        this.power = power;
    }

    public void sethorizontalvelocity(double horizontalvelocity) {
        this.horizontalvelocity = horizontalvelocity;
    }
    
    public void setx(double x) {
        this.x = x;
    }
    
    public void setz(double z) {
        this.z = z;
    }
}
