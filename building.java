import GLOOP.*;
import basis.*;

private int x, y, z;
private GLQuader obj;

public class building{

  public building(int x,int y,int z){
  
    this.x = x;
    this.y = y;
    this.z = z;
    obj = new GLQuader(x,y,z,100,100,100);
    GLTexture wandtexture = new GLTextur("concrete.jpg");
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
