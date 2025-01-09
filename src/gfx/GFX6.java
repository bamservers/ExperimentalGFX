package gfx;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class GFX6 extends AbstractGFX {
	private BufferedImage buffer = new BufferedImage (2000, 2000, BufferedImage.TYPE_INT_ARGB);
	private Graphics2D g2d = (Graphics2D) buffer.createGraphics();
	
	RenderThread [] renderThreads = new RenderThread [5];
	
	
	public void paint(Graphics2D g) {
		//g2d.scale(.1, .1);
		
		// calculate the middle of the screen
		int midx = (int)(g.getClipBounds().getWidth() / 2d);
		int midy = (int)(g.getClipBounds().getHeight() / 2d);
		double maxDist = Math.sqrt((midx * midx) + (midy * midy));

		// move to the middle of the screen, start drawing from there
		g2d.translate(midx, midy);
		
		for (int cnt = 0;cnt < renderThreads.length;cnt++)
		{
			if (renderThreads[cnt] == null || renderThreads[cnt].DONE)
			{
				Vector[] vectorChain = new Vector []{
						new Vector ((Math.random() - 0.5) * 800, Math.random() * Math.PI, Color.red),
						new Vector ((Math.random() - 0.5) * 800, Math.random() * Math.PI, Color.blue),
						new Vector ((Math.random() - 0.5) * 800, Math.random() * Math.PI, Color.yellow),
						new Vector ((Math.random() - 0.5) * 800, Math.random() * Math.PI, Color.green)
					};
				
				renderThreads[cnt] = new RenderThread (g2d, vectorChain, 0);
				new Thread (renderThreads[cnt]).start();
			}
		}
		g.drawImage (buffer, 0, 0, null);
	}
	
	
	public static class RenderThread implements Runnable
	{
		public boolean DONE = false;
		private Graphics2D g2d;
		private Vector[] vectorChain;
		private double rate;
		
		public RenderThread (Graphics2D g2d, Vector[] vectorChain, double rate)
		{
			this.g2d = g2d;
			this.vectorChain = vectorChain;
			this.rate = rate;
		}
		
		public void run ()
		{
			for (int steps = 0;steps < 1000;steps++)
			{
				double oldx = 0;
				double oldy = 0;
				g2d.rotate(1);


				// loop through the vector chains and draw them one after another
				for (int cnt = 0;cnt < vectorChain.length;cnt++)
				{
					g2d.rotate(.1);
					double newx = Math.sin(vectorChain[cnt].dir) * vectorChain[cnt].length;
					double newy = Math.cos(vectorChain[cnt].dir) * vectorChain[cnt].length;
		
					g2d.setColor(vectorChain[cnt].col);
					//g2d.drawLine((int)oldx, (int)oldy, (int)(newx + oldx), (int)(newy + oldy));	
					g2d.drawLine((int)newx, (int)newy, (int)(newx + 1), (int)(newy + 1));
					//g2d.drawString(".", (int)(oldx + newx), (int)(oldy + newy));
					
					oldx = newx + oldx;
					oldy = newy + oldy;
					
					vectorChain[cnt].dir+= 0.1;// * (cnt + 1);
					vectorChain[cnt].length += /*(Math.random() - 0.51 )**/ 1;//Math.random();
				}
			}
			DONE = true;
		}
	}
	
	private static final class Vector
	{
		double length;
		double dir;
		Color col;
		
		public Vector (double length, double dir, Color col)
		{
			this.length = length;
			this.dir = dir;
			this.col = col;
		}
	}
}
