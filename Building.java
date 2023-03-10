import GLOOP.*;
import basis.*;



public class Building{

  private int x, y, z;
  private GLQuader obj;
  public Building(int x,int y,int z){
  
    this.x = x;
    this.y = y;
    this.z = z;
    obj = new GLQuader(x,y,z,500,100,500);
    GLTextur wandtexture = new GLTextur("concrete.jpg");
    obj.setzeTextur(wandtexture);
  }


  public int getx(){
      return (int) x;
  }

  public int gety(){
      return (int) y;
  }

  public int getz(){
      return (int) z;
  }
  
}
