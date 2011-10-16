
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
 
/**
 * HCI Project Phase 1
 * 
 * @author Sam Shelley, Khurram Aslam
 */
public class ImageLabeller extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private ImageDesktop desktop;
	private JPanel mainArea;
	private JPanel rightPane;
	private JPanel labelNames;
	private JPanel deleteButtons;
    private BufferedImage image = null;
    
    private HashMap<Integer,PolygonLabel> labelList;
    
    public ImageLabeller(String imageFileName) throws IOException {
        super("InternalFrameDemo");
        image = ImageIO.read(new File(imageFileName));
		
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 0;
   
        setBounds(inset,inset,image.getWidth()+150,
                image.getHeight()+50);
 
        
        mainArea = new JPanel();
        mainArea.setLayout(new BoxLayout(mainArea,BoxLayout.X_AXIS));
        
        //Set up the GUI.
        desktop = new ImageDesktop(image,this);
        desktop.setSize(image.getWidth(),image.getHeight());
        desktop.setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
        desktop.setMinimumSize(new Dimension(image.getWidth(),image.getHeight()));
        desktop.setMaximumSize(new Dimension(image.getWidth(),image.getHeight()));
        
        JLabel header = new JLabel();
        header.setText("<html><b>Labels:</b></html>");
        
        rightPane = new JPanel();
        rightPane.setSize(145,image.getHeight());
        rightPane.setPreferredSize(new Dimension(145,image.getHeight()));
        rightPane.setMinimumSize(new Dimension(145,image.getHeight()));
        rightPane.setMaximumSize(new Dimension(145,image.getHeight()));
        rightPane.setLayout(new BoxLayout(rightPane,BoxLayout.X_AXIS));
        //rightPane.add(header);
        
        labelNames = new JPanel();
        labelNames.setLayout(new BoxLayout(labelNames, BoxLayout.Y_AXIS));
        labelNames.setPreferredSize(new Dimension(120,image.getHeight()));
        labelNames.setMinimumSize(new Dimension(120,image.getHeight()));
        labelNames.setMaximumSize(new Dimension(120,image.getHeight()));
        deleteButtons = new JPanel();
        deleteButtons.setLayout(new BoxLayout(deleteButtons, BoxLayout.Y_AXIS));
        deleteButtons.setPreferredSize(new Dimension(25,image.getHeight()));
        deleteButtons.setMinimumSize(new Dimension(25,image.getHeight()));
        deleteButtons.setMaximumSize(new Dimension(25,image.getHeight()));
        rightPane.add(labelNames);
        rightPane.add(deleteButtons);
        
        mainArea.add(desktop);
        mainArea.add(Box.createRigidArea(new Dimension(5,image.getHeight())));
        mainArea.add(rightPane);
        mainArea.setSize(image.getWidth()+150,image.getHeight());
        //create first "window"

        setContentPane(mainArea);
        
        setJMenuBar(createMenuBar());
        
        labelList = new HashMap<Integer,PolygonLabel>();
    }
    
    protected void addLabel(String text, int id) {
    	labelList.put(id,new PolygonLabel(text,id));
    }
    protected void deleteLabel(int id) {
    	labelList.get(id).delete();
    }
    protected String getLabelText(int id) {
    	return labelList.get(id).getText();
    }
    private class PolygonLabel {
    	private JLabel newLabel;
    	private JPanel buttonBorder;
    	private JButton x;
    	private int id;
    	private String labelText;
    	public PolygonLabel(String text,int id) {
    		this.id = id;
    		labelText = text;
	    	newLabel = new JLabel();
	    	newLabel.setText(text);
	    	newLabel.setBorder(new EmptyBorder(2,2,2,2));
	    	
	    	buttonBorder = new JPanel();
	    	buttonBorder.setPreferredSize(new Dimension(20,20));
	    	buttonBorder.setMaximumSize(new Dimension(20,20));
	    	buttonBorder.setMinimumSize(new Dimension(20,20));
	    	x = new JButton();
	    	x.setText("x");
	    	x.setPreferredSize(new Dimension(20,13));
	    	x.setMaximumSize(new Dimension(20,13));
	    	x.setMinimumSize(new Dimension(20,13));
	    	x.addActionListener(new DeleteListener(this));
	    	labelNames.add(newLabel);
	    	buttonBorder.add(x);
	    	deleteButtons.add(buttonBorder);
    	}
    	public void delete() {
    		newLabel.setVisible(false);
			x.setVisible(false);
			buttonBorder.setVisible(false);
    	}
    	public int getPolygonId() {
    		return id;
    	}
    	public String getText() {
    		return labelText;
    	}
    }
    private class DeleteListener implements ActionListener{
    	
    	private PolygonLabel self;
		public DeleteListener(PolygonLabel self) {
			this.self = self;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			self.delete();
			desktop.deletePolygon(self.getPolygonId());
		}
    	
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
 
        //file menu
        JMenu fmenu = new JMenu("File");
        fmenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fmenu);
        
        //edit menu
        JMenu emenu = new JMenu("Edit");
        emenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(emenu);

        //open option
        JMenuItem menuItem = new JMenuItem("Open");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("open");
        menuItem.addActionListener(this);
        fmenu.add(menuItem);
        
        //undo option
        menuItem = new JMenuItem("Undo");
        menuItem.setMnemonic(KeyEvent.VK_Z);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("undo");
        menuItem.addActionListener(this);
        emenu.add(menuItem);
 
        //quit option
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        fmenu.add(menuItem);
 
        return menuBar;
    }
 
    //React to menu selections.
    public void actionPerformed(ActionEvent e) {
        if ("undo".equals(e.getActionCommand())) {
            desktop.undo();
        } else if ("open".equals(e.getActionCommand())) {
        	desktop.openFile();
        } else {
            quit();
        }
    }
    
    //Create a new internal frame.
    protected void createFrame(int x,int y, String defaultText) {
        int offsetx = desktop.getWidth()-300-x;
        int offsety = desktop.getHeight()-y-110;
        if(offsetx < 0) {
        	x = x + offsetx;
        } else if(offsetx > 500) {
        	x = x + 50;
        }
        if(offsety < 0) {
        	y = y + offsety;
        } else if(offsety > 490) {
        	y = y + 50;
        }
        LabelFrame frame = new LabelFrame(desktop,x,y,defaultText);
        frame.setVisible(true);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {}
    }
 
    //Quit the application.
    protected void quit() {
        System.exit(0);
    }
 
    private static void setUpGUI(String imageFileName) {
        JFrame.setDefaultLookAndFeelDecorated(true);
 
        //Create and set up the window.
        try {
	        ImageLabeller frame = new ImageLabeller(imageFileName);
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
                setUpGUI("images/test.jpg");
            }
        });
    }
}