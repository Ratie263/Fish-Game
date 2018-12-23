package edu.smith.cs.csc212.p2;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class FallingRock extends Rock{
	
	public FallingRock(World world) {
		super(world);
	}
	public void draw(Graphics2D g) {
		// Draws our rocks
		
		g.setColor(color);
		RoundRectangle2D rock = new RoundRectangle2D.Double(-.5,-.5,1,1,0.3,0.3);
		g.fill(rock);
		
	}
	@Override
	public void step() {
		this.moveDown();
	}


}
