package lonefly.game;

import com.badlogic.gdx.utils.GdxNativesLoader;

import lonefly.game.engine.display.GEdit;
import lonefly.game.engine.display.GraphicLoader;
import lonefly.game.engine.util.ConsoleOutput;
import lonefly.game.engine.util.ILevels;

/*
 * Author: Rice Shelley
 * Version: 3/11/2016
 * Purpose: initiate lonefly engine 
 * assign class 
 */

public class LoneflyGE {

	// object for logging info to the console for the game engine
	public static ConsoleOutput LOG;
	// object for uploading graphics
	public static GraphicLoader GRAPHIC_LOADER;
	// object for editing graphics
	public static GEdit GEDIT;
	// scale factor for converting box2D(meters) to pixels physics scale
	public static float PHYSICS_SCALE = 150;
	// amount of sleep time engine takes to keep at the target fps
	public static float SLEEP_TIME = 30;

	// default constructor
	public LoneflyGE() {
		LOG = new ConsoleOutput(true, ILevels.WARNING);
		GRAPHIC_LOADER = new GraphicLoader();
		GEDIT = new GEdit();
	}

	// Constructor with parameters <- refer to demos for use
	public LoneflyGE(boolean consoleShow, ILevels iLvls) {
		GdxNativesLoader.load();
		LOG = new ConsoleOutput(consoleShow, iLvls);
		GRAPHIC_LOADER = new GraphicLoader();
		GEDIT = new GEdit();
	}

	// convert pixels to box2D meters
	public static float mtp(float val) {
		return val / LoneflyGE.PHYSICS_SCALE;
	}
}
