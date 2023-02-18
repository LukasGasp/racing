import GLOOP.*;

public class Ziel {

    private int x,y,z;
    private GLZylinder zylinder1;

    public Ziel(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        zylinder1 = new GLZylinder(x, y, z, 10, 20);
        zylinder1.setzeFarbe(0, 255, 0);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
}
