import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.DefaultDesktopManager;  
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.tools.javac.code.Attribute.Array;

/**
 * HCI Project Phase 1
 * 
 * @author Sam Shelley, Khurram Aslam
 */
public class ImageDesktop extends JDesktopPane implements MouseListener, MouseMotionListener{
	private static final long serialVersionUID = 1L;

	private ImageLabeller parent;
	
	private int x1,x2,y1,y2;
	private boolean dragging;
	
	private Point startpoint = null;
	private Point lastdragpoint = null;
	
	public ArrayList<Point> currentPolygon = null;
	public HashMap<Integer,ArrayList<Point>> polygonsList = null;
	
	private JPanel drawings;
	
	private BufferedImage image;
	
	public static Stack<UndoAction> undoStack;
	public static Stack<RedoAction> redoStack;
	
	public int labelIncrementor = 0;
	
	private boolean pressed;
	private boolean mouseoverS = false;
	private boolean mouseoverL = false;
	
	private Tutorial2 tutorial;
	
	private ArrayList<Point> mousedOver = null;
	private Polygon mousedPolygon = null;
	
	final static JFileChooser fc = new JFileChooser();
	
	public ImageDesktop(BufferedImage readImage, ImageLabeller parent) {
		super();
		image = readImage;
		tutorial = new Tutorial2(this);
		setDesktopManager(new PaintDesktopManager());  
		
		this.parent = parent;
		currentPolygon = new ArrayList<Point>();
		polygonsList = new HashMap<Integer,ArrayList<Point>>();
		dragging = false;
		
		drawings = new JPanel();
		drawings.setVisible(true);
		drawings.setBackground(Color.blue);
		add(drawings);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		undoStack = new Stack<UndoAction>();
		redoStack = new Stack<RedoAction>();
		
		pressed = false;
		
		//openLabel(ImageLabeller.imageFilename+ ".xml");
		if (labelsExist()) {
			System.out.println("labels exist");
		} else {
			runTutorial();
		}
		String[] ext = new String[5];
 		ext[0] = "jpeg";
 		ext[1] = "jpg";
 		ext[2] = "gif";
 		ext[3] = "png";
 		ext[4] = "tiff";
 		fc.setFileFilter(new FileNameExtensionFilter("Images",ext));
 		fc.setAcceptAllFileFilterUsed(false);
	}
	/**
	 * 
	 * Valid actions include:
	 * 
	 * addPointToCurrent
	 * completePolygon
	 * deleteCurrentPolygon
	 * deleteSavedPolygon
	 * editLabel
	 *
	 */
	private class UndoAction {
		private String action;
		private int polygonId;
		private ArrayList<Point> points;
		private String label;
		public UndoAction(String action) {
			this.action = action;
			polygonId = -1;
		}
		public UndoAction(String action,int polygonId) {
			this.action = action;
			this.polygonId = polygonId;
		}
		public UndoAction(String action,String label, int id) {
			this.action = action;
			this.label = label;
			this.polygonId = id;
		}
		public UndoAction(String action,ArrayList<Point> points) {
			this.action = action;
			this.points = points;
		}
		public UndoAction(String action,ArrayList<Point> points, String label, int id) {
			this.action = action;
			this.points = points;
			this.label = label;
			this.polygonId = id;
		}
		public String action() {
			return action;
		}
		public ArrayList<Point> getPoints() {
			return points;
		}
		public int getId() {
			return polygonId;
		}
		public String getLabel() {
			return label;
		}
	}
	
	/**
	 * 
	 * Valid actions include:
	 * 
	 * addPointToCurrent
	 * completePolygon
	 *
	 */
	private class RedoAction {
		private String action;
		private int polygonId;
		private ArrayList<Point> points;
		private String label;
		
		public RedoAction(String action) {
			this.action = action;
		}
		public RedoAction(String action, ArrayList<Point> points) {
			this.action = action;
			this.points = points;
		}
		public RedoAction(String action, int polygonId) {
			this.action = action;
			this.polygonId = polygonId;
		}
		public RedoAction(String action,String label, int id) {
			this.action = action;
			this.label = label;
			this.polygonId = id;
		}
		public String action() {
			return action;
		}
		public int getId() {
			return polygonId;
		}
		public ArrayList<Point> getPoints() {
			return points;
		}
		public String getLabel() {
			return label;
		}
	}
	
	private static class PaintDesktopManager extends DefaultDesktopManager {  
		private static final long serialVersionUID = 1L;
		public void beginDraggingFrame(JComponent f)
        {
			super.beginDraggingFrame(f);
			f.repaint();
        }	
		public void dragFrame(JComponent f, int newX, int newY)
        {
            if (!"tutorial".equals(f.getClientProperty("type")))
                super.dragFrame(f, newX, newY);
        }
		public void endDraggingFrame(JComponent f)
        {
			super.endDraggingFrame(f);
			f.repaint();
        }
		public void activateFrame(JInternalFrame f) {
			super.activateFrame(f);
			f.repaint();
		}
		public void deactivateFrame(JInternalFrame f) {
			super.deactivateFrame(f);
			f.repaint();
		}
    }
	protected void mouseOverPolygon(boolean over, int id) {
		if(over) {
			mousedOver = polygonsList.get(id);
		} else {
			mousedOver = null;
		}
		repaint();
	}
	private boolean isOpenDialog() {
		if(getAllFrames().length > 0) {
			for(JInternalFrame f : getAllFrames()) {
				 if (!"tutorial".equals(f.getClientProperty("type"))) {
					 return true;
				 }
			}
			return false;
		} else {
			return false;
		}
	}
	
	public void deleteCurrentPolygon() {
		startpoint = null;
  	  	lastdragpoint = null;
  	  	undoStack.push(new UndoAction("deleteCurrentPolygon",currentPolygon));
  	  	checkUndoRedo();
		currentPolygon = new ArrayList<Point>();
		repaint();
	}
	
	public void deletePolygon(int id) {
		undoStack.push(new UndoAction("deleteSavedPolygon",polygonsList.get(id),parent.getLabelText(id),id));
		checkUndoRedo();
		polygonsList.remove(id);
		parent.deleteLabel(id);
		repaint();

		saveLabel();
	}
	
	public void addNewPolygon() {
		//finish the current polygon if any
		//REMEMBER TO ADD CHECK FOR EDGE OF SCREEN LATER
		parent.createFrame(startpoint.getX()-50,startpoint.getY()-50,"");	
	}
	public void resetMouseover() {
		mouseoverS = false;
		mouseoverL = false;
	}
	
	public void finishNewPolygon(String label) {
		System.out.println(polygonsList.size());
		if (currentPolygon != null ) {
			polygonsList.put(labelIncrementor,currentPolygon);
			System.out.println("ince"+labelIncrementor);
			parent.addLabel(label,labelIncrementor);
			undoStack.push(new UndoAction("completePolygon",labelIncrementor));
			checkUndoRedo();
			
			labelIncrementor++;
			saveLabel();
			
			startpoint = null;
	  	  	lastdragpoint = null;
	  	  	dragging = false;
	  	  	mouseoverS = false;
	  	  	mouseoverL = false;
			currentPolygon = new ArrayList<Point>();

		}
		if(tutorial.getStep() == 2) {
			tutorial.next();
		}
		
	}

	public boolean labelsExist() {
		
		String files;
		File folder = new File(".");
		File[] listOfFiles = folder.listFiles(); 
		
		for (int i = 0; i < listOfFiles.length; i++) 
		  {
		 
		   if (listOfFiles[i].isFile()) 
		   {
		   files = listOfFiles[i].getName();
		       if (files.endsWith(".xml") || files.endsWith(".XML"))
		       {
		          return true;
		        }
		     }
		  }
		return false;
		
	}
	
	public void saveLabel() {
		String currentImageLabels = ImageLabeller.imageFilename + ".xml";
		
		try {
			FileOutputStream os = new FileOutputStream(currentImageLabels);
			XMLEncoder encoder = new XMLEncoder(os);
		    
		    HashMap<Integer, String> stringSet = new HashMap<Integer, String>();
		    for (Integer i : parent.labelList.keySet()) {
		    	stringSet.put(i,(parent.labelList.get(i).getText()));
		    	System.out.println(parent.labelList.get(i).getText());
		    }
		    
	    	encoder.writeObject(stringSet);
	    	encoder.writeObject(polygonsList);
		    
		    encoder.close();
		    os.close();
		    } catch (IOException ex) {
		    System.err.println("Could not write polygons");
	    }
	}
	
	public void resetState() {
		Set<Integer> keys = new HashSet<Integer>();
		keys.addAll(parent.labelList.keySet());
		for(Integer i : keys) {
			parent.deleteLabel(i);
		}
		labelIncrementor = 0;
		startpoint = null;
		lastdragpoint = null;
		dragging = false;
		currentPolygon = new ArrayList<Point>();
		polygonsList = new HashMap<Integer,ArrayList<Point>>();
		undoStack = new Stack<UndoAction>();
		redoStack = new Stack<RedoAction>();
		checkUndoRedo();
		repaint();
	}
	
 	public void openImage() {
 		
		fc.setCurrentDirectory(new File("images"));
		int returnVal = fc.showOpenDialog(parent);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        ImageLabeller.imageFilename = file.getName();
	        labelIncrementor = 0;
	        try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				System.out.println("invalid image");
			}
			resetState();
	        openLabel(file.getName() + ".xml");
		}
	}
 	
	public void openLabel(String imageLabelFile) {
		try {
			FileInputStream is = new FileInputStream(imageLabelFile);
			XMLDecoder decoder = new XMLDecoder(is);
				
			HashMap readMap = (HashMap) decoder.readObject();
			polygonsList = (HashMap<Integer, ArrayList<Point>>) decoder.readObject();
			int i = 0;
			for (Object key : readMap.keySet()) {
				String value = (String) readMap.get(key);
				int keyid = Integer.parseInt(key.toString());
				System.out.println("Loaded Label:"+value);
				
				parent.addLabel(value, i);
				if(i!=keyid) {
					polygonsList.put(i,polygonsList.get(keyid));
					polygonsList.remove(keyid);
				}
				i++;
			}
			labelIncrementor = i;
			
		    
			decoder.close();
		    is.close();
		        
		} catch (IOException ex) {
			System.err.println("No existing polygons to load");
		}

	}
	public void checkUndoRedo() {
		if(undoStack.empty()) {
			parent.undo.setEnabled(false);
		} else {
			parent.undo.setEnabled(true);
		}
		if(redoStack.empty()) {
			parent.redo.setEnabled(false);
		} else {
			parent.redo.setEnabled(true);
		}
	}
	public void undo() {
		if(pressed || undoStack.empty()) {
			return;
		}
		UndoAction last = undoStack.pop();
		if(last.action().equals("addPointToCurrent") && currentPolygon != null) {
			if(currentPolygon.size() > 1) {
				ArrayList<Point> removedPoints = new ArrayList<Point>();
				removedPoints.add(currentPolygon.get(currentPolygon.size()-1));
				currentPolygon.remove(currentPolygon.size()-1);
				while(!currentPolygon.get(currentPolygon.size()-1).isPrimary()) {
					removedPoints.add(currentPolygon.get(currentPolygon.size()-1));
					currentPolygon.remove(currentPolygon.size()-1);
				}
				lastdragpoint = currentPolygon.get(currentPolygon.size()-1);
				for(JInternalFrame j: getAllFrames()) {
					j.dispose();
				}
				redoStack.push(new RedoAction("addPointToCurrent",removedPoints));
				repaint();
			} else if(currentPolygon.size() == 1) {
				ArrayList<Point> removedPoints = new ArrayList<Point>();
				removedPoints.add(currentPolygon.get(0));
				redoStack.push(new RedoAction("addPointToCurrent",removedPoints));
				currentPolygon.remove(0);
				startpoint = null;
				lastdragpoint = null;
				repaint();
			} else {
				undo();
			}
		} else if(last.action().equals("completePolygon")){
			if(polygonsList.size()>0) {
				currentPolygon = polygonsList.get(last.getId());
				if(currentPolygon != null) {
					polygonsList.remove(last.getId());
					String text = parent.getLabelText(last.getId());
					parent.deleteLabel(last.getId());
					startpoint = currentPolygon.get(0);
					lastdragpoint = null;
					parent.createFrame(startpoint.getX()-50,startpoint.getY()-50,text);
					redoStack.push(new RedoAction("completePolygon",text,last.getId()));
				} else {
					undo();
					System.out.println("HUGE PROBLEM");
				}
			}
		} else if(last.action().equals("deleteCurrentPolygon")){
			currentPolygon = last.getPoints();
			startpoint = currentPolygon.get(0);
			lastdragpoint = null;
			addNewPolygon();
			redoStack.push(new RedoAction("deleteCurrentPolygon"));
			repaint();
		} else if(last.action().equals("deleteSavedPolygon")){
			ArrayList<Point> tempPoly = last.getPoints();
			if (tempPoly != null ) {
				polygonsList.put(last.getId(),tempPoly);
				parent.addLabel(last.getLabel(),last.getId());
			}
			redoStack.push(new RedoAction("deleteSavedPolygon",last.getId()));
			repaint();
		} else if(last.action().equals("editLabel")){
			redoStack.push(new RedoAction("editLabel",parent.getLabelText(last.getId()),last.getId()));
			parent.updateLabel(last.getId(),last.getLabel(),false);
		} else {
			undo();
			System.out.println("HUGE PROBLEM");
		}
		checkUndoRedo();
	}
	protected void pushEditUndo(int id, String label) {
		undoStack.push(new UndoAction("editLabel",label, id));
		checkUndoRedo();
	}
	protected void clearRedo() {
		redoStack = new Stack<RedoAction>();
	}
	protected void redo() {
		if(pressed || redoStack.empty()) {
			return;
		}
		RedoAction next = redoStack.pop();
		if(next.action().equals("addPointToCurrent")) {
			if(currentPolygon == null || currentPolygon.size()<1) {
				currentPolygon = next.getPoints();
				startpoint = currentPolygon.get(0);
				lastdragpoint = currentPolygon.get(currentPolygon.size()-1);
				undoStack.push(new UndoAction("addPointToCurrent"));
				repaint();
			} else {
				for(int i = next.getPoints().size()-1;i>=0;i--) {
					currentPolygon.add(next.getPoints().get(i));
				}
				undoStack.push(new UndoAction("addPointToCurrent"));
				lastdragpoint = currentPolygon.get(currentPolygon.size()-1);
				if(startpoint == lastdragpoint) {
					//need to peek into next redo statement to get the previously assigned text
					if(!redoStack.empty()) {
						parent.createFrame(startpoint.getX()-50,startpoint.getY()-50,redoStack.peek().getLabel());
					} else {
						parent.createFrame(startpoint.getX()-50,startpoint.getY()-50,"");
					}
					
				}
				repaint();
			}
		} else if(next.action().equals("completePolygon")) {
			for(JInternalFrame j: getAllFrames()) {
				j.dispose();
			}
			if (currentPolygon != null ) {
				polygonsList.put(next.getId(),currentPolygon);
				parent.addLabel(next.getLabel(),next.getId());
				undoStack.push(new UndoAction("completePolygon",next.getId()));
			}
			
			startpoint = null;
	  	  	lastdragpoint = null;
			currentPolygon = new ArrayList<Point>();
		} else if(next.action().equals("deleteCurrentPolygon")) {
			for(JInternalFrame j: getAllFrames()) {
				j.dispose();
			}
			deleteCurrentPolygon();
			repaint();
		} else if(next.action().equals("deleteSavedPolygon")) {
			deletePolygon(next.getId());
			repaint();
		} else if(next.action().equals("editLabel")) {
			System.out.println(next.getId());
			undoStack.push(new UndoAction("editLabel",parent.getLabelText(next.getId()),next.getId()));
			parent.updateLabel(next.getId(),next.getLabel(),false);
		} else {
			redo();
		}
		checkUndoRedo();
	}
	public Point getStartPoint(int id) {
		return polygonsList.get(id).get(0);
	}
	@Override
	public void mouseDragged( MouseEvent e ) {
		if(!isOpenDialog()) {
			x2 = e.getX();
			y2 = e.getY();
			dragging = true;
			Point cur = new Point(x2,y2,4);
			currentPolygon.add(cur);
			Graphics2D g = (Graphics2D) this.getGraphics();
			g.setStroke(new BasicStroke(4.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.setColor(Color.RED);
			g.drawLine(x1,y1,x2,y2);

			mouseOverCheck(e);

			x1 = x2;
			y1 = y2;
		}
	}
	
	public void drawPolygon(ArrayList<Point> polygon, boolean current,BufferedImage temp) {
		Graphics2D g = (Graphics2D)temp.getGraphics();
		boolean first = false;
		g.setStroke(new BasicStroke(4.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for(int i = 0; i < polygon.size(); i++) {
			Point currentVertex = polygon.get(i);
			if (i != 0) {
				Point prevVertex = polygon.get(i - 1);
				g.setColor(Color.RED);
				g.drawLine(prevVertex.getX(), prevVertex.getY(), currentVertex.getX(), currentVertex.getY());
			}
			if(currentVertex.isPrimary()) {
				if (i != 0) {
					g.setColor(Color.RED);
					g.fillOval(currentVertex.getX() - 7, currentVertex.getY() - 7, 15, 15);
				}
			}
		}
		if(startpoint != null) {
			if(current && mouseoverS) {
				g.setColor(Color.WHITE);
			} else if(current) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.RED);
			}
			g.fillOval(startpoint.getX() - 7, startpoint.getY() - 7, 15, 15);
			if(lastdragpoint != null && lastdragpoint != startpoint && current) {
				if(mouseoverL) {
					g.setColor(Color.WHITE);
				} else  {
					g.setColor(Color.RED);
				}
				g.fillOval(lastdragpoint.getX() - 7, lastdragpoint.getY() - 7, 15, 15);
			}
			
			
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(!isOpenDialog()) {
			mouseOverCheck(e);
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
    {
		BufferedImage temp = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
		temp.setData(image.getData());
	    mousedPolygon = new Polygon();
	    if(mousedOver != null) {
			for(int i = 0;i<mousedOver.size();i++) {
				Point p = mousedOver.get(i);
				mousedPolygon.addPoint(p.getX()+1,p.getY()+1);
			}
			Graphics2D g2 = (Graphics2D)temp.getGraphics();
			g2.setColor(Color.red);
			Composite defaultC = g2.getComposite();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.2f));
			g2.fillPolygon(mousedPolygon);
			g2.setComposite(defaultC);
	    }
	    for(ArrayList<Point> polygon : polygonsList.values()) {
			drawPolygon(polygon,false,temp);
		} 
			//display current polygon
		drawPolygon(currentPolygon,true,temp);
		
		
		g.drawImage(temp, 0, 0, null);
    }
	
	public void addLabel(String text) {
		finishNewPolygon(text);
	}
	
	public void mouseOverCheck(MouseEvent e) {
			boolean start1 = mouseoverS;
			boolean start2 = mouseoverL;
			if(startpoint != null && startpoint.near(new Point(e.getX(),e.getY(),1))) {
				mouseoverS = true;
			} else if(startpoint != null) {
				mouseoverS = false;
				if(lastdragpoint != null && lastdragpoint.near(new Point(e.getX(),e.getY(),1))) {
					mouseoverL = true;
				} else {
					if(lastdragpoint != null && lastdragpoint != startpoint) {
						mouseoverL = false;
					}
				}
			}
			if(mouseoverS != start1 || mouseoverL != start2) {
				repaint();
			}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("num  labels"+parent.labelList.size());
		pressed = true;
		if(!isOpenDialog() && e.getButton() == MouseEvent.BUTTON1) {
			boolean end = false;
			boolean dragpoint = false;
			x1 = e.getX();
			y1 = e.getY();
			if(startpoint == null) {
				startpoint = new Point(x1,y1,8, true);
				lastdragpoint = startpoint;
		    	currentPolygon.add(startpoint);
		    } else if(startpoint.near(new Point(x1,y1,1)) && startpoint != lastdragpoint) {
		    	currentPolygon.add(startpoint);
		    	x1 = startpoint.getX();
		    	y1 = startpoint.getY();
		    	end = true;
		    } else if(lastdragpoint != null && lastdragpoint.near(new Point(x1,y1,1))) {
		    	  //we don't add another point here because you are just starting
		    	  //another drag
		    	dragpoint = true;
		    	return;
		    } else {
		    	lastdragpoint = new Point(x1,y1,8,true);
		    	currentPolygon.add(lastdragpoint);
		    }
		      
				

			if(end) {
				addNewPolygon();
			}

		    	  //System.out.println(x1 + " " + y1);
			undoStack.push(new UndoAction("addPointToCurrent"));
			checkUndoRedo();
			mouseOverCheck(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(pressed && !isOpenDialog()) {
			clearRedo();
			if(dragging) {
				dragging = false;
				if(startpoint != null && startpoint.near(new Point(e.getX(),e.getY(),1))) {
			    	  currentPolygon.add(startpoint);
			    	  addNewPolygon();
			    } else {
			    	if(tutorial.getStep() == 1 || tutorial.getStep() >= 3) {
						tutorial.next();
					}
			    	lastdragpoint = new Point(e.getX(),e.getY(),8,true);
			    	currentPolygon.add(lastdragpoint);
			    }
			}
			mouseOverCheck(e);
			undoStack.push(new UndoAction("addPointToCurrent"));
			checkUndoRedo();
		}
		pressed = false;
	}
	
	private void runTutorial() {
		tutorial.next();
		
	}

}
