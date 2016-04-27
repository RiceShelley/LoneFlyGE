package lonefly.game.engine.entity;

import java.awt.image.BufferedImage;

import lonefly.game.LoneflyGE;
import lonefly.game.engine.display.HasGraphic;
import lonefly.game.engine.util.ILevels;

/*
 * Author: Rice Shelley
 * Version: 3/14/2016
 * Purpose: class for just uploading graphics such as images 
 * that can be moved around but have no effect on other game objects 
 */

public class Graphic extends Entity implements HasGraphic {

	// BufferedImage graphic
	private BufferedImage graphic;

	// create Graphic via resource path
	public Graphic(String name, float x, float y, float width, float height, String gPath) {
		super(name, x, y, width, height, null, null);
		graphic = LoneflyGE.GRAPHIC_LOADER.loadImage(gPath);
		// log graphic creation
		LoneflyGE.LOG.println("New Graphic created! name <" + getName() + ">", ILevels.SMALLTALK);
	}

	// create graphic via Buffered Image
	public Graphic(String name, float x, float y, float width, float height, BufferedImage graphic) {
		super(name, x, y, width, height, null, null);
		this.graphic = graphic;
		// log graphic creation
		LoneflyGE.LOG.println("New Graphic created! name <" + getName() + ">", ILevels.SMALLTALK);
	}

	// change graphic via image path
	public void changeGraphic(String gPath) {
		graphic = LoneflyGE.GRAPHIC_LOADER.loadImage(gPath);
	}

	// change graphic via bufferdImage
	@Override
	public void setGraphic(BufferedImage img) {
		graphic = img;
	}

	@Override
	public BufferedImage getGraphic() {
		return graphic;
	}

	@Override
	public String toString() {
		return "Graphic: " + getName() + " At X: " + getX() + " Y: " + getY();
	}

}
