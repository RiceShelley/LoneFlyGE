package game_engine_demo;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import lonefly.game.Updater;
import lonefly.game.engine.display.Activity;
import lonefly.game.engine.entity.GameObject;
import lonefly.game.engine.entity.Text;
import lonefly.game.engine.environment.Direction;
import lonefly.game.engine.input.LfGEKeyListener;

@SuppressWarnings("serial")
public class Week4Demo extends Activity implements Updater {

	// key listener
	private LfGEKeyListener key;
	// fps display
	private Text fps;
	// game object for player
	private GameObject player;
	// array of random words
	private String[] rand = { "Whale", "cat", "Screen", "Hat", "Fish" };

	public Week4Demo(LfGEKeyListener key) {
		// Initialize activity
		super("Week4Demo", new World(new Vector2(0f, 9.8f), true), 1920, 1080, false);
		// assign key listener to that for the key listener passed in
		this.key = key;
		// create platform
		super.createPlatform("ground", 700, 900, 1920, 400,
				"/home/rootie/SoftwareDev/eclipse workspace/LoneflyGEWeek4/src/demoRes/grass.jpg");
		// create game object. Assign the gameObject created to player to it can
		// be manipulated
		player = super.createGameObject("player", 2, 0, 50, 50,
				"/home/rootie/SoftwareDev/eclipse workspace/LoneflyGEWeek4/src/demoRes/box.jpg");
		player.setProp(1f, .5f, .2f);
		// create random word machine or in other words a game object
		super.createGameObject("rwm", 500, 200, 100, 20,
				"/home/rootie/SoftwareDev/eclipse workspace/LoneflyGEWeek4/src/demoRes/box.jpg");
		player.setProp(1f, .5f, .2f);
		// create fps display
		fps = super.createText("fps", 800, 50, 30, 30, "fps: ", Color.WHITE);
	}

	@Override
	public void update() {
		// update engine
		super.update();
		// loop through keys pressed
		for (int i = 0; i < key.getKeys().length; i++) {
			// player controls
			if (key.getKeys()[i] == KeyEvent.VK_H) {
				if (player.getBody().getLinearVelocity().x < 4) {
					player.push(Direction.WEST, 2f);
				}
			} else if (key.getKeys()[i] == KeyEvent.VK_L) {
				if (player.getBody().getLinearVelocity().x < 4) {
					player.push(Direction.EAST, 2f);
				}
			} else if (key.getKeys()[i] == KeyEvent.VK_SPACE) {
				if (player.getBody().getLinearVelocity().y < .01) {
					player.push(Direction.NORTH, 2);
				}
			}
		}
		// if player is touching rwm
		if (player.isTouching().equals("rwm")) {
			// remove previous word if any
			super.removeTextGraphic("text");
			// create a new random word
			super.createText("text", 500, 500, 30, 30, rand[(int) ((Math.random() * 10) % 5)], Color.WHITE);
			// set what player is touching to null so the player has to lose
			// contact with rwm before generating another random word
			player.setTouching("null");
		}
		// update frames per second count
		fps.setText("fps: " + (int) super.getFpsCounter().getFps());
		//sleep
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
