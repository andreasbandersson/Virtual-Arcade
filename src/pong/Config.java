package pong;
import javafx.scene.paint.Color;



public class Config {
	
	public static class Game {
		public static final int maxWins = 5;
		public static Color[] colors = {
				Color.WHITE, // 	new Color(255, 255, 255) 
				Color.BLACK,  // 	new Color(0, 0, 0)
				Color.ORANGE, // 	.. etc
				Color.YELLOW, 
				Color.GREEN, 
				Color.MAGENTA
		};
	}

	public static class Window {
		public static final int width = 800;
		public static final int height = 500;
		public static final String title = "Pong";
	} 
	
	public static class Platform {
		public static final int width=20;
		public static final int height=100;
		public static Color color = Color.rgb(255, 255, 255);
		public static int speed = 10;
		public static final int xOffset = 5;
	}
	
	public static class Ball {
		public static final int width = 20, height = 20;
		public static final int speed = 10;
		public static final String bounceSound = "files/bounce.wav";
		public static final int delayTime = 2000;
		public static final Color defaultColor = Color.WHITE;
	}
	
	public static class ColorOpt {
		public static final int width = 32, height = 32;
	}
}
