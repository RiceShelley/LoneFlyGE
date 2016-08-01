package game_engine_demo;

import lonefly.game.engine.display.Activity;
import lonefly.game.engine.entity.Platform;
import lonefly.game.engine.environment.Direction;
import lonefly.game.engine.environment.Point;

public class pinWheel {

	private Activity act;

	int x;
	int y;
	private Platform main;
	private Platform blade3;

	public pinWheel(Activity act, int x, int y) {
		this.x = x;
		this.y = y;
		this.act = act;
		main = act.resources().addPlatform("blade1", x, y, 10, 100,
				"C:/Users/Genesh911/Documents/softwareDev/eclipseWS/LoneflyGEWeek4/src/demoRes/box.jpg");
		main.setProp(1f, .3f, .3f);
		blade3 = act.resources().addPlatform("blade3", x - 50, y, 100, 10,
				"C:/Users/Genesh911/Documents/softwareDev/eclipseWS/LoneflyGEWeek4/src/demoRes/box.jpg");
		blade3.setProp(1f, .3f, .3f);
		blade3.bind(main, new Point(0, 0));
	}

	public Platform getP() {
		return main;
	}

}
