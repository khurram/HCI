import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Tutorial2{
	private JPanel inner;
	private int step = 0;
	private JInternalFrame frame;
	public Tutorial2(ImageDesktop parent) {
		frame = new JInternalFrame("Tips", false, true, false, false);
		frame.putClientProperty("type", "tutorial");
		frame.setSize(250,150);
		frame.setLocation(10,10);
        frame.setVisible(false);
        
        parent.add(frame);
		
		CardLayout layout = new CardLayout(0, 0);
		
		inner = new JPanel();
		inner.setLayout(layout);
		inner.setBorder(new EmptyBorder(5,5,5,5));
		inner.setBackground(Color.yellow);
		frame.add(inner);
		
		//pane1
		JPanel pane1 = new JPanel();
		pane1.setBackground(Color.yellow);
		pane1.setLayout(new BorderLayout());
		JLabel text = new JLabel("<html><b>Welcome to the ImageLabeller!</b><br><br>Click anywhere on the image to start drawing." +
				"<br><br>Click \"Next\" to begin.</html>");
		text.setFont(new Font(text.getFont().getFamily(),text.getFont().getStyle(),14));
		pane1.add(text,BorderLayout.PAGE_START);
		
		//pane2
		JPanel pane2 = new JPanel();
		pane2.setBackground(Color.yellow);
		pane2.setLayout(new BorderLayout());
		text = new JLabel("<html>Complete a polygon by clicking or dragging to the green startpoint.</html>");
		text.setFont(new Font(text.getFont().getFamily(),text.getFont().getStyle(),14));
		pane2.add(text,BorderLayout.PAGE_START);
		
		
		//pane3
		JPanel pane3 = new JPanel();
		pane3.setBackground(Color.yellow);
		pane3.setLayout(new BorderLayout());
		text = new JLabel("<html>You can edit existing labels by clicking on the label on the right pane and typing in the new label. " +
				"Press \"enter\" to submit the change.</html>");
		text.setFont(new Font(text.getFont().getFamily(),text.getFont().getStyle(),14));
		pane3.add(text,BorderLayout.PAGE_START);
		
		inner.add(pane1,"1");
		inner.add(pane2,"2");
		inner.add(pane3,"3");
		layout.show(inner,"0");
	}
	public void next() {
		if(step<=3) {
			frame.setVisible(true);
			((CardLayout) inner.getLayout()).show(inner,++step+"");
		} else {
			frame.setVisible(false);
		}
	}
	public int getStep() {
		return step;
	}
}
