
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import java.awt.event.*;


/**
 * HCI Project Phase 1
 * 
 * @author Sam Shelley, Khurram Aslam
 */

public class LabelFrame extends JInternalFrame implements KeyListener, InternalFrameListener{
	private static final long serialVersionUID = 1L;
	private JTextField text;
    public LabelFrame(int x, int y) {
        super("Choose a Label", false, true, false, false); 
        setSize(300,80);
        text = new JTextField();
        text.addKeyListener(this);
        addInternalFrameListener(this);
        add(text);
        setLocation(x, y);
        
    }

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		
	}

	@Override
	public void keyReleased(KeyEvent key) {
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

		
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent arg0) {
		
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent arg0) {
		//removes current polygon
		((ImageDesktop)getDesktopPane()).deletePolygon(-1);
		
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent arg0) {
		
		
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent arg0) {
		
		
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent arg0) {
		
		
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent arg0) {
		
		
	}
}