import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Map extends Group {
	private static final int PLAT_HEIGHT = 25;
	private static final int PLAT_MIN_WIDTH = 50;
	private static final int PLAT_MAX_WIDTH = 300;
	private static final int PLAT_Y_GEN = (int) (Freefall.WINDOW_HEIGHT - PLAT_HEIGHT);
	private static final double MOVE_SPEED = 8;

	public Map() {
		rgen = new Random();
	}

	public Node hitsMap(Point2D colPt) {
		double x = colPt.getX();
		double y = colPt.getY();
		for (Node value : getChildren()) {
			double left = value.getTranslateX();
			double right = left + ((Rectangle) value).getWidth();
			double bottom = value.getTranslateY();
			double top = bottom + PLAT_HEIGHT;
			if (x > left && x < right && y > bottom && y < top)
				return value;
		}
		return null;
	}

	public void clear() {
		getChildren().clear();
	}

	public void genPlatform() {
		Rectangle plat = new Rectangle(rgen.nextInt(PLAT_MAX_WIDTH - PLAT_MIN_WIDTH) + PLAT_MIN_WIDTH, PLAT_HEIGHT,
				Color.WHITE);
		plat.setTranslateX(rgen.nextInt((int) Freefall.WINDOW_WIDTH));
		plat.setTranslateY(PLAT_Y_GEN);
		plat.setFill(Color.WHITE);
		getChildren().add(plat);
	}

	public void move() {
		for (Node platform : getChildren()) {
			if (platform.getTranslateY() + MOVE_SPEED < 0) {
				getChildren().remove(platform);
				return;
			}
			platform.setTranslateY(platform.getTranslateY() - MOVE_SPEED);
		}
	}

	private Random rgen;
}
