package hci;


import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Handles image editing panel
 * @author Michal
 *
 */
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {
	/**
	 * some java stuff to get rid of warnings
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * image to be tagged
	 */
	BufferedImage image = null;
	
	private int x1,y1,x2,y2; //mouse locations
	private boolean dragging;
	
	private Point startpoint = null;
	private Point lastdragpoint = null;
	
	
	private Raster r; //raster to store current drawing
	private Raster rpre; //raster to store previous drawing for undo
	
	/**
	 * list of current polygon's vertices 
	 */
	ArrayList<Point> currentPolygon = null;
	HashSet<Point> currentPolygonSet = null;
	
	/**
	 * list of polygons
	 */
	ArrayList<ArrayList<Point>> polygonsList = null;
	
	/**
	 * default constructor, sets up the window properties
	 */
	public ImagePanel() {
		currentPolygon = new ArrayList<Point>();
		currentPolygonSet = new HashSet<Point>();
		polygonsList = new ArrayList<ArrayList<Point>>();
		dragging = false;
		this.setVisible(true);

		Dimension panelSize = new Dimension(800, 600);
		this.setSize(panelSize);
		this.setMinimumSize(panelSize);
		this.setPreferredSize(panelSize);
		this.setMaximumSize(panelSize);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * extended constructor - loads image to be labelled
	 * @param imageName - path to image
	 * @throws Exception if error loading the image
	 */
	public ImagePanel(String imageName) throws Exception{
		this();
		image = ImageIO.read(new File(imageName));
		if (image.getWidth() > 800 || image.getHeight() > 600) {
			int newWidth = image.getWidth() > 800 ? 800 : (image.getWidth() * 600)/image.getHeight();
			int newHeight = image.getHeight() > 600 ? 600 : (image.getHeight() * 800)/image.getWidth();
			System.out.println("SCALING TO " + newWidth + "x" + newHeight );
			Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
			image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(scaledImage, 0, 0, this);
		}
	}

	/**
	 * Displays the image
	 */
	public void ShowImage() {
		Graphics g = this.getGraphics();
		
		if (image != null) {
			g.drawImage(
					image, 0, 0, null);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		//display iamge
		ShowImage();
		
		//display all the completed polygons
		for(ArrayList<Point> polygon : polygonsList) {
			drawPolygon(polygon);
			//finishPolygon(polygon);
		}
		
		//display current polygon
		drawPolygon(currentPolygon);
	}
	
	/**
	 * displays a polygon without last stroke
	 * @param polygon to be displayed
	 */
	public void drawPolygon(ArrayList<Point> polygon) {
		Graphics2D g = (Graphics2D)this.getGraphics();
		g.setColor(Color.RED);
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
	
	/**
	 * displays last stroke of the polygon (arch between the last and first vertices)
	 * @param polygon to be finished
	 */
	public void finishPolygon(ArrayList<Point> polygon) {
		//if there are less than 3 vertices than nothing to be completed
		if (polygon.size() >= 3) {
			Point firstVertex = polygon.get(0);
			Point lastVertex = polygon.get(polygon.size() - 1);
		
			Graphics2D g = (Graphics2D)this.getGraphics();
			g.setStroke(new BasicStroke(4.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.setColor(Color.RED);
			g.drawLine(firstVertex.getX(), firstVertex.getY(), lastVertex.getX(), lastVertex.getY());
		}
	}
	
	/**
	 * moves current polygon to the list of polygons and makes pace for a new one
	 */
	public void addNewPolygon() {
		//finish the current polygon if any
		if (currentPolygon != null ) {
			finishPolygon(currentPolygon);
			polygonsList.add(currentPolygon);
		}
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.RED);
		g.fillOval(startpoint.getX()-7,startpoint.getY()-7,15,15);
		startpoint = null;
  	  	lastdragpoint = null;
		currentPolygon = new ArrayList<Point>();
		currentPolygonSet = new HashSet<Point>();
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
	    	  System.out.println("hiya");
	    	  currentPolygon.add(lastdragpoint);
	    	  dragpoint = true;
	      } else {
	    	  currentPolygon.add(new Point(x1,y1,8,true));
	    	  currentPolygonSet.add(new Point(x1,y1,8,true));
	      }
	      
	      r=image.getData();
	      //check if the cursos withing image area
	      if (x1 > image.getWidth() || y1 > image.getHeight()) {
	    	  //if not do nothing
	    	  return;
	      }
			
	      Graphics2D g = (Graphics2D)image.getGraphics();
			
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
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		drawVertex(e);
		mouseOverCheck(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(dragging) {
			dragging = false;
			if(startpoint != null && startpoint.near(new Point(e.getX(),e.getY(),1))) {
		    	  currentPolygon.add(startpoint);
		    	  addNewPolygon();
		    } else {
		    	Graphics2D g = (Graphics2D)image.getGraphics();
		    	System.out.println("setting dragpoint");
		    	lastdragpoint = new Point(e.getX(),e.getY(),8,true);
		    	currentPolygon.add(lastdragpoint);
		    	g.setColor(Color.RED);
		    	g.fillOval(e.getX()-7,e.getY()-7,15,15);
		    }
		}
		mouseOverCheck(e);
		
	}
	
	public void mouseDragged( MouseEvent e ) {
		  x2 = e.getX();
	      y2 = e.getY();
	      dragging = true;
	      Point cur = new Point(x2,y2,4);
	      currentPolygon.add(cur);
	      Graphics2D g = (Graphics2D) image.getGraphics();
	      r=image.getData();
	      g.setStroke(new BasicStroke(4.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	      g.setColor(Color.RED);
	      g.drawLine(x1,y1,x2,y2);

	      mouseOverCheck(e);

	      x1 = x2;
		  y1 = y2;
	   }

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseOverCheck(e);
	}
	
	public void mouseOverCheck(MouseEvent e) {
		Graphics2D g = (Graphics2D) image.getGraphics();
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
		this.getGraphics().drawImage(image, 0, 0, null);
	}
}
