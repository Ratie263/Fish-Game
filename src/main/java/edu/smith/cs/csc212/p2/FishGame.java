package edu.smith.cs.csc212.p2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FishGame {
	/**
	 * This is the world in which the fish are missing. (It's mostly a List!).
	 */
	World world;
	/**
	 * The player (a Fish.COLORS[0]-colored fish) goes seeking their friends.
	 */
	Fish player;
	/**
	 * The home location.
	 */
	FishHome home;
	/**
	 * These are the missing fish!
	 */
	List<Fish> missing;
	/**
	 * These are fish we've found!
	 */
	List<Fish> found;
	/**
	 * These fish are'nt home!
	 */
	List<Fish> notHome;
	List<Bubble> bubbles;
	int stepsTaken;

	int foodCount;
	int NUM_Bubbles;
	public static final int NUM_Rocks = 8;
	/**
	 * Score!
	 */
	int score;
	int twentySteps;

	/**
	 * Create a FishGame of a particular size.
	 * 
	 * @param w how wide is the grid?
	 * @param h how tall is the grid?
	 */

	public FishGame(int w, int h) {
		NUM_Bubbles = 10;
		world = new World(w, h);

		missing = new ArrayList<Fish>();
		found = new ArrayList<Fish>();
		notHome = new ArrayList<Fish>();
		bubbles = new ArrayList<Bubble>();
		// Add a home!
		home = world.insertFishHome();
		// insert Bubbles randomly
		for (int i = 0; i < NUM_Bubbles; i++) {
			world.insertBubbleRandomly();

		}
		// for inserting rocks randomly
		for (int i = 0; i < NUM_Rocks; i++) {
			world.insertRockRandomly();
			world.insertFallingRockRandomly();
		}

		world.insertSnailRandomly();

		// Make the player out of the 0th fish color.
		player = new Fish(0, world);
		// Start the player at "home".
		player.setPosition(home.getX(), home.getY());
		player.markAsPlayer();
		world.register(player);

		// Generate fish of all the colors but the first into the "missing" and the
		// "notHome" List.
		for (int ft = 1; ft < Fish.COLORS.length; ft++) {
			Fish friend = world.insertFishRandomly(ft);
			missing.add(friend);
			notHome.add(friend);
		}

	}

	// returns number of missing fish
	public int missingFishLeft() {
		return missing.size();
	}

	/**
	 * This method is how the PlayFish app tells whether we're done.
	 * 
	 * @return true if the player has won (or maybe lost?).
	 */
	public boolean gameOver() {
		// We want to bring the fish home before we win!
		return notHome.isEmpty();

	}

	/**
	 * Update positions of everything (the user has just pressed a button).
	 */
	public void step() {
		// Keep track of how long the game has run.
		this.stepsTaken += 1;
		// add food randomly for the fish to eat!
		Random rand = new Random();
		this.foodCount = rand.nextInt(500);
		if ((foodCount % 3) == 0) {
			world.insertFoodRandomly();
		}

		List<Fish> wanderedHome = new ArrayList<>();
		// These are all the objects in the world in the same cell as the fish.
		for (Fish fish : missing) {
			List<WorldObject> overlap = fish.findSameCell();
			overlap.remove(fish);
			// if fish steps on food, fish eats food and player gets no points
			for (WorldObject wo : overlap) {
				if (wo instanceof Food) {
					wo.remove();
				}
				// if fish steps on bubble, bubble dissapears and fish enters "bubble" state
				if (wo instanceof Bubble) {
					wo.remove();

				}
				// when fish get home, wander home, they are removed from screen, but player
				// only gets one point!
				if (wo instanceof FishHome) {
					wanderedHome.add(fish);
					notHome.remove(fish);
					world.remove(fish);
					score += 1;
				}

			}
		}
		for (Fish f : wanderedHome) {
			missing.remove(f);

		}
		// if bubble hits onto rock, bubble bursts
		for (Bubble b : bubbles) {
			List<WorldObject> overlap = b.findSameCell();
			// The player is there, too, let's skip them.
			overlap.remove(b);

			// If we hit a rock, bubble bursts.
			for (WorldObject wo : overlap) {

				if (wo instanceof Rock) {
					// Remove this bubble from the world
					world.remove(b);
				}
			}
		}

		List<WorldObject> overlap = this.player.findSameCell();
		// The player is there, too, let's skip them.
		overlap.remove(this.player);

		// If we find a fish, remove it from missing.
		for (WorldObject wo : overlap) {
			// It is missing if it's in our missing list.
			if (missing.contains(wo)) {
				// Remove this fish from the missing list.
				missing.remove(wo);

				// fish has been found, so add to found list
				found.add((Fish) wo);

				// Increase score by color when you find a fish!
				if (wo instanceof Fish) {
					Color fishColor = ((Fish) wo).getColor();
					if (fishColor == Color.green) {
						score += 10;
					} else if (fishColor == Color.yellow) {
						score += 15;
					} else if (fishColor == Color.pink) {
						score += 20;
					}
					// white is the special fish!
					else if (fishColor == Color.white) {
						score += 25;
					}
					// orange and red aren't worth much
					else {
						score += 5;

					}
				}
			}
			// when fish wander home, remove them from screen
			if (wo instanceof FishHome) {

				for (Fish fish : found) {

					notHome.remove(fish);
					world.remove(fish);
				}
				found.clear();
			}

			if (wo instanceof Food) {

				score += 5;
				world.remove(wo);
			}

		}
		// Make sure missing fish *do* something.
		wanderMissingFish();
		// When fish get added to "found" they will follow the player around.
		World.objectsFollow(player, found);
		// Step any world-objects that run themselves.
		world.stepAll();

	}

	private Object trappedInBubble(boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Call moveRandomly() on all of the missing fish to make them seem alive.
	 */
	private void wanderMissingFish() {
		Random rand = ThreadLocalRandom.current();
		for (Fish lost : missing) {

			// 30% of the time, lost fish move randomly.
			if ((lost.FastScared() == 1) && (rand.nextDouble() < 0.8)) {
				lost.moveRandomly();
			} else if ((lost.FastScared() == 0) && (rand.nextDouble() < 0.3)) {
				lost.moveRandomly();
			}
		}
	}

	/**
	 * This gets a click on the grid. We want it to destroy rocks that ruin the
	 * game.
	 * 
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 */
	public void click(int x, int y) {
		System.out.println("Clicked on: " + x + "," + y + " world.canSwim(player,...)=" + world.canSwim(player, x, y));
		List<WorldObject> atPoint = world.find(x, y);
		
		for (WorldObject wo : atPoint) {
			//Allows the user to click and remove rocks.
			if (wo instanceof Rock) {
				world.remove(wo);
			}
			//allows use to remove bubbles
			if (wo instanceof Bubble) {
				world.remove(wo);
				
			}
		}
	}
}
