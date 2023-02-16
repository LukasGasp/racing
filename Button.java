import basis.*;

public class Button {

    Bild b;
    int x, y, mx, my;
    
    Maus m;

    public Button(int x, int y, int mx, int my, String pfad){
        this.x = x;
        this.y = y;
        this.mx = mx;
        this.my = my;
        b = new Bild(pfad, x, y);
        b.setzeGroesse(mx, my);
        m = new Maus();
    }

    public boolean pressed() {
        if(m.istGedrueckt() && 
            m.hPosition()<x+mx && m.hPosition()>x &&
            m.vPosition()<y+my && m.vPosition()>y
            )
            {
            return true;
        }
        else return false;
    }
}
