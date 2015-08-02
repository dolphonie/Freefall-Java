import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Player extends Group {
	public static final double CHAR_HEIGHT = 50;
	public static final double CHAR_WIDTH = 50;
	public static final double EYE_RADIUS = 10;
	public static final double EYE_Y = CHAR_HEIGHT/3;
	public static final double EYE_OFFSET = CHAR_WIDTH/4;
	
	private static final double MOVE_SPEED = 6;

	public enum Direction {
		LEFT, RIGHT, NONE
	}

	public Player(Color color) {
		Rectangle body = new Rectangle(CHAR_WIDTH, CHAR_HEIGHT, color);
		Circle leftEye = new Circle(CHAR_WIDTH/2-EYE_OFFSET,EYE_Y,EYE_RADIUS,Color.BLACK);
		Circle rightEye = new Circle(CHAR_WIDTH/2+EYE_OFFSET,EYE_Y,EYE_RADIUS,Color.BLACK);
		getChildren().addAll(body,leftEye,rightEye);
	}

	public void reset() {
		vx = 0;
		vy = 0;
		movDir = Direction.NONE;
		setTranslateY(Freefall.WINDOW_HEIGHT - CHAR_HEIGHT * 2);
		setTranslateX(Freefall.WINDOW_WIDTH / 2);
	}

	public void move() {
		if (getMoveDir() == Direction.LEFT) {
			if (vx >= -MOVE_SPEED * 2)
				adjustXVel(-MOVE_SPEED);
		} else if (getMoveDir() == Direction.RIGHT) {
			if (vx <= MOVE_SPEED * 2)
				adjustXVel(MOVE_SPEED);
		}
		if (vx < 0) {
			vx += MOVE_SPEED / 3;
		} else if (vx > 0) {
			vx -= MOVE_SPEED / 3;
		}
		if (vx <= .5 && vx >= -.5)
			vx = 0;
		setTranslateX(getTranslateX() + vx);
		setTranslateY(getTranslateY() - getYVel());


		if (getTranslateY() <= Freefall.WINDOW_HEIGHT - CHAR_HEIGHT) {
			adjustYVel(-1);
		}

	}

	public void jump() {
		setYVel(20);
	}

	public double getXVel() {
		return vx;
	}

	public double getYVel() {
		return vy;
	}

	public void adjustXVel(double increment) {
		vx += increment;
	}

	public void adjustYVel(double increment) {
		setYVel(getYVel() + increment);
	}

	public void setXVel(double vel) {
		vx = vel;
	}

	public void setYVel(double vel) {
		vy = vel;
	}

	public void setMovDir(Direction dir) {
		movDir = dir;
	}

	public Direction getMoveDir() {
		return movDir;
	}

	private Direction movDir;
	private double vx = 0;
	private double vy = 0;
}
