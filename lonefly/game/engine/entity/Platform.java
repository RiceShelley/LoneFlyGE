package lonefly.game.engine.entity;

import java.awt.image.BufferedImage;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import lonefly.game.LoneflyGE;
import lonefly.game.engine.display.HasGraphic;
import lonefly.game.engine.util.ILevels;

/*
 * Author: Rice Shelley
 * Version: 3/17/2016
 * Purpose: "platforms" are entity's that are designed for Game
 * Objects to sit on top of
 */

public class Platform extends Entity implements HasGraphic, Destroy {

	// graphic
	private BufferedImage graphic;

	public Platform(String name, float x, float y, float width, float height, World world, String gPath) {
		super(name, LoneflyGE.mtp(x), LoneflyGE.mtp(y), width, height, world, BodyType.KinematicBody);
		graphic = LoneflyGE.GRAPHIC_LOADER.loadImage(gPath);
		LoneflyGE.LOG.println("New platform created! name <" + getName() + ">", ILevels.SMALLTALK);
	}

	// destroy platform
	@Override
	public void destroy() {
		Array<Fixture> fix = getBody().getFixtureList();
		for (Fixture f : fix) {
			getBody().destroyFixture(f);
		}
	}

	@Override
	public BufferedImage getGraphic() {
		return graphic;
	}
	
	@Override
	public void setGraphic(BufferedImage img) {
		graphic = img;
	}

	@Override
	public String toString() {
		return "Game Object: " + getName() + " At X: " + getX() + " Y: " + getY();
	}

}
