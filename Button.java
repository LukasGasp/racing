import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

public class Button implements ActionListener{

    JButton button;
    boolean pressed;
    int x, y, mx, my;
    JLabel picLabel;
    

    public Button(int x, int y, int mx, int my, String pfad){
        pressed = false;
        Image importedImage = null;
        try {
            importedImage = ImageIO.read(new File(pfad));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage buffered = (BufferedImage) importedImage;
        int scaleX = (int) (mx);
        int scaleY = (int) (my);
        Image finalImage = buffered.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);

        picLabel = new JLabel(new ImageIcon(finalImage), JLabel.CENTER);
        picLabel.setVerticalAlignment(JLabel.TOP);
        this.x = x;
        this.y = y;
        this.mx = mx;
        this.my = my;
        button = new JButton(new ImageIcon(finalImage));
        button.addActionListener(this); 
    }

    public JButton add() {
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button){
            pressed = true;
            System.out.println("Button geklickt!");
        }
    } 

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean getpressed() {
        return pressed;
    }
}
