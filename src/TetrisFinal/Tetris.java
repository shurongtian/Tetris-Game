package TetrisFinal;

/*
 * CS6366 - Summer 2020
 * Assignment 3 - Final Tetris
 * Shurong Tian - sxt151030
 * 07/12/2020
 * 
 * An user interface with sliders is added to the program. The user can 
 * adjust different game attributes to have different game experience.
 * 
 * Attributes include: M(scoring factor), N(number of rows required for 
 * each level), S(speed factor), game height, game width, square size.
 * 
 * These attributes have different ranges. the slider bars have different
 * ranges and displays according to the attributes.
 * 
 *  Point-inside-Polygon algorithm is used to change shape when the cursor
 *  is inside the falling shape.
 *  
 *  Adjust the attributes and press START to play.
 *  
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Hashtable;
import javax.swing.Timer;


class Point2D {
	   int x, y;
	   Point2D(int x, int y) {this.x = x; this.y = y;}
	}

class Tools2D {
	static float area2(Point2D a, Point2D b, Point2D c) {
		return (a.x - c.x) * (b.y - c.y) -
				(a.y - c.y) * (b.x - c.x);
}
	static float distance2(Point2D p, Point2D q) {
		float dx = p.x - q.x, dy = p.y - q.y;
		return dx * dx + dy * dy;
	}
	static boolean insideTriangle(Point2D a, Point2D b, Point2D c,
			Point2D p){ // ABC is assumed to be counter-clockwise
		return area2(a, b, p) >= 0 &&
				area2(b, c, p) >= 0 &&
				area2(c, a, p) >= 0;
	}
}

class Square{

	int Type;
	int X, Y;
	Color color;
	int rotation;
	
	Square(int x, int y, Color c){
		X = x;
		Y = y;
		//Type = type;
		color = c;
	}
}


public class Tetris extends Frame {
	static JButton start;
	static JLabel startLabel;
	static JSlider MSlider;
	static JLabel MLabel;
	static JSlider NSlider;
	static JLabel NLabel;
	static JSlider SSlider;
	static JLabel SLabel;
	static JSlider Width;
	static JLabel WLabel;
	static JSlider Height;
	static JLabel HLabel;
	static JSlider Size;
	static JLabel sizeLabel;
	JPanel panel;
	
	boolean startPressed = false;
	
	//initial values
		int score = 0;
		int level = 1;
		int M = 10;
		int N = 20;
		int S = 1;
		int h = 20;
		int w = 10;
		int lines = 0;
		int speed = 120;
		int size = 1;
		
	CvTetris tetris;
	
	void gamePanel() {
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(250, 600));
		panel.setMaximumSize(new Dimension(250, 600));
		start = new JButton("START");
		
		start.setPreferredSize(new Dimension(100, 50));
		
		startLabel = new JLabel("Make changes then press START");
		//panel.setLayout(new BorderLayout());
	    
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startPressed = true;
				tetris.repaint();
			}
		});
		
		
		MLabel = new JLabel("Scoring factor");
		MSlider = new JSlider(JSlider.HORIZONTAL, 1, 15, 10);
		MSlider.setMinorTickSpacing(1);
		MSlider.setMajorTickSpacing(5);
		MSlider.setPaintTicks(true);
		MSlider.setPaintLabels(true);
		MSlider.setLabelTable(MSlider.createStandardLabels(2));
		
		MSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (startPressed) {
					MSlider.setValue(M);
				} else {
					MLabel.setText(String.valueOf("Scoring factor: " + MSlider.getValue()));
					M = MSlider.getValue();
					tetris.repaint();
				}
			}
		});
		
		
		
		NLabel = new JLabel("Level difficulty (cancelled rows)");
		NSlider = new JSlider(JSlider.HORIZONTAL, 20, 40, 20);
		NSlider.setMinorTickSpacing(5);
		NSlider.setMajorTickSpacing(10);
		NSlider.setPaintTicks(true);
		NSlider.setPaintLabels(true);
		NSlider.setLabelTable(NSlider.createStandardLabels(5));
		
		NSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (startPressed) {
					NSlider.setValue(N);
				} else {
					NLabel.setText(String.valueOf("Level difficulty (cancelled rows): " + NSlider.getValue()));
					N = NSlider.getValue();
					tetris.repaint();
				}
			}
		});
		
		
		SLabel = new JLabel("Dropping speed (%)");
		SSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 10);
		SSlider.setMinorTickSpacing(10);
		SSlider.setMajorTickSpacing(100);
		SSlider.setPaintTicks(true);
		SSlider.setPaintLabels(true);
		SSlider.setLabelTable(SSlider.createStandardLabels(10));
	    
		SSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (startPressed) {
					SSlider.setValue(S);
				} else {
					SLabel.setText(String.valueOf("Dropping speed: " + SSlider.getValue() + "%"));
					S = SSlider.getValue();
					tetris.repaint();
				}
			}
		});
		
		
		HLabel = new JLabel("Game height");
		Height = new JSlider(JSlider.HORIZONTAL, 20, 30, 20);
		Height.setMinorTickSpacing(2);
		Height.setMajorTickSpacing(5);
		Height.setPaintTicks(true);
		Height.setPaintLabels(true);
		Height.setLabelTable(Height.createStandardLabels(2));
		
		Height.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (startPressed) {
					Height.setValue(h);
				} else {
					HLabel.setText(String.valueOf("Game height: " + Height.getValue()));
					h = Height.getValue();
					tetris.repaint();
				}
			}
		});
		
		
		WLabel = new JLabel("Game width");
		Width = new JSlider(JSlider.HORIZONTAL, 10, 15, 10);
		Width.setMinorTickSpacing(1);
		Width.setMajorTickSpacing(5);
		Width.setPaintTicks(true);
		Width.setPaintLabels(true);
		Width.setLabelTable(Width.createStandardLabels(1));
		
		Width.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (startPressed) {
					Width.setValue(w);
				} else {
					WLabel.setText(String.valueOf("Game width: " + Width.getValue()));
					w = Width.getValue();
					tetris.repaint();
				}
			}
		});
		
		
		sizeLabel = new JLabel("Square size");
		Size = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
		Size.setMinorTickSpacing(1);
		Size.setMajorTickSpacing(5);
		Size.setPaintTicks(true);
		Size.setPaintLabels(true);
		Size.setLabelTable(Size.createStandardLabels(1));
		
		Size.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (startPressed) {
					Size.setValue(size);
				} else {
					sizeLabel.setText(String.valueOf("Square size: " + Size.getValue()));
					size = Size.getValue();
					tetris.repaint();
				}
			}
		});
		
		
		panel.add(MLabel);
	    panel.add(MSlider);
	    panel.add(NLabel);
	    panel.add(NSlider);
	    panel.add(SLabel);
	    panel.add(SSlider);
	    panel.add(HLabel);
	    panel.add(Height);
	    panel.add(WLabel);
	    panel.add(Width);
	    panel.add(sizeLabel);
	    panel.add(Size);
	    panel.add(startLabel);
	    panel.add(start);  
    }
	
	public static void main(String[] args) {new Tetris();}

	
	
	
	Tetris() {
	      super("Tetris 3");
	      addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent e) {System.exit(0);}
	      });

	      
	      tetris = new CvTetris();
	      gamePanel();
	      add("Center", tetris);
	      add("West", panel);
	      setSize(800, 600);
	      setVisible(true);
	   }
 


class CvTetris extends Canvas {
	int minMainBoxX, minMainBoxY, maxMainBoxX, maxMainBoxY, 
	minUpperBoxX, minUpperBoxY, maxUpperBoxX, maxUpperBoxY, 
	minQuitBoxX, minQuitBoxY, maxQuitBoxX, maxQuitBoxY,
	maxX, maxY, minMaxXY, xCenter, yCenter;
	boolean showPause;
	int squareUnit;
	
	//random generator
	Random r = new Random();
	int currentPieceNum = r.nextInt(7);
	int nextPieceNum = r.nextInt(7);
	
	//outter points for current piece
	int minXCoord, maxXCoord, minYCoord, maxYCoord;
	//coords for current piece
	int[][] coords = new int[4][2];
	int xCoord = 0;
	int yCoord = 0;
	
	//to store dropped pieces
	Set<int[]> dropped = new HashSet<>();
	//to store coords of dropped pieces
	Set<int[]> saveCoords = new HashSet<>();

	int count= 0;
	int rotation = 0;
		
	Point2D[] pol = new Point2D[4];
	
	Set<Square> currPiece;
	Set<Square> nextPiece;
	Color Color;
	Set<Square> droppedSquares = new HashSet<>();
		
	
	Map<Integer, Integer> checkMark = new HashMap<>();
	   int[][] marks = new int[h][w];
	   
	   
	   
	Timer timer = new Timer((speed - S) * 5, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean collided = collisionCheck(currPiece, droppedSquares);
			if(inMainBox() && !collided && !showPause) {
			moveDown();
			count += 1;
			repaint();
			}
			
			else if(collided && !showPause) {
				
				
				dropped.add(new int[] {currentPieceNum, count, xCoord, yCoord, rotation});
				
				for(Square s: currPiece) {
					if(s.Y <= minMainBoxY) {
						System.exit(0);
					}
					   droppedSquares.add(new Square(s.X, s.Y, s.color));    
				}
				
				
				cancelRow();
				
				currentPieceNum = nextPieceNum;
				nextPieceNum = r.nextInt(7);
				count = 0;
				xCoord = 0;
				yCoord = 0;
				minXCoord = 0;
				maxXCoord = 0;
				minYCoord = 0;
				maxYCoord = 0;
				rotation = 0;
				repaint();
			}
		}
	});
	
	void initgr() {
		   
	      Dimension d = getSize();
	      maxX = d.width - 1; maxY = d.height - 1;
	      minMaxXY = Math.min(maxX, maxY);
	      xCenter = maxX / 2; yCenter = maxY / 2;
	      
	      /*if(!startPressed) {
	    	  M = MSlider.getValue();
	    	  N = NSlider.getValue();
	    	  S = SSlider.getValue();
	    	  h = Height.getValue();
	    	  w = Width.getValue();
	    	  size = Size.getValue();
	      }*/
	   }
	
	
	   CvTetris(){
		 //detects if the mouse is in the main box area
		   addMouseMotionListener(new MouseAdapter() {
			   @Override
			   public void mouseMoved(MouseEvent e) {
				   int x = e.getX();
				   int y = e.getY();
				   
				   Point2D p = new Point2D(x, y);
				   
				   //if the mouse is in the area
				   if((x >= minMainBoxX) &&(x <= minMainBoxX + maxMainBoxX) && (y >= minMainBoxY) &&(y <= minMainBoxY + maxMainBoxY) ) {
					   showPause = true;
					   
					   for(Square s: currPiece) {
						   pol[0] = new Point2D(s.X, s.Y);
						   pol[1] = new Point2D(s.X, s.Y);
						   pol[2] = new Point2D(s.X, s.Y);
						   pol[3] = new Point2D(s.X, s.Y);
					   }
					   if(insidePolygon(p, pol)) {
						   
						   currentPieceNum = r.nextInt(7);
					   }
					   repaint();
				   }
				   else {
					   showPause = false;
					   repaint();
				   } 
			   }
		   });   
		   
		   //detects if the the mouse clicks the quit box
		   addMouseListener(new MouseAdapter() {
			   @Override
	            public void mouseClicked(MouseEvent e) {
	                int x = e.getX();
	                int y = e.getY();
	                
	                boolean collided = collisionCheckSideway(currPiece, droppedSquares);
	                if((x >= minQuitBoxX) &&(x <= minQuitBoxX + maxQuitBoxX) && (y >= minQuitBoxY) &&(y <= minQuitBoxY + maxQuitBoxY) ) {
	                	System.exit(0);
	                }
	                //mouse click move the piece
	                else if(e.getButton() == MouseEvent.BUTTON1 && minXCoord > minMainBoxX && !showPause && !collided) {
	                	moveLeft();
	                }
	                else if(e.getButton() == MouseEvent.BUTTON3 && maxXCoord < minMainBoxX + maxMainBoxX && !showPause && !collided) {
	                	moveRight();
	                }
			   }
		   });
		   
		   addMouseWheelListener(new MouseAdapter() {
				public void mouseWheelMoved(MouseWheelEvent event) {
					//scroll upward
					if (event.getWheelRotation() < 0 && minXCoord > minMainBoxX && maxXCoord < minMainBoxX + maxMainBoxX) {
						rotation = (rotation + 1) % 4;
						repaint();
					}
					//scroll downward
					else if (event.getWheelRotation() > 0 && minXCoord > minMainBoxX && maxXCoord < minMainBoxX + maxMainBoxX) {
						rotation = (rotation - 1) % 4;
						repaint();
					}
				}
	   });
	   }
	   
	   public void paint(Graphics g) {
		   
		  if(startPressed) {
			   timer.start();
		  }
		  
	      initgr();
	      squareUnit = minMaxXY / 25 + size;
	      
	      //coords for the main box 
	      minMainBoxX = maxX * 2 / 10;
	      minMainBoxY = maxY /10;
	      maxMainBoxX = squareUnit * w;
	      maxMainBoxY = squareUnit * h;
	      //coords for the upper right box 
	      minUpperBoxX = minMainBoxX + maxMainBoxX + squareUnit * 2;
	      minUpperBoxY = minMainBoxY;
	      maxUpperBoxX = squareUnit * 6;
	      maxUpperBoxY = squareUnit * 4;
	      //coords for the quit box 
	      minQuitBoxX = minUpperBoxX;
	      minQuitBoxY = maxMainBoxY ;
	      maxQuitBoxX = squareUnit * 4;
	      maxQuitBoxY = squareUnit * 2;
	      
	      //draw the main box
	      g.drawRect(minMainBoxX, minMainBoxY, maxMainBoxX, maxMainBoxY);
	      
	      //draw upper right box
	      g.drawRect(minUpperBoxX, minUpperBoxY, maxUpperBoxX, maxUpperBoxY);
	      
	      //draw the quit box 
	      g.drawRect(minQuitBoxX, minQuitBoxY, maxQuitBoxX, maxQuitBoxY);
	      
	      //draw pause box
	      if(showPause) {
	    	  g.setColor(Color.blue);
	    	  g.drawRect(minMainBoxX + squareUnit * 2, minMainBoxY + squareUnit * 9, squareUnit * 5, squareUnit * 2);
	    	  g.setFont(new Font("Arial", Font.BOLD, squareUnit));
	    	  g.drawString("PAUSE", minMainBoxX + squareUnit * 3, minMainBoxY + squareUnit * 10);			
	      }
	      
	      //draw strings for texts
	      g.setColor(Color.black);
	      g.setFont(new Font("Arial", Font.BOLD, squareUnit));
	      g.drawString("Level:       " + level, minUpperBoxX, maxUpperBoxY + squareUnit * 8);
	      g.drawString("Lines:       " + lines, minUpperBoxX, maxUpperBoxY + squareUnit * 10);
	      g.drawString("Score:      " + score, minUpperBoxX,maxUpperBoxY + squareUnit * 12);
	      g.drawString("QUIT", minQuitBoxX + squareUnit, minQuitBoxY + squareUnit);
	      
	      
	      for(int j = 0; j < h; j ++) {
			   for (int i = 0; i < w; i++) {
				   marks[j][i] = 0;
			   }
		   }
		   
		   for(int i = 1; i <= h; i++) {
			   
			   checkMark.put(i, 0);
		   }
	      
	      
	    //draw blocks
	      displayDroppedPieces(droppedSquares, g);
	      displayNextPiece(nextPieceNum, g, 0, 0, squareUnit, 0);
	      displayCurrentPiece(currentPieceNum, g, xCoord, yCoord, squareUnit, rotation);
	   }
	   
	   //Block pieces
	   void blockLPurple(Graphics g, int x, int y, int squareUnit, int rotation){
		   currPiece = new HashSet<>();
		   Color c = new Color(102,0,153);
		   if(rotation == 0 ) {
			   	currPiece.add(new Square (x, y, c));
				currPiece.add(new Square (x, y + squareUnit, c));
				currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
				currPiece.add(new Square (x + squareUnit * 2, y + squareUnit, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 3;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 2;
		   }
		   else if (rotation == 1 || rotation == -3) {
			   currPiece.add(new Square (x, y, c));
				currPiece.add(new Square (x, y + squareUnit, c));
				currPiece.add(new Square (x + squareUnit, y, c));
				currPiece.add(new Square (x, y + squareUnit * 2, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 2;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 3;
		   }
		   else if (rotation == 2 || rotation == -2) {
			   currPiece.add(new Square (x, y, c));
				currPiece.add(new Square (x + squareUnit, y, c));
				currPiece.add(new Square (x + squareUnit * 2, y, c));
				currPiece.add(new Square (x + squareUnit * 2, y + squareUnit, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 3;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 2;
		   }
		   
		   else if(rotation == 3 || rotation == -1) {
			   currPiece.add(new Square (x + squareUnit, y, c));
				currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
				currPiece.add(new Square (x + squareUnit, y + squareUnit * 2, c));
				currPiece.add(new Square (x, y + squareUnit * 2, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 2;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 3;
		   }
		   
		   for (Square s: currPiece) {
			   x = s.X;
			   y = s.Y;
			   
			   g.setColor(c);
			   g.fillRect(x, y, squareUnit, squareUnit);
			   g.setColor(Color.black);
			   g.drawRect(x, y, squareUnit, squareUnit); 
		   }
	   }
	   
	   void blockLRed(Graphics g, int x, int y, int squareUnit, int rotation){
		   currPiece = new HashSet<>();
		   Color c = Color.red;
		   if(rotation == 0 ) {
			   currPiece.add(new Square (x + squareUnit * 2, y, c));
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit * 2, y + squareUnit, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 3;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 2;
		   }
		   else if (rotation == 1 || rotation == -3) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x, y + squareUnit * 2, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit *2, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 2;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 3;
		   }
		   else if (rotation == 2 || rotation == -2) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x + squareUnit, y, c));
			   currPiece.add(new Square (x + squareUnit * 2, y, c));
			   currPiece.add(new Square (x, y + squareUnit, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 3;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 2;
		   }
		   
		   else if(rotation == 3 || rotation == -1) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x + squareUnit, y, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit * 2, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 2;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 3;
		   }
		   
		   for (Square s: currPiece) {
			   x = s.X;
			   y = s.Y;
			   
			   g.setColor(c);
			   g.fillRect(x, y, squareUnit, squareUnit);
			   g.setColor(Color.black);
			   g.drawRect(x, y, squareUnit, squareUnit); 
		   }
	   }
	    
	   void blockZYellow(Graphics g, int x, int y, int squareUnit, int rotation){
		   currPiece = new HashSet<>();
		   Color c = new Color(255,255,30);
		   if(rotation == 0 || rotation == 2 || rotation == -2) {
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit * 2, y, c));
			   currPiece.add(new Square (x + squareUnit, y, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 3;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 2;
		   }
		   else if (rotation == 1 || rotation == 3 || rotation == -1 || rotation == -3) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit *2, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 2;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 3;
		   }
		   
		   for (Square s: currPiece) {
			   x = s.X;
			   y = s.Y;
			   
			   g.setColor(c);
			   g.fillRect(x, y, squareUnit, squareUnit);
			   g.setColor(Color.black);
			   g.drawRect(x, y, squareUnit, squareUnit); 
		   }
	   }
	   
	   void blockZBlue(Graphics g, int x, int y, int squareUnit, int rotation){
		   currPiece = new HashSet<>();
		   Color c = new Color(0,0,204);
		   if(rotation == 0 || rotation == 2 || rotation == -2) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit * 2, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 3;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 2;
		   }
		   else if (rotation == 1 || rotation == 3 || rotation == -1 || rotation == -3) {
			   currPiece.add(new Square (x + squareUnit, y, c));
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   currPiece.add(new Square (x, y + squareUnit * 2, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 2;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 3;
		   }
		   for (Square s: currPiece) {
			   x = s.X;
			   y = s.Y;
			   
			   g.setColor(c);
			   g.fillRect(x, y, squareUnit, squareUnit);
			   g.setColor(Color.black);
			   g.drawRect(x, y, squareUnit, squareUnit); 
		   }
	   }
	   
	   void blockT(Graphics g, int x, int y, int squareUnit, int rotation){
		   currPiece = new HashSet<>();
		   Color c = Color.orange;
		   if(rotation == 0 ) {
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y, c));
			   currPiece.add(new Square (x + squareUnit * 2, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 3;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 2;
		   }
		   else if (rotation == 1 || rotation == -3) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   currPiece.add(new Square (x, y + squareUnit *2, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 2;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 3;
		   }
		   else if (rotation == 2 || rotation == -2) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x + squareUnit, y, c));
			   currPiece.add(new Square (x + squareUnit * 2, y,c ));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 3;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 2;
		   }
		   
		   else if(rotation == 3 || rotation == -1) {
			   currPiece.add(new Square (x + squareUnit, y, c));
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
			   currPiece.add(new Square (x + squareUnit, y + squareUnit * 2, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 2;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 3;
		   }
		   
		   for (Square s: currPiece) {
			   x = s.X;
			   y = s.Y;
			   
			   g.setColor(c);
			   g.fillRect(x, y, squareUnit, squareUnit);
			   g.setColor(Color.black);
			   g.drawRect(x, y, squareUnit, squareUnit);
		   }
	   }
	   
	   void blockSqr(Graphics g, int x, int y, int squareUnit, int rotation){
		   currPiece = new HashSet<>();
		   Color c = new Color(51,153,255);
		   
		   currPiece.add(new Square (x, y, c));
		   currPiece.add(new Square (x + squareUnit, y, c));
		   currPiece.add(new Square (x, y + squareUnit, c));
		   currPiece.add(new Square (x + squareUnit, y + squareUnit, c));
		   
		   minXCoord = x;
		   maxXCoord = x + squareUnit * 2;
		   minYCoord = y;
		   maxYCoord = y + squareUnit * 2;

		   for (Square s: currPiece) {
			   x = s.X;
			   y = s.Y;
			   
			   g.setColor(c);
			   g.fillRect(x, y, squareUnit, squareUnit);
			   g.setColor(Color.black);
			   g.drawRect(x, y, squareUnit, squareUnit);   
		   }
	   }
	   
	   void blockI(Graphics g, int x, int y, int squareUnit, int rotation){
		   currPiece = new HashSet<>();
		   Color c = new Color(0,204,0);
		   if(rotation == 0 || rotation == 2 || rotation == -2) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x + squareUnit, y, c));
			   currPiece.add(new Square (x + squareUnit * 2, y, c));
			   currPiece.add(new Square (x + squareUnit * 3, y, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit * 4;
			   minYCoord = y;
			   maxYCoord = y + squareUnit;
		   }
		   else if (rotation == 1 || rotation == 3 || rotation == -1 || rotation == -3) {
			   currPiece.add(new Square (x, y, c));
			   currPiece.add(new Square (x, y + squareUnit, c));
			   currPiece.add(new Square (x, y + squareUnit * 2, c));
			   currPiece.add(new Square (x, y + squareUnit * 3, c));
			   
			   minXCoord = x;
			   maxXCoord = x + squareUnit;
			   minYCoord = y;
			   maxYCoord = y + squareUnit * 4;
		   }
		   for (Square s: currPiece) {
			   x = s.X;
			   y = s.Y;
			   
			   g.setColor(c);
			   g.fillRect(x, y, squareUnit, squareUnit);
			   g.setColor(Color.black);
			   g.drawRect(x, y, squareUnit, squareUnit); 
		   }
	   }
	   
	   
	   void displayCurrentPiece(int currentPiece, Graphics g, int x, int y, int squareUnit, int rotation) {
		   if(currentPiece == 0) {
			   blockLPurple(g, x + minMainBoxX + squareUnit * 4, y + minMainBoxY,squareUnit, rotation);
		   }
		   if(currentPiece == 1) {
			   blockLRed(g, x + minMainBoxX + squareUnit * 4, y + minMainBoxY,squareUnit, rotation);
		   }
		   if(currentPiece == 2) {
			   blockZYellow(g, x + minMainBoxX + squareUnit * 4, y + minMainBoxY,squareUnit, rotation);
		   }
		   if(currentPiece == 3) {
			   blockZBlue(g, x + minMainBoxX + squareUnit * 4, y + minMainBoxY,squareUnit, rotation);
		   }
		   if(currentPiece == 4) {
			   blockT(g, x + minMainBoxX + squareUnit * 4, y + minMainBoxY,squareUnit, rotation);
		   }
		   if(currentPiece == 5) {
			   blockI(g, x + minMainBoxX + squareUnit * 4, y + minMainBoxY,squareUnit, rotation);
		   }
		   if(currentPiece == 6) {
			   blockSqr(g, x + minMainBoxX + squareUnit * 4, y + minMainBoxY,squareUnit, rotation);
		   }
	   }
	   
	   
	   void displayNextPiece(int nextPiece, Graphics g, int x, int y, int squareUnit, int rotation) {
		   if(nextPiece == 0) {
			   blockLPurple(g, x + minUpperBoxX + squareUnit, y + minUpperBoxY +  squareUnit,squareUnit, 0);
		   }
		   if(nextPiece == 1) {
			   blockLRed(g, x + minUpperBoxX + squareUnit, y + minUpperBoxY +  squareUnit,squareUnit, 0);
		   }
		   if(nextPiece == 2) {
			   blockZYellow(g, x + minUpperBoxX + squareUnit, y + minUpperBoxY +  squareUnit,squareUnit, 0);
		   }
		   if(nextPiece == 3) {
			   blockZBlue(g, x + minUpperBoxX + squareUnit, y + minUpperBoxY +  squareUnit,squareUnit, 0);
		   }
		   if(nextPiece == 4) {
			   blockT(g, x + minUpperBoxX + squareUnit, y + minUpperBoxY +  squareUnit,squareUnit, 0);
		   }
		   if(nextPiece == 5) {
			   blockI(g, x + minUpperBoxX + squareUnit, y + minUpperBoxY +  squareUnit,squareUnit, 0);
		   }
		   if(nextPiece == 6) {
			   blockSqr(g, x + minUpperBoxX + squareUnit, y + minUpperBoxY +  squareUnit,squareUnit, 0);
		   }
	   }
	   
	   void moveDown() {
		   yCoord = yCoord + squareUnit;   
	   }
	
	   void moveLeft() {
		   xCoord = xCoord - squareUnit; 
		   minXCoord = minXCoord - squareUnit;
		   maxXCoord = maxXCoord - squareUnit;
	   }
	   void moveRight() {
		   xCoord = xCoord + squareUnit;
		   minXCoord = minXCoord + squareUnit;
		   maxXCoord = maxXCoord + squareUnit;
	   }
	   
	   boolean inMainBox() {

		   return (minXCoord >= minMainBoxX) && (maxXCoord <= minMainBoxX + maxMainBoxX) && (minYCoord >= minMainBoxY) &&(maxYCoord < minMainBoxY + maxMainBoxY); 
	   } 
	   
	   boolean collisionCheck(Set<Square> currPiece, Set<Square> droppedSquares) {
		   boolean collided = false; 

		   for (Square n : currPiece) {
			   for(Square d: droppedSquares) {
				   if((n.X == d.X && n.Y + squareUnit == d.Y)) {
					   collided = true;
					   //System.out.println("COLLIDED!!");
					   break;
				   }
			   }
		   }
		   //if the piece touches the bottom of the box 
		   if(maxYCoord == minMainBoxY + maxMainBoxY) {
			   collided = true;
		   }
		return collided;
	   }
	   
	   boolean collisionCheckSideway(Set<Square> currPiece, Set<Square> droppedSquares) {
		   boolean collided = false; 

		   for (Square n : currPiece) {
			   for(Square d: droppedSquares) {
				   if((n.X + squareUnit == d.X && n.Y == d.Y) || (n.X - squareUnit == d.X && n.Y == d.Y)) {
					   collided = true;
					   //System.out.println("COLLIDED!!");
					   break;
				   }
			   }
		   }
		   
		return collided;
	   }
	   
	   
	   void displayDroppedPieces(Set<Square>droppedSquares, Graphics g) {
		   for(Square s: droppedSquares) {
			   drawSquare(g, s);
		   }
	   }
	   
	   void drawSquare(Graphics g, Square square) {
		   //square.setType(currRandPiece);
		   
		   g.setColor(square.color);
		   System.out.println(square.color);
		   g.fillRect(square.X, square.Y, squareUnit, squareUnit);
		   g.setColor(Color.black);
		   g.drawRect(square.X, square.Y, squareUnit, squareUnit);
	   }
	   
	   
	   void cancelRow() {

		   for(Square s: droppedSquares) {

			   
			   int x = (s.X - minMainBoxX) / squareUnit;
			   int y = (s.Y - minMainBoxY) / squareUnit;
			   
			   marks[y][x] = 1;
		   }
			   
			   for(int j = 0; j < h; j++) {
				   Boolean fullRow = true;
				   for(int i = 0; i < w; i++) {
					   if(marks[j][i] == 0) {

						   fullRow = false;
						   //break;
					   }
					   }
				   if(fullRow) {
					   lines++;
					   score += level * M;
					   if(lines == N) {
						   level++;
						   speed = speed*(1 + level * S);
					   }
					   
					   checkMark.put(j, 1);
	                   Set<Square> squaresToRemove = new HashSet<>();
	                   for(Square s: droppedSquares) {
	                       int x = (s.X - minMainBoxX) / squareUnit;
	                       int y = (s.Y - minMainBoxY) / squareUnit;

	                       if(y == j) {
	                           squaresToRemove.add(s);
	                       }
	                   }
	                   for(Square s: squaresToRemove) {
	                           droppedSquares.remove(s);
	                   }
	                   for(Square s: droppedSquares) {
	                       int y = (s.Y - minMainBoxY) / squareUnit;
	                       if(y < j)
	                               s.Y += squareUnit;
	                   }
	                   System.out.println(".");
	               }
	           }
			   
			   
		   System.out.println(minMainBoxX+ " : " + minMainBoxY);
		   System.out.println(checkMark.keySet() + " : " + checkMark.values());
	   }
	   
	   boolean insidePolygon(Point2D p, Point2D[] pol){ 	
		   int n = pol.length, j = n - 1;
		   boolean b = false;
		   float x = p.x, y = p.y;
		   for (int i=0; i<n; i++){ 
			   if (pol[j].y <= y && y < pol[i].y &&
					   Tools2D.area2(pol[j], pol[i], p) > 0 ||
					   pol[i].y <= y && y < pol[j].y &&
					   Tools2D.area2(pol[i], pol[j], p) > 0) b = !b;
			   j = i;
		   }
		   return b;
	   }
	   
	   
	}
}

