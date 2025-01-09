package gfx;
import java.awt.*;
import java.awt.event.*;

public abstract class AbstractGFX implements KeyListener
{
	public abstract void paint(java.awt.Graphics2D g2d);
	
	@Override
	public void keyTyped(KeyEvent e)	{	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		System.out.println ("[Abstract GFX] Key Typed: " + e.toString());		
	}

	@Override
	public void keyReleased(KeyEvent e)	{	}

	public void resized(Dimension size){}
}
