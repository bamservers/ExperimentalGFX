package tunnel;
import java.awt.Graphics2D;

import gfx.*;


public class GFX2a extends AbstractGFX {
	double scale = 1;
	double inc = .1;
	double rot = 0;
	
	public void paint(Graphics2D g2d) {
		//g2d.scale(.1, .1);
		double oldx = -1;
		double oldy = -1;
		
		int midx = (int)(g2d.getClipBounds().getWidth() / 2d);
		int midy = (int)(g2d.getClipBounds().getHeight() / 2d);
		
		g2d.translate(midx, midy);

		for (double x = 0;x < g2d.getClipBounds().getWidth() * scale;x+= inc)
		{
			double y = formula (x);
			
			double newx = x / scale;
			double newy = y / scale;
			
			double distPercentageToOuterrEdge = Math.sqrt(((newx - midx) * (newx - midx)) + ((newy - midy) * (newy - midy)));
			int r = (int)(distPercentageToOuterrEdge) % 255;
			int g = (int)(distPercentageToOuterrEdge) % 255;
			int b = (int)(distPercentageToOuterrEdge) % 255;
			g2d.setColor(new java.awt.Color (r, g, b));
			
			if (oldx != -1 && oldy != -1)
			{
				g2d.drawLine((int)oldx, (int)oldy, (int)newx + 100, (int)newy + 100);
			}
			
			
			oldx = newx * 1.5;
			oldy = newy * 1.5;
			
			g2d.rotate(x + rot);
		}
		
		rot+= 0.00005;
		//scale+= 0.5;
	}
	
	// returns the Y value, after giving X to the formula
	private double formula (double x)
	{
		return .25 * (1 + (4 * x) - ((1 + 2 * x) * Math.cos(Math.PI * (x * .0001))));
	}

}
