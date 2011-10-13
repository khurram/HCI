
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class LabelFrame extends JInternalFrame implements KeyListener, InternalFrameListener{
    private JTextField text;
    public LabelFrame(int x, int y) {
        super("Choose a Label", 
              false,
              true, //closable
              false, //maximizable
              false);//iconifiable

        
        setSize(300,80);
        text = new JTextField();
        text.addKeyListener(this);
        addInternalFrameListener(this);
        add(text);
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
			((ImageDesktop)getDesktopPane()).addLabel(text.getText());
			setVisible(false);
			dispose();
		}
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent arg0) {
		((ImageDesktop)getDesktopPane()).deletePolygon(-1);
		
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}