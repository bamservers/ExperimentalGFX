package tunnel;
import java.awt.Color;
import java.awt.Graphics2D;

import gfx.*;


public class GFX2d extends AbstractGFX {
	double scale = .5;
	double inc = .5;
	double rot = 0;
	
	public void paint(Graphics2D g2d) {
		g2d.scale(.2, .2);
		double oldx = -1;
		double oldy = -1;
		
		int midx = (int)(g2d.getClipBounds().getWidth() / 2d);
		int midy = (int)(g2d.getClipBounds().getHeight() / 2d);
		double maxDist = Math.sqrt((midx * midx) + (midy * midy));
		
		g2d.translate(midx, midy);

		for (double x = 0;x < g2d.getClipBounds().getWidth() * scale;x+= inc)
		{
			double y = formula (x) * 2.1;
			
			double newx = x / scale;
			double newy = y / scale;
			
			double dist = Math.sqrt(((newx - midx) * (newx - midx)) + ((newy - midy) * (newy - midy)));
			
			dist = (1d / (dist / maxDist)) - 1;
			if (dist > 1) dist = 1;
			
			int r = (int)Math.max(0, Math.min(255, (dist * 255)));
			int g = (int)Math.max(0, Math.min(255, (dist * 255)));
			int b = (int)Math.max(0, Math.min(255, (dist * 255)));
			
			g2d.setColor(new java.awt.Color (r, g, b));
						
			g2d.drawLine((int)oldx, (int)oldy, (int)(newx - 100), (int)(newy + 100));
			
			oldx = newx;// * 2.5;
			oldy = newy;// * 2.5;
			
			g2d.rotate(x + rot);
		}
		
		rot-= 0.00001;
	}

	// returns the Y value, after giving X to the formula
	private double formula (double x)
	{
		return .25 * (1 + (4 * x) - ((1 + 2 * x) * Math.cos((x * .001)) * Math.sin((-x * .01))));
	}

}