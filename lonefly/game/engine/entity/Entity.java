package lonefly.game.engine.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import lonefly.game.LoneflyGE;
import lonefly.game.engine.environment.Coordinate;
import lonefly.game.engine.environment.Direction;
import lonefly.game.engine.environment.Point;
import lonefly.game.engine.util.ILevels;

/*
 * Author: Rice Shelley
 * Version: 3/11/2016
 * Purpose: defines what it means 
 * to be a "thing" in a 2d enviorment 
 * IMPORTANT: -> if Entity is a Box2D body then its x, y is in terms of meters NOT PIXELS and is 
 * scaled before rendering by a predefined scale factor in LoneflyGE
 */

public abstract class Entity implements Coordinate, Comparable<String> {

	// name of Entity could be anything (ex. Jerry, bob, bill)
	private String name;
	// properties
	private float height;
	private float width;
	private float x;
	private float y;
	private float angle;
	// Box2D body
	private Body body;
	// primary box2D fixture
	private FixtureDef mainFix;
	// if this entity is colliding with another, the other's name is stored in
	// the
	// touching variable bellow
	private String touching = null;
	// entity this entity is bound to if any otherwise null
	private Entity bound = null;

	public Entity(String name, float x, float y, float width, float height, World world, BodyType bodyType) {
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.name = name;
		// if entity is supposed to be a box2D entity such as a platform or game
		// object
		if (world != null && bodyType != null) {
			// create body definition
			BodyDef gOBodyDef = new BodyDef();
			gOBodyDef.position.set(x, y);
			gOBodyDef.type = bodyType;
			// make entity a rectangle of some type
			PolygonShape box = new PolygonShape();
			box.setAsBox((this.width / LoneflyGE.PHYSICS_SCALE) / 2f, (this.height / LoneflyGE.PHYSICS_SCALE) / 2f);
			// add body to world. store body in class private instance variable
			// "body"
			this.body = world.createBody(gOBodyDef);
			// create fixture for body
			mainFix = new FixtureDef();
			mainFix.shape = box;
			body.createFixture(mainFix);
		} else {
			body = null;
		}
	}

	/*
	 * move objects given a direction and amount note direction is based of
	 * Direction enum
	 */
	public void move(Direction dir, float amt) {
		if (body != null) {
			switch (dir) {
			case NORTH:
				body.setTransform(body.getPosition().x, body.getPosition().y - amt, 0f);
				break;
			case EAST:
				body.setTransform(body.getPosition().x + amt, body.getPosition().y, 0f);
				break;
			case SOUTH:
				body.setTransform(body.getPosition().x, body.getPosition().y + amt, 0f);
				break;
			case WEST:
				body.setTransform(body.getPosition().x - amt, body.getPosition().y, 0f);
				break;
			case NULL:
				break;
			}
		} else {
			switch (dir) {
			case NORTH:
				y -= amt;
				break;
			case EAST:
				x += amt;
				break;
			case SOUTH:
				y += amt;
				break;
			case WEST:
				x -= amt;
				break;
			case NULL:
				break;
			}
		}
	}

	/*
	 * bind entity's coordinates to another entity param -> Entity that this
	 * entity will be bound to and a point object to define the off set of the
	 * bind
	 */
	public void bind(Entity e, Point p) {
		// if un bound
		if (bound == null) {
			// assign entity e to bound
			bound = e;
			// create new thread
			new Thread(new Runnable() {
				@Override
				public void run() {
					// while still bound
					while (bound != null) {
						// Synchronize positions of entities + off set
						x = bound.getX() + p.getX();
						y = bound.getY() + p.getY();
						// give cpu a lil rest
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		} else
			// if entity is already bound
			LoneflyGE.LOG.err(
					this + " Alread bound to " + bound + " cannout bind to " + e + "call unBind() to unbind " + bound);
	}

	// unbind entity
	public void unBind() {
		bound = null;
	}

	// comparing game objects to other game objects given the "other" game
	// objects name
	@Override
	public int compareTo(String name) {
		if (getName().equals(name)) {
			return 0;
		} else if (getName().length() > name.length()) {
			return 1;
		} else {
			return -1;
		}
	}

	// getters

	public Body getBody() {
		return body;
	}

	// set body's properties
	public void setProp(float density, float friction, float restitution) {
		if (body != null) {
			body.destroyFixture(body.getFixtureList().get(0));
			mainFix.density = density;
			mainFix.friction = friction;
			mainFix.restitution = restitution;
			body.createFixture(mainFix);
		} else {
			LoneflyGE.LOG.println("Can not set properties of <" + this + "> object must have a body to set properties!",
					ILevels.WARNING);
		}
	}

	@Override
	public float getX() {
		if (body != null)
			return body.getPosition().x * LoneflyGE.PHYSICS_SCALE;
		else
			return x;
	}

	@Override
	public float getY() {
		if (body != null)
			return body.getPosition().y * LoneflyGE.PHYSICS_SCALE;
		else
			return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public String isTouching() {
		if (touching == null) {
			return "null";
		} else {
			return touching;
		}
	}

	public float getAngle() {
		if (body != null) {
			return body.getAngle();
		} else {
			return angle;
		}
	}

	// setters
	@Override
	public void setX(float x) {
		if (body != null)
			body.setTransform(new Vector2(x, body.getPosition().y), 0f);
		else
			this.x = x;
	}

	@Override
	public void setY(float y) {
		if (body != null)
			body.setTransform(new Vector2(body.getPosition().x, y), 0f);
		else
			this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTouching(String touching) {
		this.touching = touching;
	}

	public void setAngle(float angle) {
		if (body != null) {
			body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y), angle);
		} else {
			this.angle = angle;
		}
	}
}
