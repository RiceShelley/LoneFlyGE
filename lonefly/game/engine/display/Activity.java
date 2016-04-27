package lonefly.game.engine.display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JPanel;

import com.badlogic.gdx.physics.box2d.World;

import lonefly.game.FramesPerSecond;
import lonefly.game.LoneflyGE;
import lonefly.game.Updater;
import lonefly.game.engine.entity.Entity;
import lonefly.game.engine.entity.GameObject;
import lonefly.game.engine.entity.Graphic;
import lonefly.game.engine.entity.Platform;
import lonefly.game.engine.entity.Text;
import lonefly.game.engine.environment.Camera;
import lonefly.game.engine.environment.CollisionsListener;
import lonefly.game.engine.util.ILevels;

/*
 * Author: Rice Shelley
 * Version: 3/14/2016
 * Purpose: Handle displays and uploading graphics to them 
 */

public class Activity extends JPanel implements Updater {
	private static final long serialVersionUID = 1L;
	// name given to activity
	private String name;
	// buffered image that contains all the activities graphics
	private BufferedImage display;
	// width / height of display
	private int renderW;
	private int renderH;
	// frames per second calculator
	private FramesPerSecond fps;
	// let engine regulate sleep intervals / performance
	private boolean regPerformance;
	// game camera position of view point
	private Camera cam;
	/*
	 * all game object this Activity handles. Stored in data structure bellow.
	 * game object = Dynamic bodies that are effected by gravity, collide with
	 * platforms and other game objects.
	 */
	private ArrayList<GameObject> gObjects;
	/*
	 * all game graphics the Activity handles. Stored in data structure bellow.
	 * gGraphic = graphics displayed in buffered image display are not effected
	 * by gravity, and do not collide with other objects
	 */
	private ArrayList<Graphic> gGraphics;
	/*
	 * all platform objects the Activity handles. Stored in data structure
	 * bellow. platform = Kinematic bodes, not effected by gravity. Collide with
	 * other platforms, and game objects.
	 */
	private ArrayList<Platform> platforms;
	/*
	 * all frame graphics the Activity handles. Stored in data structure bellow.
	 * fGraphics = frame graphic used to display graphics directly to the
	 * Activity JPanel best used for UI such as maps and borders
	 */
	private ArrayList<Graphic> fGraphics;
	/*
	 * All text graphics stored in data structure bellow
	 */
	private ArrayList<Text> textG;
	// Box2D world <- yea for fancy physics
	private World world;

	/*
	 * Activity constructor
	 * 
	 * @param - name = name of activity. World = Box2D world used for handling
	 * physics. renderW = width of private instance buffered image "display".
	 * renderH = height of private instance buffered image "display".
	 * regPerformance = true - let engine regulate its own performance / false -
	 * engine will not regulate its own performance
	 */
	public Activity(String name, World world, int renderW, int renderH, boolean regPerformance) {
		this.world = world;
		this.name = name;
		this.renderW = renderW;
		this.renderH = renderH;
		this.regPerformance = regPerformance;
		// camera can be adjusted by using getCamera()
		this.cam = new Camera();
		// Initialize display to size defined by renderW and renderH
		display = new BufferedImage(renderW, renderH, BufferedImage.TYPE_INT_RGB);
		/*
		 * create collision listener - when collisions are made this object
		 * calls to gameCollision() <- method in this class. gameCollision() can
		 * be overridden by user to create events on certain collisions
		 */
		new CollisionsListener(this);
		/*
		 * initialize game object data structures <- more info on what these are
		 * at there deceleration
		 */
		gGraphics = new ArrayList<Graphic>();
		gObjects = new ArrayList<GameObject>();
		platforms = new ArrayList<Platform>();
		fGraphics = new ArrayList<Graphic>();
		textG = new ArrayList<Text>();
		// initiate frames per second calculator
		fps = new FramesPerSecond();
		new Timer().scheduleAtFixedRate(fps, 0, 1000);
		// set JPanel background to black JPanel(super class of activity)
		setBackground(Color.BLACK);
		// notify log that activity has been created
		LoneflyGE.LOG.println("new Activity created!", ILevels.SMALLTALK);
	}

	// create game object and add to the activity
	/*
	 * @param name = game object name (used for distinguishing it from other
	 * game objects) x, y = x and y coordinate values <-- in terms of the
	 * physics engine NOT PIXELS!!! width, height = width and height of game
	 * object IN PIXELS! resource path = path of image that can be used to
	 * represent a game object if null game object is represented with a blue
	 * rectangle
	 */
	public GameObject createGameObject(String name, float x, float y, float width, float height, String resourcePath) {
		// create game object
		GameObject gO = new GameObject(name, x, y, width, height, world, resourcePath);
		// add to activities game object data structure
		gObjects.add(gO);
		// log game object creation
		LoneflyGE.LOG.println("Game Object " + gO + " added to " + this, ILevels.SMALLTALK);
		// return game object so user may have
		return gO;
	}

	/*
	 * create graphics (given the file path) and add to the activity @param
	 * refer to previous @param with the exception of if resource path is null
	 * graphic is represented with a yellow rectangle
	 */
	public Graphic createGameGraphic(String name, float x, float y, float width, float height, String resourcePath) {
		// create new graphic
		Graphic g = new Graphic(name, x, y, width, height, resourcePath);
		// add to game graphics data structure
		gGraphics.add(g);
		// log graphic creation
		LoneflyGE.LOG.println("Game graphic: " + g + " added to " + this, ILevels.SMALLTALK);
		// return newly created graphic object
		return g;
	}

	// create graphics (given a buffered image)
	public Graphic createGameGraphic(String name, float x, float y, float width, float height, BufferedImage graphic) {
		// create graphic
		Graphic g = new Graphic(name, x, y, width, height, graphic);
		// add to game graphics data structure
		gGraphics.add(g);
		// log graphic creation
		LoneflyGE.LOG.println("Game graphic: " + g + " added to " + this, ILevels.SMALLTALK);
		// return graphic
		return g;
	}

	/*
	 * create graphics (given the file path) and add to the activity @param
	 * refer to createGameObject @param with the exception of if resource path
	 * is null graphic is represented with a yellow rectangle
	 */
	public Graphic createFrameGraphic(String name, float x, float y, float width, float height, String resourcePath) {
		// create new graphic
		Graphic g = new Graphic(name, x, y, width, height, resourcePath);
		// add to frame graphics data structure
		fGraphics.add(g);
		// log graphic creation
		LoneflyGE.LOG.println("Frame graphic: " + g + " added to " + this, ILevels.SMALLTALK);
		// return newly created graphic object
		return g;
	}

	// create graphics (given a buffered image)
	public Graphic createFrameGraphic(String name, float x, float y, float width, float height, BufferedImage graphic) {
		// create graphic
		Graphic g = new Graphic(name, x, y, width, height, graphic);
		// add to game graphics data structure
		fGraphics.add(g);
		// log graphic creation
		LoneflyGE.LOG.println("Frame graphic: " + g + " added to " + this, ILevels.SMALLTALK);
		// return graphic
		return g;
	}

	// create platform
	/*
	 * @param name = platform name (used for distinguishing it from other
	 * platforms) x, y = x and y coordinate values <-- in terms of the physics
	 * engine NOT PIXELS!!! width, height = width and height of game object IN
	 * PIXELS! resource path = path of image that can be used to represent a
	 * platform if null platform is represented with a green rectangle
	 */
	public Platform createPlatform(String name, float x, float y, float width, float height, String resourcePath) {
		// create new platform
		Platform p = new Platform(name, x, y, width, height, world, resourcePath);
		// add to platform data structure
		platforms.add(p);
		// log platform creation
		LoneflyGE.LOG.println("Platform: " + p + " added to " + this, ILevels.SMALLTALK);
		// return newly created platform
		return p;
	}

	/*
	 * add text graphic to activity name = name used to identify text object x,
	 * y, width, height = you should know what these values are for text = text
	 * that will be displayed by gText object color = color of gText
	 */
	public Text createText(String name, float x, float y, float width, float height, String text, Color color) {
		// create new text object
		Text gText = new Text(name, x, y, width, height, text, color);
		// add text object to gTextObjects
		textG.add(gText);
		// log text creation
		LoneflyGE.LOG.println("Text: " + gText + " added to " + this, ILevels.SMALLTALK);
		// return text object
		return gText;
	}

	// get display <- display being the main container for everything being
	// rendered (except fGraphics)
	public BufferedImage getDisplay() {
		return display;
	}

	// method of JPanel overridden to upload graphics to panel
	/*
	 * note if graphic not found for a game entity then it will be shown as a
	 * color coordinated cubes - Color key: Blue = game object / yellow =
	 * graphic / green = platform
	 */
	@Override
	public void paintComponent(Graphics g) {
		// create new display
		display = new BufferedImage(renderW, renderH, BufferedImage.TYPE_INT_RGB);
		/*
		 * disG2D is now a reference to buffered image display's Graphic2D
		 * object so if we draw to disG2D since its a reference to displays
		 * graphics2D object we are drawing to the display buffered image
		 */
		Graphics2D disG2D = display.createGraphics();
		// clear JPanel
		g.clearRect(0, 0, getWidth(), getHeight());
		// draw background black
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		// draw game graphics
		disG2D.setColor(Color.YELLOW);
		for (int i = 0; i < gGraphics.size(); i++) {
			Graphic gG = gGraphics.get(i);
			if (gG.getGraphic() != null) {
				// if game graphic is at angle
				if (gG.getAngle() != 0.0) {
					// rotate graphic
					BufferedImage rotG = LoneflyGE.GEDIT.rotateGraphic(gG.getGraphic(), gG.getWidth(), gG.getHeight(),
							gG.getAngle());
					// render rotated graphic
					disG2D.drawImage(rotG, (int) (gG.getX() - (rotG.getWidth() / 2)),
							(int) (gG.getY() - (rotG.getHeight() / 2)), rotG.getWidth(), rotG.getHeight(), null);
				} else
					disG2D.drawImage(gG.getGraphic(), (int) (gG.getX() - (gG.getWidth() / 2)),
							(int) (gG.getY() - (gG.getHeight() / 2)), (int) gG.getWidth(), (int) gG.getHeight(), null);

			} else
				// draw non rotated graphic
				disG2D.fillRect((int) (gG.getX() - (gG.getWidth() / 2)), (int) (gG.getY() - (gG.getHeight() / 2)),
						(int) gG.getWidth(), (int) gG.getHeight());
		}
		// draw platforms
		disG2D.setColor(Color.GREEN);
		for (int i = 0; i < platforms.size(); i++) {
			Platform p = platforms.get(i);
			if (p.getGraphic() != null) {
				// if platform is at angle
				if (p.getBody().getAngle() != 0.0) {
					// get rotated graphic
					BufferedImage rotG = LoneflyGE.GEDIT.rotateGraphic(p.getGraphic(), p.getWidth(), p.getHeight(),
							p.getBody().getAngle());
					// draw rotated graphic
					disG2D.drawImage(rotG, (int) (p.getX() - (rotG.getWidth() / 2)),
							(int) (p.getY() - (rotG.getHeight() / 2)), rotG.getWidth(), rotG.getHeight(), null);
				} else {
					// draw non rotated graphic
					disG2D.drawImage(p.getGraphic(), (int) (p.getX() - (p.getWidth() / 2)),
							(int) (p.getY() - (p.getHeight() / 2)), (int) p.getWidth(), (int) p.getHeight(), null);
				}
			} else {
				// if no graphic available draw rectangle in its place NOTE:
				// these rectangle fillers will not rotate
				disG2D.fillRect((int) (p.getX() - (p.getWidth() / 2)), (int) (p.getY() - (p.getHeight() / 2)),
						(int) p.getWidth(), (int) p.getHeight());
			}
		}
		// draw game objects
		disG2D.setColor(Color.BLUE);
		for (int i = 0; i < gObjects.size(); i++) {
			GameObject gO = gObjects.get(i);
			if (gO.getGraphic() != null) {
				// if game object is at angle
				if (gO.getBody().getAngle() != 0.0) {
					// rotate graphic
					BufferedImage rotG = LoneflyGE.GEDIT.rotateGraphic(gO.getGraphic(), gO.getWidth(), gO.getHeight(),
							gO.getBody().getAngle());
					// draw rotated graphic
					disG2D.drawImage(rotG, (int) (gO.getX() - (rotG.getWidth() / 2)),
							(int) (gO.getY() - (rotG.getHeight() / 2)), rotG.getWidth(), rotG.getHeight(), null);
				} else {
					// draw non rotated graphic
					disG2D.drawImage(gO.getGraphic(), (int) (gO.getX() - (gO.getWidth() / 2)),
							(int) (gO.getY() - (gO.getHeight() / 2)), (int) gO.getWidth(), (int) gO.getHeight(), null);
				}
			} else {
				// if no graphic available draw rectangle in its place NOTE:
				// these rectangle fillers will not rotate
				disG2D.fillRect((int) (gO.getX() - (gO.getWidth() / 2)), (int) (gO.getY() - (gO.getHeight() / 2)),
						(int) gO.getWidth(), (int) gO.getHeight());
			}

		}
		// draw graphical text
		for (int i = 0; i < textG.size(); i++) {
			// get text from text data structure
			Text tG = textG.get(i);
			// set color of text
			disG2D.setColor(tG.getColor());
			// define font
			disG2D.setFont(new Font(null, (int) tG.getWidth(), (int) tG.getHeight()));
			// create buffered image container for text <- makes text easer to
			// rotate if need be
			BufferedImage tContainer = new BufferedImage(disG2D.getFontMetrics().stringWidth(tG.getText()),
					disG2D.getFontMetrics().getHeight(), BufferedImage.TYPE_INT_ARGB);
			// get graphics
			Graphics2D tCG = tContainer.createGraphics();
			// make background transparent
			tCG.setComposite(AlphaComposite.Clear);
			tCG.fillRect(0, 0, tContainer.getWidth(), tContainer.getHeight());
			// set buffered image back to non transparency
			tCG.setComposite(AlphaComposite.Src);
			// set font according to that of disG2D
			tCG.setFont(disG2D.getFont());
			// set color according to that of disG2D
			tCG.setColor(disG2D.getColor());
			// draw text
			tCG.drawString(tG.getText(), 0, (int) (tContainer.getHeight() / 1.35));
			// render
			// if at angle
			if (tG.getAngle() != 0.0) {
				// rotate graphic
				BufferedImage rotG = LoneflyGE.GEDIT.rotateGraphic(tContainer, tContainer.getWidth(),
						tContainer.getHeight(), tG.getAngle());
				// draw rotated graphic
				disG2D.drawImage(rotG, (int) tG.getX() - (rotG.getWidth() / 2),
						(int) tG.getY() - (rotG.getHeight() / 2), rotG.getWidth(), rotG.getHeight(), null);
			} else {
				// if graphic not rotated
				disG2D.drawImage(tContainer, (int) tG.getX() - (tContainer.getWidth() / 2),
						(int) tG.getY() - (tContainer.getHeight() / 2), tContainer.getWidth(), tContainer.getHeight(),
						null);
			}
		}
		// render display <- display being a container for all the graphics
		// above
		g.drawImage(display, (int) (cam.getX() * -1), (int) (cam.getY() * -1), null);
		// draw frame graphics directly to JPanel
		for (Graphic gG : fGraphics) {
			if (gG.getGraphic() != null)
				// if frame graphic is at angle
				if (gG.getAngle() != 0.0) {
					// rotate graphic
					BufferedImage rotG = LoneflyGE.GEDIT.rotateGraphic(gG.getGraphic(), gG.getWidth(), gG.getHeight(),
							gG.getAngle());
					// draw rotated graphic
					g.drawImage(rotG, (int) (gG.getX() - (rotG.getWidth() / 2)),
							(int) (gG.getY() - (rotG.getHeight() / 2)), rotG.getWidth(), rotG.getHeight(), null);

				} else {
					g.drawImage(gG.getGraphic(), (int) (gG.getX() - (gG.getWidth() / 2)),
							(int) (gG.getY() - (gG.getHeight() / 2)), (int) gG.getWidth(), (int) gG.getHeight(), null);
				}
			else {
				g.fillRect((int) (gG.getX() - (gG.getWidth() / 2)), (int) (gG.getY() - (gG.getHeight() / 2)),
						(int) gG.getWidth(), (int) gG.getHeight());
			}

		}
	}

	/*
	 * called by CollisionListener every time a game object or platform have
	 * collisions method is intended to be overridden by user
	 */
	public void gameCollision(Entity a, Entity b) {
		LoneflyGE.LOG.println(a + " Collided with " + b, ILevels.SMALLTALK);
	}

	// update Activity
	@Override
	public void update() {
		// update Box2D
		world.step(1 / 45f, 6, 2);
		// run garbage Collect
		garbageCollect();
		// repaint activitie's JPanel
		repaint();
		// if regulate performance is true attempt to regulate performance based
		// on frame rate <- this will be rewritten in a future update to work a
		// little bit better
		if (regPerformance) {
			if (fps.getFps() > fps.getTarget()) {
				LoneflyGE.SLEEP_TIME += .01;
			} else {
				LoneflyGE.SLEEP_TIME -= .01;
			}
			try {
				if (LoneflyGE.SLEEP_TIME > 0) {
					Thread.sleep((long)LoneflyGE.SLEEP_TIME);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(LoneflyGE.SLEEP_TIME);
		}
		fps.tick();
	}

	// regulate Performance
	// garbage collect if a entity goes out of world delete it
	public void garbageCollect() {
		ArrayList<GameObject> gOMarked = new ArrayList<GameObject>();
		for (GameObject gO : gObjects) {
			if (gO.getX() > (renderW * 2) || gO.getX() < (renderW * -1) || gO.getY() > (renderH * 2)
					|| gO.getY() < (renderH * -1)) {
				// mark for deletion
				gOMarked.add(gO);
			}
		}
		// remove game objects marked for removal
		for (GameObject gO : gOMarked) {
			if (removeGameObject(gO)) {
				// log removal
				LoneflyGE.LOG.println(this + gO.toString() + " was deleted by garbage collect!!!", ILevels.MSG);
			} else {
				// log failed deletion
				LoneflyGE.LOG.println(this + " garbage collect failed to delete <" + gO.toString() + ">",
						ILevels.ERROR);
			}
		}
		ArrayList<Platform> platMarked = new ArrayList<Platform>();
		// check platforms
		for (Platform p : platforms) {
			if (p.getX() > (renderW * 2) || p.getX() < (renderW * -1) || p.getY() > (renderH * 2)
					|| p.getY() < (renderH * -1)) {
				// mark for deletion
				platMarked.add(p);
			}
		}
		// remove platforms marked for removal
		for (Platform p : platMarked) {
			if (removePlatform(p)) {
				// log removal
				LoneflyGE.LOG.println(this + platMarked.toString() + " was deleted by garbage collect!!!", ILevels.MSG);
			} else {
				// log failed deletion
				LoneflyGE.LOG.println(this + " garbage collect failed to delete <" + platMarked.toString() + ">",
						ILevels.ERROR);
			}
		}
	}

	// kind of self explanatory
	@Override
	public String toString() {
		return "Activity " + name + " ";
	}

	/*
	 * methods bellow for adding entity objects directly to their corresponding
	 * data structures
	 */

	public void addGameObject(GameObject gO) {
		gObjects.add(gO);
	}

	public void addPlatform(Platform p) {
		platforms.add(p);
	}

	public void addGameGraphic(Graphic g) {
		gGraphics.add(g);
	}

	public void addFrameGraphic(Graphic g) {
		fGraphics.add(g);
	}

	public void addTextGraphic(Text t) {
		textG.add(t);
	}
	/*
	 * methods bellow for removing entities of activity entities can be removed
	 * by name or corresponding object
	 */

	public boolean removeGameObject(String name) {
		for (int i = 0; i < gObjects.size(); i++) {
			if (gObjects.get(i).getName().equals(name)) {
				gObjects.get(i).destroy();
				gObjects.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removeGameObject(GameObject gO) {
		for (int i = 0; i < gObjects.size(); i++) {
			if (gObjects.get(i).equals(gO)) {
				gObjects.get(i).destroy();
				gObjects.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removePlatform(String name) {
		for (int i = 0; i < platforms.size(); i++) {
			if (platforms.get(i).getName().equals(name)) {
				platforms.get(i).destroy();
				platforms.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removePlatform(Platform p) {
		for (int i = 0; i < platforms.size(); i++) {
			if (platforms.get(i).equals(p)) {
				platforms.get(i).destroy();
				platforms.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removeGameGraphic(String name) {
		for (int i = 0; i < gGraphics.size(); i++) {
			if (gGraphics.get(i).getName().equals(name)) {
				gGraphics.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removeGameGraphic(Graphic g) {
		for (int i = 0; i < gGraphics.size(); i++) {
			if (gGraphics.get(i).equals(g)) {
				gGraphics.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removeFrameGraphic(String name) {
		for (int i = 0; i < fGraphics.size(); i++) {
			if (fGraphics.get(i).getName().equals(name)) {
				fGraphics.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removeFrameGraphic(Graphic g) {
		for (int i = 0; i < fGraphics.size(); i++) {
			if (fGraphics.get(i).equals(g)) {
				fGraphics.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removeTextGraphic(String name) {
		for (int i = 0; i < textG.size(); i++) {
			if (textG.get(i).getName().equals(name)) {
				textG.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removeTextGraphic(Text t) {
		for (int i = 0; i < textG.size(); i++) {
			if (textG.get(i).equals(t)) {
				textG.remove(i);
				return true;
			}
		}
		return false;
	}

	// getters
	public World getWorld() {
		return world;
	}

	public ArrayList<GameObject> getGameObjects() {
		return gObjects;
	}

	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}

	public ArrayList<Graphic> getActivityGameGraphics() {
		return gGraphics;
	}

	public ArrayList<Graphic> getActivityFrameGraphics() {
		return fGraphics;
	}

	public ArrayList<Text> getTextGraphics() {
		return textG;
	}

	public Camera getCamera() {
		return cam;
	}

	public FramesPerSecond getFpsCounter() {
		return fps;
	}
}
