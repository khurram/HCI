import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.DefaultDesktopManager;  

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
	
	private ArrayList<Point> currentPolygon = null;
	private HashMap<Integer,ArrayList<Point>> polygonsList = null;
	
	private JPanel drawings;
	
	private BufferedImage image;
	
	private Stack<UndoAction> undoStack;
	private Stack<RedoAction> redoStack;
	
	private int labelIncrementor = 0;
	
	final JFileChooser fc = new JFileChooser();
	
	public ImageDesktop(BufferedImage readImage, ImageLabeller parent) {
		super();
		image = readImage;
		
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
	}
	/**
	 * 
	 * Valid actions include:
	 * 
	 * addPointToCurrent
	 * completePolygon
	 * deleteCurrentPolygon
	 * deleteSavedPolygon
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
		
		public RedoAction(String action, ArrayList<Point> points) {
			this.action = action;
			this.points = points;
		}
		public RedoAction(String action,int polygonId, ArrayList<Point> points, String label) {
			this.action = action;
			this.polygonId = polygonId;
			this.label = label;
			this.points = points;
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
			f.repaint();
        }	
		public void endDraggingFrame(JComponent f)
        {
			f.repaint();
        }	
    }
	
	private boolean isOpenDialog() {
		return getAllFrames().length > 0;
	}
	
	public void deleteCurrentPolygon() {
		startpoint = null;
  	  	lastdragpoint = null;
  	  	undoStack.push(new UndoAction("deleteCurrentPolygon",currentPolygon));
		currentPolygon = new ArrayList<Point>();
		repaint();
	}
	
	public void deletePolygon(int id) {
		undoStack.push(new UndoAction("deleteSavedPolygon",polygonsList.get(id),parent.getLabelText(id),id));
		polygonsList.remove(id);
		//System.out.println(polygonsList.size());
		repaint();
	}
	
	public void addNewPolygon() {
		//finish the current polygon if any
		//REMEMBER TO ADD CHECK FOR EDGE OF SCREEN LATER
		parent.createFrame(startpoint.getX()-50,startpoint.getY()-50,"");	
	}
	
	public void finishNewPolygon(String label) {
		if (currentPolygon != null ) {
			polygonsList.put(labelIncrementor,currentPolygon);
			parent.addLabel(label,labelIncrementor);
			undoStack.push(new UndoAction("completePolygon",labelIncrementor));
			labelIncrementor++;
			//saveFile(polygonsList);
		}
		
		startpoint = null;
  	  	lastdragpoint = null;
		currentPolygon = new ArrayList<Point>();
	}
	
	public void saveFile(HashMap<Integer,ArrayList<Point>> polygonsList) {
		fc.setCurrentDirectory(new File("."));
		int returnVal = fc.showSaveDialog(parent);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
		        FileOutputStream os = new FileOutputStream(file);
		        XMLEncoder encoder = new XMLEncoder(os);
		        System.out.println(polygonsList);

		        encoder.writeObject(polygonsList);
		        encoder.close();
		        os.close();
		      } catch (IOException ex) {
		        System.err.println("Could not write polygons");
		      }
		}
		
		

	}

	public void openFile() {
		fc.setCurrentDirectory(new File("."));
		int returnVal = fc.showOpenDialog(parent);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();

			try {
				FileInputStream is = new FileInputStream(file);
				XMLDecoder decoder = new XMLDecoder(is);
				polygonsList = (HashMap<Integer,ArrayList<Point>>) (decoder.readObject());
		        decoder.close();
		        is.close();
		        
		        repaint();
		      } catch (IOException ex) {
		        System.err.println("Could not load in polygons");
		      }
		}

	}
	
	public void undo() {
		if(undoStack.empty()) {
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
				} else {
					undo();
				}
			}
		} else if(last.action().equals("deleteCurrentPolygon")){
			currentPolygon = last.getPoints();
			startpoint = currentPolygon.get(0);
			lastdragpoint = null;
			addNewPolygon();
			repaint();
		} else if(last.action().equals("deleteSavedPolygon")){
			ArrayList<Point> tempPoly = last.getPoints();
			if (tempPoly != null ) {
				polygonsList.put(last.getId(),tempPoly);
				parent.addLabel(last.getLabel(),last.getId());
			}
			repaint();
		} else {
			undo();
		}
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
	
	public void drawPolygon(ArrayList<Point> polygon) {
		Graphics2D g = (Graphics2D)this.getGraphics();
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(4.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for(int i = 0; i < polygon.size(); i++) {
			Point currentVertex = polygon.get(i);
			if (i != 0) {
				Point prevVertex = polygon.get(i - 1);
				g.drawLine(prevVertex.getX(), prevVertex.getY(), currentVertex.getX(), currentVertex.getY());
			}
			if(currentVertex.isPrimary()) {
				g.fillOval(currentVertex.getX() - 7, currentVertex.getY() - 7, 15, 15);
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
	protected void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, null);
        for(ArrayList<Point> polygon : polygonsList.values()) {
			drawPolygon(polygon);
		}
		
		//display current polygon
		drawPolygon(currentPolygon);
		
		//repaint the frames to make sure they overlap
		for(JInternalFrame f : this.getAllFrames()) {
			f.repaint();
		}
		
    }
	
	public void addLabel(String text) {
		finishNewPolygon(text);
	}
	
	public void mouseOverCheck(MouseEvent e) {
		Graphics2D g = (Graphics2D) this.getGraphics();
		for(int i=0;i<currentPolygon.size();i++) {
			if(currentPolygon.get(i).isPrimary()) {
				if(currentPolygon.get(i).near(new Point(e.getX(),e.getY(),1))) {
					g.setColor(Color.WHITE);
					g.fillOval(currentPolygon.get(i).getX()-7,currentPolygon.get(i).getY()-7,15,15);
				} else {
					g.setColor(Color.RED);
					g.fillOval(currentPolygon.get(i).getX()-7,currentPolygon.get(i).getY()-7,15,15);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}
	
	private void drawVertex(MouseEvent e) {
		  boolean end = false;
		  boolean dragpoint = false;
		  x1 = e.getX();
	      y1 = e.getY();
	      if(startpoint == null) {
	    	  startpoint = new Point(x1,y1,8, true);
	    	  currentPolygon.add(startpoint);
	      } else if(startpoint.near(new Point(x1,y1,1))) {
	    	  currentPolygon.add(startpoint);
	    	  x1 = startpoint.getX();
	    	  y1 = startpoint.getY();
	    	  end = true;
	      } else if(lastdragpoint != null && lastdragpoint.near(new Point(x1,y1,1))) {
	    	  //we don't add another point here because you are just starting
	    	  //another drag
	    	  dragpoint = true;
	      } else {
	    	  currentPolygon.add(new Point(x1,y1,8,true));
	      }
	      
			
	      Graphics2D g = (Graphics2D)this.getGraphics();
			
	      //if the left button than we will add a vertex to poly
	      if (e.getButton() == MouseEvent.BUTTON1) {
	    	  g.setColor(Color.RED);
	    	  if (currentPolygon.size() > 1) {
	    		  Point lastVertex = currentPolygon.get(currentPolygon.size() - 2);
	    		  g.setStroke(new BasicStroke(4.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	    		  g.drawLine(lastVertex.getX(), lastVertex.getY(), x1, y1);
	    	  }
	    	  if(!end && !dragpoint) {
	    		  g.fillOval(x1-7,y1-7,15,15);
	    	  }
	    	  if(end) {
	    		  addNewPolygon();
	    	  }

	    	  //System.out.println(x1 + " " + y1);
	      }
	      undoStack.push(new UndoAction("addPointToCurrent"));
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!isOpenDialog()) {
			drawVertex(e);
			mouseOverCheck(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!isOpenDialog()) {
			if(dragging) {
				dragging = false;
				if(startpoint != null && startpoint.near(new Point(e.getX(),e.getY(),1))) {
			    	  currentPolygon.add(startpoint);
			    	  addNewPolygon();
			    } else {
			    	Graphics2D g = (Graphics2D)this.getGraphics();
			    	lastdragpoint = new Point(e.getX(),e.getY(),8,true);
			    	currentPolygon.add(lastdragpoint);
			    	g.setColor(Color.RED);
			    	g.fillOval(e.getX()-7,e.getY()-7,15,15);
			    }
			}
			mouseOverCheck(e);
			undoStack.push(new UndoAction("addPointToCurrent"));
		}
	}

}
