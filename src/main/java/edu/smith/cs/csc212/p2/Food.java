package edu.smith.cs.csc212.p2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class Food extends WorldObject {


	public Food(World world) {
		super(world);

	}

	@Override
	public void draw(Graphics2D g) {
		//draws our fish-food
		g.setColor(Color.orange);

		Ellipse2D food = new Ellipse2D.Double(0, 0, .5, .5);
		g.fill(food);
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

}
