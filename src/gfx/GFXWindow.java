package gfx;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tunnel.*;

public class GFXWindow extends JFrame implements KeyListener
{
	//public static boolean WINDOW_IS_VISIBLE = true; 
	private static GFXWindow fw;

	private List<AbstractGFX> agfx = new ArrayList<AbstractGFX>();// TODO: Load this array with all the GFX experiments
	private JFractalPanel jfp;
	private int currentGFX = 0;

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Load all the experiments
		List<AbstractGFX> agfx = new ArrayList<AbstractGFX>();
		agfx.add (new CurrentGFX());
		agfx.add (new tunnel.GFX1());
		agfx.add (new tunnel.GFX2());
		agfx.add (new tunnel.GFX2a());
		agfx.add (new tunnel.GFX2b());
		agfx.add (new tunnel.GFX2c());
		agfx.add (new tunnel.GFX2d());
		agfx.add (new tunnel.GFX3());
		agfx.add (new tunnel.GFX4());
		agfx.add (new tunnel.GFX5());
		agfx.add (new GFX6());
		agfx.add (new GFX7());
		agfx.add (new GFX7a());
		
		fw = new GFXWindow (agfx);//(new CurrentGFX());//(new CurrentFractal());
		fw.setDefaultCloseOperation(EXIT_ON_CLOSE);		
		fw.setVisible(true);
		
		while (fw.isVisible())
		{
			fw.repaint();
			
			try {Thread.sleep(33);}// TODO: Separate the tick logic from the paiting logic, so we can choose to increase or decrease the simulation speed, while keeping a steady framerate
			catch (Exception e){}
		}
		//WINDOW_IS_VISIBLE = false;
		//fw.dispose();
		
	}
	
	public GFXWindow(AbstractGFX af)
	{
		this (Arrays.asList(af));
	}
	
	public GFXWindow(List<AbstractGFX> afs)
	{
		super ("Experimental Graphics Window");
		setSize(1000, 1000);
		setLocationRelativeTo(null);// center of the screen // setLocation (300, 000);
		
		this.agfx = afs;
		jfp = new JFractalPanel (afs.get(currentGFX));
		
		this.add(jfp);
		
		this.addKeyListener(this);
		this.addComponentListener(new ComponentAdapter() 
		{  
	        public void componentResized(ComponentEvent evt)
	        {
	            Component c = (Component)evt.getSource();
	            System.out.println ("Resized: " + evt.getComponent().getSize());
				jfp.flogic.resized(jfp.getSize()/* evt.getComponent().getSize() */);
	        }
		});
	}
	
	@Override
	public void keyTyped(KeyEvent e)		{	}
	@Override
	public void keyReleased(KeyEvent e)	{	}
	@Override
	public void keyPressed(KeyEvent e)
	{
		//System.out.println ("Key Typed: " + e.toString());
		
		// Close the window, on exit
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			fw.dispose();
			return;
		}
		else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
		{
			currentGFX = (currentGFX + 1) % agfx.size();
			jfp.flogic = agfx.get(currentGFX);
		}
		else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
		{
			currentGFX = (currentGFX - 1) % agfx.size();
			currentGFX = currentGFX < 0 ? agfx.size() - 1 : currentGFX;
			jfp.flogic = agfx.get(currentGFX);
			jfp.flogic.resized(jfp.getSize());
		}
		else
		{
			// Forward the key press logic to the currently active gfx logic
			jfp.flogic.keyPressed(e);
		}
	}
}


class JFractalPanel extends JPanel
{
	protected AbstractGFX flogic;
	final private static AffineTransform identityTransform = new AffineTransform();
	
	public JFractalPanel (AbstractGFX p_flogic)
	{
		super (false);
		flogic = p_flogic;
		this.setBackground(Color.black);
	}
	
	// draw the battle arena
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setBackground(Color.black);
		g2.setColor(Color.white);
		g2.clearRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		// set zoom and view location
		// TODO: Map this functionality to the mouse or keyboard
		double scaleFactor = .5;
		
		// TODO: Fix this portion of the code, because the scaling & translation aren't working as expected.
		g2.translate(getSize().getWidth() / (1/scaleFactor), getSize().getHeight() / (1/scaleFactor));
		g2.scale(scaleFactor, scaleFactor);
		g2.translate(-getSize().getWidth() / (1/scaleFactor), -getSize().getHeight() / (1/scaleFactor));
				
		try
		{
			flogic.paint(g2);
		}
		catch (Exception e)
		{
			System.out.println ("Error while trying to repaint: " + e.toString());
			e.printStackTrace();
		}

		// Reset the transform, so we can properly draw the string in normal circumstances
		g2.setTransform(identityTransform);
		g2.setColor(Color.RED);
		g2.drawString("GFX: " + flogic.getClass().getName(), 0, 15);
	}
}