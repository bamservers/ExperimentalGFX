package gfx;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;


public class CurrentGFX extends AbstractGFX {
	
	private BufferedImage buffer;
	private Graphics2D g2d;
	private int currentBufferIndex = 0;
	private double MAX_PROGRESS = 10;
	private double objectProgress = 0;
	
	private int maxx;
	private int maxy;
	
	public CurrentGFX()
	{
		// move to the middle of the screen, start drawing from there
		//g2d.scale(2, 2);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();

		maxx = ge.getMaximumWindowBounds().width;
		maxy = ge.getMaximumWindowBounds().height;

		buffer = gc.createCompatibleImage(maxx, maxy, Transparency.TRANSLUCENT);//new BufferedImage (1000, 1000, BufferedImage.TYPE_INT_ARGB);//
		g2d = (Graphics2D) buffer.createGraphics();

		for (int cnt = 0;cnt < MAX_OBJECTS;cnt++)
		{
			newObject(cnt);
			x[cnt] = Math.random() * maxx;
			y[cnt] = Math.random() * maxy;
		}

		Thread myLogic = new Thread (new Runnable ()
		{
			public void run ()
			{
				while (/*GFXWindow.WINDOW_IS_VISIBLE*/true)
				{
					for (int cnt = 0;cnt < MAX_OBJECTS;cnt++)
					{
						x[cnt]+= vx[cnt];
						y[cnt]+= vy[cnt];
						
						if (x[cnt] > maxx || y[cnt] > maxy/* || x[cnt] < 0 || y[cnt] < 0 */) newObject(cnt);
					}
					//try {Thread.sleep(10);}catch(Exception e){}
				}
			}
		}, "GFX Logic");
		myLogic.setDaemon(true);// Make this a user thread, meaning it shouldn't prevent the JVM fropm stopping (if the app stops)
		myLogic.start();
	}
	
	public void resized(Dimension newSize)
	{
		maxx = newSize.width;
		maxy = newSize.height;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		
		// rebuild the buffer on resize
		buffer = gc.createCompatibleImage(maxx, maxy, Transparency.TRANSLUCENT);//new BufferedImage (1000, 1000, BufferedImage.TYPE_INT_ARGB);//
		g2d = (Graphics2D) buffer.createGraphics();

		for (int cnt = 0;cnt < MAX_OBJECTS;cnt++)
		{
			newObject(cnt);
			x[cnt] = Math.random() * maxx;
			y[cnt] = Math.random() * maxy;
		}
	}
	
	public void paint(Graphics2D g) {
		
		// calculate the middle of the screen
//		int midx = (int)(g.getClipBounds().getWidth() / 2d);
//		int midy = (int)(g.getClipBounds().getHeight() / 2d);
//		double maxDist = Math.sqrt((midx * midx) + (midy * midy));

		//System.out.println ("currentBufferIndex: " + currentBufferIndex);		
		//int fade;
		
//		for (int cnt = currentBufferIndex;cnt < BUFFER_COUNT;cnt++)
//		{
//			//fade = (int)(((double)(BUFFER_COUNT - (cnt - currentBufferIndex)) / (double)BUFFER_COUNT) * 255d);// goes backwards
//			//fade = (int)(((double)((cnt - currentBufferIndex)) / (double)BUFFER_COUNT) * 255d);
//			g2d [cnt].setColor(fadeAmount);
//			g2d [cnt].fillRect(0, 0, (int)g.getClipBounds().getWidth(), (int)g.getClipBounds().getWidth());
//			g.drawImage (buffer [cnt], 0, 0, null);
//			//System.out.println (cnt + " / " + BUFFER_COUNT + ", fade=" + g2d [cnt].getColor ().getAlpha());
//		}
//
//		for (int cnt = 0;cnt < currentBufferIndex;cnt++)
//		{
//			//fade = (int)(((double)(currentBufferIndex - cnt) / (double)BUFFER_COUNT) * 255d);// goes backwards
//			//fade = (int)(((double)(BUFFER_COUNT - (currentBufferIndex - cnt)) / (double)BUFFER_COUNT) * 255d);
//			g2d [cnt].setColor(fadeAmount);
//			g2d [cnt].fillRect(0, 0, (int)g.getClipBounds().getWidth(), (int)g.getClipBounds().getWidth());
//			g.drawImage (buffer [cnt], 0, 0, null);			
//			//System.out.println (cnt + " / " + BUFFER_COUNT + ", fade=" + g2d [cnt].getColor ().getAlpha());
//		}

		g2d.setColor(fadeAmount);
		g2d.fillRect(0, 0, maxx, maxy);
		g2d.translate(maxx / 2d, maxy / 2d);
		g2d.rotate(0.01d);
		g2d.translate(-maxx / 2d, -maxy / 2d);
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawRect(0, 0, maxx, maxy);
//		for (int cnt = BUFFER_COUNT - 1;cnt > -1;cnt--)
//		{
//			int bufferIndex = currentBufferIndex - cnt;
//			if (bufferIndex < 0)bufferIndex+= BUFFER_COUNT;
//			
////			g2d [bufferIndex].setColor(fadeAmount);
////			g2d [bufferIndex].fillRect(0, 0, (int)g.getClipBounds().getWidth(), (int)g.getClipBounds().getWidth());
//			g.drawImage (buffer [bufferIndex], 0, 0, null);
//			//System.out.println (bufferIndex + " / " + BUFFER_COUNT + ", painting: " + g2d [bufferIndex].hashCode());
//			//System.out.print ("");
//		}
		
		draw (g2d);
		g.drawImage (buffer, 0, 0, null);

	}
	Color fadeAmount = new Color (0, 0, 0, 50);
	Ellipse2D.Double circle = new Ellipse2D.Double();
	
	int MAX_OBJECTS = 1250;
	double x[] = new double[MAX_OBJECTS];
	double y[] = new double[MAX_OBJECTS];
	double vx[] = new double[MAX_OBJECTS];
	double vy[] = new double[MAX_OBJECTS];
	Color c[] = new Color[MAX_OBJECTS];
	
	public void draw (Graphics2D g)
	{
//		g.setColor(new Color (0, 0, 0, 255));
//		g.fillRect(0, 0, 1000, 1000);
		//System.out.println("drawing: " +  g.hashCode());
		
//		int x = 500 + (int)(Math.sin((Math.PI * 2d) * (objectProgress / MAX_PROGRESS)) * 100d);
//		int y = 500 + (int)(Math.cos((Math.PI * 2d) * (objectProgress / MAX_PROGRESS)) * 100d);
//		g.setColor(Color.green);
//		circle.setFrame(x - 25, y - 25, 50, 50);
//		g.fill(circle);
//		objectProgress++;

		for (int cnt = 0;cnt < MAX_OBJECTS;cnt++)
		{
			g.setColor(c [cnt]);
			circle.setFrame((int)x[cnt] - 2, (int)y[cnt] - 2, 4, 4);
			g.draw(circle);
		}
	}

	private void newObject (int index)
	{
		if (Math.random() > 0.5)
		{
			x[index] = Math.random() * maxx;
			y[index] = -5;
		}
		else
		{
			x[index] = -5;
			y[index] = Math.random() * maxy;	
		}
		
		vx[index] = Math.random() / 10000d; 
		vy[index] = Math.random() / 10000d; 
		
		c[index] = new Color (0/*(int)(Math.random() * 255)*/, (int)(Math.random() * 255), 0/*(int)(Math.random() * 255)*/);
	}
	
//	public static class RenderThread implements Runnable
//	{
//		public boolean DONE = false;
//		private Graphics2D g2d;
//		private Vector[] vectorChain;
//		private double rate;
//		
//		public RenderThread (Graphics2D g2d, Vector[] vectorChain, double rate)
//		{
//			this.g2d = g2d;
//			this.vectorChain = vectorChain;
//			this.rate = rate;
//		}
//		
//		public void run ()
//		{
//			//g2d.scale(1.01, 1.01);
//			//g2d.rotate(1);
//			for (int steps = 0;steps < 1024;steps++)
//			{
//				double oldx = 0;
//				double oldy = 0;
//				//g2d.rotate(1);
//				//g2d.translate(0.9, 0.9);
//
//				// loop through the vector chains and draw them one after another
//				for (int cnt = 0;cnt < vectorChain.length;cnt++)
//				{
//					//g2d.rotate(.1);
//					double newx = Math.sin(vectorChain[cnt].dir) * vectorChain[cnt].length;
//					double newy = Math.cos(vectorChain[cnt].dir) * vectorChain[cnt].length;
//		
//					g2d.setColor(vectorChain[cnt].col);
//					//g2d.drawLine((int)oldx, (int)oldy, (int)(newx + oldx), (int)(newy + oldy));	
//					g2d.drawLine((int)newx, (int)newy, (int)(newx + 1), (int)(newy + 1));
//					//g2d.drawString(".", (int)(oldx + newx), (int)(oldy + newy));
//					
//					oldx = newx + oldx;
//					oldy = newy + oldy;
//					
//					vectorChain[cnt].dir+= Math.PI / 512;
//					vectorChain[cnt].length += 0.5;
//				}
//			}
//			DONE = true;
//		}
//	}
//	
//	private static final class Vector
//	{
//		double length;
//		double dir;
//		Color col;
//		
//		public Vector (double length, double dir, Color col)
//		{
//			this.length = length;
//			this.dir = dir;
//			this.col = col;
//		}
//	}
}
