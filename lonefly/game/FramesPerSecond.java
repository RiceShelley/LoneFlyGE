package lonefly.game;

import java.util.TimerTask;

/*
 * Author: Rice Shelley
 * Version: 4/15/2016
 * Purpose: calculate frames per second 
 */
public class FramesPerSecond extends TimerTask {

	// Average amount of frames rendered in the last 2 seconds
	private float fps = 0;
	//target fps 
	private float target = 60;
	// keeps track of how many frames are rendered in a second
	private int framesRendered;

	// Increment frames rendered
	public void tick() {
		framesRendered++;
	}

	// timer task overridden from super class
	@Override
	public void run() {
		if (fps == 0) {
			fps = framesRendered;
		} else {
			// amount of frames displayed from two second ago + amount of frames
			// rendered one second ago / 2
			fps = (fps + framesRendered) / 2;
		}
		framesRendered = 0;
	}

	// get frames per second
	public float getFps() {
		return fps;
	}
	
	//get target fps
	public float getTarget() {
		return target;
	}
}
