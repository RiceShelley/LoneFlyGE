package game_engine_demo;

import java.awt.Dimension;

import lonefly.game.LoneflyGE;
import lonefly.game.engine.display.Display;
import lonefly.game.engine.input.LfGEKeyListener;
import lonefly.game.engine.util.ILevels;

public class TesterGame {

	public static void main(String[] args) {
		// start game engine - parameters - set show console to true and output
		// filter to ERROR
		new LoneflyGE(true, ILevels.SMALLTALK);
		// create new keyListener
		LfGEKeyListener key = new LfGEKeyListener();
		// create new demo class that extends activity pass it out key listener
		Week4Demo demo1 = new Week4Demo(key);
		// create display frame pass it frame size, visibility, name, and a
		// key listener to bind to the frame
		Display d = new Display(new Dimension(1000, 1080), true, "game", key);
		// add Activity to display
		d.addActivity(demo1);
		// game loop
		while (true) {
			// update activity
			demo1.update();
		}
	}

}
