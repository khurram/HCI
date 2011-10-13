
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
 
/*
 * InternalFrameDemo.java requires:
 *   MyInternalFrame.java
 */
public class ImageLabeller extends JFrame
                               implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageDesktop desktop;
	JPanel mainArea;
	JPanel rightPane;
    BufferedImage image = null;
    public ImageLabeller() throws Exception {
        super("InternalFrameDemo");
        image = ImageIO.read(new File("images/test.jpg"));

		
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 0;
   
        setBounds(inset,inset,image.getWidth()+150,
                image.getHeight()+20);
 
        
        mainArea = new JPanel();
        mainArea.setLayout(new BoxLayout(mainArea,BoxLayout.X_AXIS));
        
        
        //Set up the GUI.
        desktop = new ImageDesktop(image,this);
        desktop.setSize(image.getWidth(),image.getHeight());
        desktop.setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
        desktop.setMinimumSize(new Dimension(image.getWidth(),image.getHeight()));
        desktop.setMaximumSize(new Dimension(image.getWidth(),image.getHeight()));
        
        JLabel header = new JLabel();
        header.setText("<html><b>Labels:</b><br /></html>");
        
        rightPane = new JPanel();
        rightPane.setSize(145,image.getHeight());
        rightPane.setPreferredSize(new Dimension(145,image.getHeight()));
        rightPane.setMinimumSize(new Dimension(145,image.getHeight()));
        rightPane.setMaximumSize(new Dimension(145,image.getHeight()));
        rightPane.setLayout(new BoxLayout(rightPane,BoxLayout.Y_AXIS));
        rightPane.add(header);
        
        mainArea.add(desktop);
        mainArea.add(Box.createRigidArea(new Dimension(5,image.getHeight())));
        mainArea.add(rightPane);
        mainArea.setSize(image.getWidth()+150,image.getHeight());
        //create first "window"

        setContentPane(mainArea);
        
        //setJMenuBar(createMenuBar());
        //Make dragging a little faster but perhaps uglier.
        
       // desktop.add(image);
    }
 
    public void addLabel(String text) {
    	JLabel newLabel = new JLabel();
    	newLabel.setText(text);
    	rightPane.add(newLabel);
    }
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
 
        //Set up the lone menu.
        JMenu menu = new JMenu("Document");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);
 
        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Undo");
        menuItem.setMnemonic(KeyEvent.VK_Z);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("undo");
        menuItem.addActionListener(this);
        menu.add(menuItem);
 
        //Set up the second menu item.
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);
 
        return menuBar;
    }
 
    //React to menu selections.
    public void actionPerformed(ActionEvent e) {
        if ("new".equals(e.getActionCommand())) { //new
            createFrame(30,30);
        } else { //quit
            quit();
        }
    }
 
    //Create a new internal frame.
    protected void createFrame(int x,int y) {
        LabelFrame frame = new LabelFrame(x,y);
        frame.setVisible(true); //necessary as of 1.3
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }
 
    //Quit the application.
    protected void quit() {
        System.exit(0);
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
 
        //Create and set up the window.
        try {
        ImageLabeller frame = new ImageLabeller();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Display the window.
        frame.setVisible(true);
        } catch (Exception e) {
        	System.out.println("fail");
        }
       
       
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}