package game_engine_demo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import lonefly.game.ResourceManager;
import lonefly.game.Updater;
import lonefly.game.engine.display.Activity;
import lonefly.game.engine.entity.GameObject;
import lonefly.game.engine.environment.Direction;
import lonefly.game.engine.input.LfGEKeyListener;
import lonefly.game.engine.util.Collision;

public class Week4Demo extends Activity implements Updater, Collision {

	// key listener
	private LfGEKeyListener key;
	// game object for player
	private GameObject player;

	private ArrayList<pinWheel> pin;
	
	ParticleTrailEmitter PTE = new ParticleTrailEmitter(0, 0, super.resources());

	public Week4Demo(LfGEKeyListener key) {
		// Initialize activity
		super("Week4Demo", new ResourceManager(new World(new Vector2(0, 9.8f), true), 75, 1920 * 2, 1080 * 2), 1920,
				1080);
		// assign key listener to that for the key listener passed in
		this.key = key;
		// create platform
		super.resources().addPlatform("ground", 700, 900, 1920, 400,
				"C:/Users/Genesh911/Documents/softwareDev/eclipseWS/LoneflyGEWeek4/src/demoRes/grass.jpg");
		// create game object. Assign the gameObject created to player to it can
		// be manipulated
		player = super.resources().addGameObject("player", 2, 0, 50, 50,
				"C:/Users/Genesh911/Documents/softwareDev/eclipseWS/LoneflyGEWeek4/src/demoRes/box.jpg");
		player.setProp(1f, .5f, .2f);
		// create random word machine or in other words a game object
		super.resources().addGameObject("rwm", 500, 600, 100, 20,
				"C:/Users/Genesh911/Documents/softwareDev/eclipseWS/LoneflyGEWeek4/src/demoRes/box.jpg");
		player.setProp(1f, .5f, .2f);
		player.setIndex(90);
		/*
		 * pin = new ArrayList<pinWheel>(); int x = 100; int y = 300; for (int i
		 * = 0; i < 15; i++) { pin.add(new pinWheel(this, x, y)); x += 75; y +=
		 * 30; }
		 */
	}

	@Override
	public void update() {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		PTE.setX(player.getX());
		PTE.setY(player.getY());
		PTE.update();
		// update engine
		super.update();
		// loop through keys pressed
		for (int i = 0; i < key.getKeys().length; i++) {
			// player controls
			if (key.getKeys()[i] == KeyEvent.VK_H) {
				if (player.getBody().getLinearVelocity().x < 4) {
					player.push(Direction.WEST, 7f);
				}
			} else if (key.getKeys()[i] == KeyEvent.VK_L) {
				if (player.getBody().getLinearVelocity().x < 4) {
					player.push(Direction.EAST, 7f);
				}
			} else if (key.getKeys()[i] == KeyEvent.VK_SPACE) {
				if (player.getBody().getLinearVelocity().y < .01) {
					player.push(Direction.NORTH, 10f);
				}
			} else if (key.getKeys()[i] == KeyEvent.VK_S) {
				super.resources()
						.addGameObject("cube", 1000, 100, 25, 25,
								"C:/Users/Genesh911/Documents/softwareDev/eclipseWS/LoneflyGEWeek4/src/demoRes/cube.png")
						.setProp(1f, .2f, .5f);
			} else if (key.getKeys()[i] == KeyEvent.VK_Y) {
				// create random word machine or in other words a game object
				super.resources().addGameObject("rwm", 500, 600, 100, 20,
						"C:/Users/Genesh911/Documents/softwareDev/eclipseWS/LoneflyGEWeek4/src/demoRes/box.jpg");
			} 
		}
		/*
		 * for (int i = 0; i < pin.size(); i++) {
		 * pin.get(i).getP().setAngle(pin.get(i).getP().getAngle() - ((float)
		 * Math.random() / 10)); }
		 */
	}

}
