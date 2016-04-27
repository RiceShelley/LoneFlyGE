package lonefly.game.engine.entity;

import java.awt.image.BufferedImage;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import lonefly.game.LoneflyGE;
import lonefly.game.engine.display.HasGraphic;
import lonefly.game.engine.environment.Direction;
import lonefly.game.engine.util.ILevels;

/*
 * Author: Rice Shelley
 * Version: 3/11/2016
 * Purpose: creating graphical tangible objects with collisions,
 * positions, and depth.
 */

public class GameObject extends Entity implements HasGraphic, Destroy {

	// GameObject graphic
	private BufferedImage graphic;

	public GameObject(String name, float x, float y, float width, float height, World world, String gPath) {
		super(name, LoneflyGE.mtp(x), LoneflyGE.mtp(y), width, height, world, BodyType.DynamicBody);
		if (LoneflyGE.GRAPHIC_LOADER.loadImage(gPath) != null)
			graphic = LoneflyGE.GRAPHIC_LOADER.loadImage(gPath);
		LoneflyGE.LOG.println("Object " + name + " created ", ILevels.SMALLTALK);
	}

	@Override
	public BufferedImage getGraphic() {
		return graphic;
	}

	@Override
	public void setGraphic(BufferedImage img) {
		graphic = img;
	}

	/*
	 * Applies a force in a direction - thx box2D
	 */
	public void push(Direction dir, float force) {
		switch (dir) {
		case NORTH:
			super.getBody().applyForceToCenter(0, force * -1f, true);
			break;
		case EAST:
			super.getBody().applyForceToCenter(force, 0, true);
			break;
		case SOUTH:
			super.getBody().applyForceToCenter(0, force, true);
			break;
		case WEST:
			super.getBody().applyForceToCenter(force * -1f, 0, true);
			break;
		case NULL:
			break;
		}

	}

	@Override
	public String toString() {
		return "Game Object: " + getName() + " At X: " + getX() + " Y: " + getY();
	}

	// destroy game object
	@Override
	public void destroy() {
		Array<Fixture> fix = getBody().getFixtureList();
		for (Fixture f : fix) {
			getBody().destroyFixture(f);
		}
	}
}
