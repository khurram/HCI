
import javax.swing.JInternalFrame;
import javax.swing.JTextField;

import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class LabelFrame extends JInternalFrame implements KeyListener{
    static int openFrameCount = 0;

    public LabelFrame(int x, int y) {
        super("Choose a Label", 
              false,
              true, //closable
              false, //maximizable
              false);//iconifiable

    
        setSize(300,80);
        JTextField text = new JTextField();
        text.addKeyListener(this);
        this.add(text);
        setLocation(x, y);
    }

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent key) {
		// TODO Auto-generated method stub
		if(key.getKeyCode() == KeyEvent.VK_ENTER) {
			setVisible(false);
			dispose();
		}
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}
}