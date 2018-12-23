package edu.smith.cs.csc212.p2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

/**
 *  @author jfoley
 */
public class Rock extends WorldObject {
	/**
	 * I took these colors from Wikipedia's Cool and Warm Gray sections.
	 * https://en.wikipedia.org/wiki/Shades_of_gray#Cool_grays
	 * https://en.wikipedia.org/wiki/Shades_of_gray#Warm_grays
	 */
	//these are the colors we will randomly assign to our rocks
	Color[] ROCK_COLORS = new Color[] {
			new Color(144,144,192),
			new Color(145,163,176),
			new Color(112,128,144),
			new Color(94,113,106),
			new Color(76,88,102),
			new Color(170,152,169),
			new Color(152,129,123),
			new Color(138,129,141),
			new Color(72,60,50)
			
	};
	
	//Indexes the ROCK_COLORS array.
	Color color;
		
	/**
	 * Construct a Rock in our world.
	 * @param world - the grid world.
	 */
	public Rock(World world) {
		super(world);
		
		// Initializing rock color index to a random number!
		this.color=ROCK_COLORS[ rand.nextInt(ROCK_COLORS.length)];
		
	}
	
	//Draws a rock!

	@Override
	public void draw(Graphics2D g) {
		
		// sets the right rock color		
		g.setColor(color);
		RoundRectangle2D rock = new RoundRectangle2D.Double(-.5,-.5,1,1,0.3,0.3);
		g.fill(rock);
	}

	@Override
	public void step() {
		// Rocks don't actually *do* anything.		
	}

}
