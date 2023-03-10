import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

public class Button implements ActionListener{

    private JButton button;
    private boolean pressed;
    private int mx;
    private int my; //VSCode sagt, die Variablen wuerden nicht benutzt werden
    private JLabel picLabel;
    

    public Button(int mx, int my, String pfad){
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
        button = new JButton(new ImageIcon(finalImage));
        button.addActionListener(this); 
    }

    public JButton add() {
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button){
            pressed = true;
        }
    } 

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean getpressed() {
        return pressed;
    }
}
