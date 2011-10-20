import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

public class JLabelLink extends JLabel implements MouseListener{
	String text;
	ClickListener listener;
	public JLabelLink(String text) {
		super("<html><u>"+text+"</u></html>");
		this.text = text;
		setFont(new Font(getFont().getFamily(),getFont().getStyle(),16));
		addMouseListener(this);
	}
	public JLabelLink(String text,ClickListener l, int align) {
		this(text);
		addClickListener(l);
		setHorizontalAlignment(align);
	}
	public void addClickListener(ClickListener l) {
		listener = l;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		listener.click();
	}
	@Override
	public void setText(String text) {
		super.setText("<html><u>"+text+"</u></html>");
		this.text = text;
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		setForeground(Color.blue);
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		setForeground(Color.black);
		setText(text);
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
interface ClickListener {
	public void click();
}