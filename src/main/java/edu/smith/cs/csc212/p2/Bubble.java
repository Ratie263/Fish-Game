package edu.smith.cs.csc212.p2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Bubble extends WorldObject {
	Color color;

	public Bubble(World world) {
		super(world);
		
	}

	@Override
	public void draw(Graphics2D g) {
		//draws our bubbles
		Color color = new Color(1f, 1f, 1f, 0.5f);

		g.setColor(color);
		Ellipse2D bubble = new Ellipse2D.Double(-.5, -.5, 1, 1);
		g.fill(bubble);
	}

	@Override
	//makes our bubbles"float"
	public void step() {
		this.moveUp();
	}

}
