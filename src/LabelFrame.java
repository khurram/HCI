
import javax.swing.JInternalFrame;
import javax.swing.JTextField;

import java.awt.event.*;
import java.awt.*;

public class LabelFrame extends JInternalFrame {
    static int openFrameCount = 0;

    public LabelFrame(int x, int y) {
        super("Choose a Label", 
              false,
              true, //closable
              false, //maximizable
              false);//iconifiable

    
        setSize(300,80);
        JTextField text = new JTextField();
        this.add(text);
        setLocation(x, y);
    }
}