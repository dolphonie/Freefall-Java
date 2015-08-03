import java.util.Random;

import javax.print.DocFlavor.URL;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Freefall extends Application {
	public static final double WINDOW_HEIGHT = 1000;
	public static final double WINDOW_WIDTH = 1000;
	private static final boolean PRINT_DATA = false;
	private static final double STARTING_X = WINDOW_WIDTH / 2;
	private static final double STARTING_Y = Player.CHAR_HEIGHT * 2;
	public static final double TEXT_Y = 50;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		rgen = new Random();
		primaryStage.setTitle("Freefall (Only $9999.99)");
		Group root = new Group();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		scene.setFill(Color.BLACK);
		map = new Map();
		player = new Player(Color.WHITE);
		player.setTranslateY(STARTING_Y);
		player.setTranslateX(STARTING_X);
		distanceMeter = new Text(0, 20, "Distance: ");
		distanceMeter.setFont(new Font(24));
		distanceMeter.setFill(Color.WHITE);
		updateDistanceMeter();
		root.getChildren().addAll(player, map, distanceMeter);
		primaryStage.setScene(scene);
		EventHandler<KeyEvent> keyPressHandler = (new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				handleKeyPressEvent(ke);
			}
		});
		EventHandler<KeyEvent> keyReleaseHandler = (new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				handleKeyReleaseEvent(ke);
			}
		});
		scene.setOnKeyPressed(keyPressHandler);
		scene.setOnKeyReleased(keyReleaseHandler);
		primaryStage.show();
		java.net.URL resource = getClass().getResource("music.mp3");
	    Media media = new Media(resource.toString());
	    music = new MediaPlayer(media);
	    music.setVolume(1);
	    music.setCycleCount(MediaPlayer.INDEFINITE);
	    music.play();
		animate();
	}

	private void animate() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				player.move();
				if (rgen.nextInt(25) == 0) {
					map.genPlatform();
					System.out.println("Platform Generated");
				}
				map.move();
				if (detectDeath() == true)
					restart();
				distance++;
				updateDistanceMeter();
			}
		}.start();
	}

	private void restart() {
		player.setTranslateX(STARTING_X);
		player.setTranslateY(STARTING_Y);
		player.setXVel(0);
		player.setYVel(0);
		distance = 0;
		map.clear();
	}

	private void handleKeyPressEvent(KeyEvent ke) {
		KeyCode kc = ke.getCode();
		EventType<KeyEvent> et = ke.getEventType();
		if (PRINT_DATA)
			System.out.println(et);
		switch (kc) {
		case A:
		case LEFT:
			player.setMovDir(Player.Direction.LEFT);
			break;
		case D:
		case RIGHT:
			player.setMovDir(Player.Direction.RIGHT);
			break;
		case W:
		case UP:
			player.jump();
			break;
		default:
			break;
		}
	}

	private void handleKeyReleaseEvent(KeyEvent ke) {
		KeyCode kc = ke.getCode();
		EventType<KeyEvent> et = ke.getEventType();
		if (PRINT_DATA)
			System.out.println(et);
		switch (kc) {

		case LEFT:
			if (player.getMoveDir() != Player.Direction.RIGHT)
				player.setMovDir(Player.Direction.NONE);
			break;
		case RIGHT:
			if (player.getMoveDir() != Player.Direction.LEFT)
				player.setMovDir(Player.Direction.NONE);
			break;

		default:
			break;
		}
	}

	private boolean detectDeath() {
		if (map.hitsMap(new Point2D(player.getTranslateX(), player.getTranslateY())) != null
				|| map.hitsMap(new Point2D(player.getTranslateX() + Player.CHAR_WIDTH, player.getTranslateY())) != null
				|| map.hitsMap(new Point2D(player.getTranslateX(), player.getTranslateY() + Player.CHAR_HEIGHT)) != null
				|| map.hitsMap(new Point2D(player.getTranslateX() + Player.CHAR_WIDTH,
						player.getTranslateY() + Player.CHAR_HEIGHT)) != null) {
			return true;
		}

		if (player.getTranslateY() < 0 || player.getTranslateY() > WINDOW_HEIGHT)
			return true;
		if (player.getTranslateX() < 0 || player.getTranslateX() > WINDOW_WIDTH)
			return true;
		return false;
	}

	private void updateDistanceMeter() {
		distanceMeter.setText("Distance: " + distance + "m");
	}

	private MediaPlayer music;
	private double distance = 0;
	private Text distanceMeter;
	private Random rgen;
	private Map map;
	private Player player;
}
