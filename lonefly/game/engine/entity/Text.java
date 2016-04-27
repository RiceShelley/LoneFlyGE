package lonefly.game.engine.entity;

import java.awt.Color;

/*
 * Author: Rice Shelley
 * Version: 4/22/2016
 * Purpose: to graphically display text 
 */

public class Text extends Entity {

	// text the text object displays
	private String text;
	// color of the text
	private Color color;

	// Default constructor - set text to "Undefined" - set color to blue - put
	// text at x=0, y=0
	public Text() {
		super("Undefined", 0, 0, 50, "Undefined".length() * 10, null, null);
		text = super.getName();
		color = Color.BLUE;
	}

	// constructor with parameters
	public Text(String name, float x, float y, float width, float height, String text, Color color) {
		super(name, x, y, width, height, null, null);
		this.text = text;
		this.color = color;
	}

	@Override
	public String toString() {
		return super.getName();
	}
	
	// setters
	// set text
	public void setText(String text) {
		this.text = text;
	}

	// set color
	public void setColor(Color color) {
		this.color = color;
	}

	// getters
	// get text
	public String getText() {
		return text;
	}

	// get color
	public Color getColor() {
		return color;
	}

}
