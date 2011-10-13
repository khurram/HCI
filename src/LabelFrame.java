
import javax.swing.JInternalFrame;

import java.awt.event.*;
import java.awt.*;

public class LabelFrame extends JInternalFrame {
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    public LabelFrame() {
        super("Document #" + (++openFrameCount), 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

    
        setSize(100,300);

        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }
}