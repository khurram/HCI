import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Tutorial {
	private ImageDesktop parent;
	private JPanel inner;
	private int step = 1;
	private JInternalFrame frame;
	public Tutorial(ImageDesktop parent) throws IOException {
		this.parent = parent;
		ClickListener prevlistener = new ClickListener() {
			public void click() {
				prev();
			}
		};
		ClickListener nextlistener = new ClickListener() {
			public void click() {
				next();
			}
		};
		ClickListener quitlistener = new ClickListener() {
			public void click() {
				try {
					frame.setClosed(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
				frame.dispose();
			}
		};
		frame = new JInternalFrame("Tutorial", false, true, false, false);
		frame.putClientProperty("type", "tutorial");
		frame.setSize(700,500);
		frame.setLocation(50,10);
        frame.setVisible(true);
        
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
		JLabel text = new JLabel("<html><b>Welcome to the tutorial!</b><br><br>Close this window at any time to skip the tutorial.<br><br>" +
				"The ImageLabeler provides a way to label objects in a image by circling them and then tagging them as what they are. There are two " +
				"steps when labeling an image." +
				"<br><br>Click \"Next\" to begin.</html>");
		text.setFont(new Font(text.getFont().getFamily(),text.getFont().getStyle(),14));
		pane1.add(text,BorderLayout.PAGE_START);
		
		JPanel navigation = new JPanel();
		navigation.setLayout(new GridLayout(1,1));
		navigation.setBackground(Color.yellow);
        JLabelLink next = new JLabelLink("Next&#62;&#62;",nextlistener,SwingConstants.RIGHT);
        navigation.add(next);
        pane1.add(navigation,BorderLayout.PAGE_END);
        
        //pane2
        JPanel pane2 = new JPanel();
		pane2.setBackground(Color.yellow);
		pane2.setLayout(new BorderLayout());
		final BufferedImage image1 = ImageIO.read(new File("images/tut1.jpg"));
		JPanel image1p = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image1,0,0,null);
			}
		};
		image1p.setBackground(Color.yellow);
		
		text = new JLabel("<html><b>Step 1: Select the Image</b><br><br>In order to select an image, you need to draw a polygon around the object" +
				"that you want to label. You can start by pressing once where you want your polygon. The first point will appear in green.<br><br></html>");
		pane2.add(text,BorderLayout.PAGE_START);
		pane2.add(image1p,BorderLayout.CENTER);
		
		navigation = new JPanel();
		navigation.setLayout(new GridLayout(1,2));
		navigation.setBackground(Color.yellow);
		JLabelLink prev = new JLabelLink("&#60;&#60;Prev",prevlistener,SwingConstants.LEFT);
        navigation.add(prev);
        next = new JLabelLink("Next&#62;&#62;",nextlistener,SwingConstants.RIGHT);
        navigation.add(next);
        pane2.add(navigation,BorderLayout.PAGE_END);
        
        //pane3
        JPanel pane3 = new JPanel();
		pane3.setBackground(Color.yellow);
		pane3.setLayout(new BorderLayout());
		final BufferedImage image2 = ImageIO.read(new File("images/tut2.jpg"));
		JPanel image2p = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image2,0,0,null);
			}
		};
		image2p.setBackground(Color.yellow);
		image2p.setAlignmentX(SwingConstants.CENTER);
		
		text = new JLabel("<html>Click again to create another point on the image. This will automatically " +
				"generate a line connecting the two points.<br><br></html>");
		pane3.add(text,BorderLayout.PAGE_START);
		pane3.add(image2p,BorderLayout.CENTER);
		
		navigation = new JPanel();
		navigation.setLayout(new GridLayout(1,2));
		navigation.setBackground(Color.yellow);
		prev = new JLabelLink("&#60;&#60;Prev",prevlistener,SwingConstants.LEFT);
        navigation.add(prev);
        next = new JLabelLink("Next&#62;&#62;",nextlistener,SwingConstants.RIGHT);
        navigation.add(next);
        pane3.add(navigation,BorderLayout.PAGE_END);
        
        
        //pane4
        JPanel pane4 = new JPanel();
		pane4.setBackground(Color.yellow);
		pane4.setLayout(new BorderLayout());
		final BufferedImage image3 = ImageIO.read(new File("images/tut3.jpg"));
		JPanel image3p = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image3,0,0,null);
			}
		};
		image3p.setBackground(Color.yellow);
		
		text = new JLabel("<html>Continue creating points around the object you want to select. " +
				"You can also freeform draw by clicking and dragging. Try using this for the more circular parts of the object.<br><br></html>");
		pane4.add(text,BorderLayout.PAGE_START);
		pane4.add(image3p,BorderLayout.CENTER);
		
		navigation = new JPanel();
		navigation.setLayout(new GridLayout(1,2));
		navigation.setBackground(Color.yellow);
		prev = new JLabelLink("&#60;&#60;Prev",prevlistener,SwingConstants.LEFT);
        navigation.add(prev);
        next = new JLabelLink("Next&#62;&#62;",nextlistener,SwingConstants.RIGHT);
        navigation.add(next);
        pane4.add(navigation,BorderLayout.PAGE_END);
        
        //pane5
        JPanel pane5 = new JPanel();
		pane5.setBackground(Color.yellow);
		pane5.setLayout(new BorderLayout());
		final BufferedImage image4 = ImageIO.read(new File("images/tut4.jpg"));
		JPanel image4p = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image4,0,0,null);
			}
		};
		image4p.setBackground(Color.yellow);
		
		text = new JLabel("<html>Finish the polygon by dragging to or clicking on the startpoint (the green point). Doing so will make a " +
				"window pop up which allows you to input <br><br></html>");
		pane5.add(text,BorderLayout.PAGE_START);
		pane5.add(image4p,BorderLayout.CENTER);
		
		navigation = new JPanel();
		navigation.setLayout(new GridLayout(1,2));
		navigation.setBackground(Color.yellow);
		prev = new JLabelLink("&#60;&#60;Prev",prevlistener,SwingConstants.LEFT);
        navigation.add(prev);
        next = new JLabelLink("Next&#62;&#62;",nextlistener,SwingConstants.RIGHT);
        navigation.add(next);
        pane5.add(navigation,BorderLayout.PAGE_END);
        
        //pane6
        JPanel pane6 = new JPanel();
		pane6.setBackground(Color.yellow);
		pane6.setLayout(new BorderLayout());
		final BufferedImage image5 = ImageIO.read(new File("images/tut5.jpg"));
		JPanel image5p = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image5,0,0,null);
			}
		};
		image5p.setBackground(Color.yellow);
		
		text = new JLabel("<html><b>Step 2: Label the image.</b><br><br>Choose a label for your created polygon and press \"submit\" (or hit the" +
				" enter key) <br><br></html>");
		pane6.add(text,BorderLayout.PAGE_START);
		pane6.add(image5p,BorderLayout.CENTER);
		
		navigation = new JPanel();
		navigation.setLayout(new GridLayout(1,2));
		navigation.setBackground(Color.yellow);
		prev = new JLabelLink("&#60;&#60;Prev",prevlistener,SwingConstants.LEFT);
        navigation.add(prev);
        next = new JLabelLink("Next&#62;&#62;",nextlistener,SwingConstants.RIGHT);
        navigation.add(next);
        pane6.add(navigation,BorderLayout.PAGE_END);
        
        //pane7
        JPanel pane7 = new JPanel();
		pane7.setBackground(Color.yellow);
		pane7.setLayout(new BorderLayout());
		final BufferedImage image6 = ImageIO.read(new File("images/tut6.jpg"));
		JPanel image6p = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image6,0,0,null);
			}
		};
		image6p.setBackground(Color.yellow);
		
		text = new JLabel("<html>Click once on an already submitted label to edit it. Press \"enter\" to submit the change.<br><br>You can also press " +
				"the \"x\" button next to each label to delete it and the corresponding polygon.<br><br></html>");
		pane7.add(text,BorderLayout.PAGE_START);
		pane7.add(image6p,BorderLayout.CENTER);
		
		navigation = new JPanel();
		navigation.setLayout(new GridLayout(1,2));
		navigation.setBackground(Color.yellow);
		prev = new JLabelLink("&#60;&#60;Prev",prevlistener,SwingConstants.LEFT);
        navigation.add(prev);
        next = new JLabelLink("Next&#62;&#62;",nextlistener,SwingConstants.RIGHT);
        navigation.add(next);
        pane7.add(navigation,BorderLayout.PAGE_END);
        
        //pane8
        JPanel pane8 = new JPanel();
		pane8.setBackground(Color.yellow);
		pane8.setLayout(new BorderLayout());
		
		text = new JLabel("<html><b>Advanced Tips</b><br><br>Use Ctrl + Z to Undo actions and Ctrl + Y to Redo actions.<br><br>Mousing over " +
				"a label on the right will highlight the corresponding polygon on the image.<br><br>You can hit enter instead of pressing submit " +
				"when finishing a label.<br><br>Closing the label input window will delete your polygon (the shortcut is escape).</html>");
		pane8.add(text,BorderLayout.PAGE_START);
		
		navigation = new JPanel();
		navigation.setLayout(new GridLayout(1,2));
		navigation.setBackground(Color.yellow);
		prev = new JLabelLink("&#60;&#60;Prev",prevlistener,SwingConstants.LEFT);
        navigation.add(prev);
        next = new JLabelLink("Finish",quitlistener,SwingConstants.RIGHT);
        navigation.add(next);
        pane8.add(navigation,BorderLayout.PAGE_END);
        
        inner.add(pane1,"1");
        inner.add(pane2,"2");
        inner.add(pane3,"3");
        inner.add(pane4,"4");
        inner.add(pane5,"5");
        inner.add(pane6,"6");
        inner.add(pane7,"7");
        inner.add(pane8,"8");
        
        layout.show(inner, "1");
	}
	public void prev() {
		((CardLayout) inner.getLayout()).show(inner,""+--step);
	}
	public void next() {
		((CardLayout) inner.getLayout()).show(inner,++step+"");
	}
}
