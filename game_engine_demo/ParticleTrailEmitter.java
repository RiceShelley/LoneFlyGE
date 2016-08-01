package game_engine_demo;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import lonefly.game.ResourceManager;
import lonefly.game.Updater;
import lonefly.game.engine.display.GraphicLoader;
import lonefly.game.engine.effects.Particle;
import lonefly.game.engine.environment.Point;

public class ParticleTrailEmitter implements Updater {

	private float x;
	private float y;
	private ResourceManager rMan;
	private ArrayList<Particle> part = new ArrayList<Particle>();

	public ParticleTrailEmitter(float x, float y, ResourceManager rMan) {
		this.x = x;
		this.y = y;
		this.rMan = rMan;
		new Thread(new emitt()).start();
	}

	private class emitt implements Runnable {
		@Override
		public void run() {
			int offset = 0;
			BufferedImage texture = new GraphicLoader().loadImage(
					"C:/Users/Genesh911/Documents/softwareDev/eclipseWS/LoneflyGEWeek4/src/demoRes/particle.png");
			while (true) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				offset = (int) (Math.random() * 100.0);
				for (int i = 0; i < 360; i++) {
					if (i % 5 == 0) {
						float speed = (float) (Math.random() * 2);
						Particle p = new Particle(new Point(20, 20), .3f, x, y, speed, i + offset, 1200, texture);
						p.setIndex(0);
						part.add(p);
					}
				}
			}
		}
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void update() {
		rMan.addParticleArray(part);
		part.clear();
	}
}
