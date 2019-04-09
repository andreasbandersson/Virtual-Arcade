package pong;

import java.awt.Color;

public class Config {
	
	public static class Game {
		public static final int maxWins = 5; 
	}

	public static class Window {
		public static final int width = 800;
		public static final int height = 500;
		public static final String title = "Pong";
	} 
	
	public static class Platform {
		public static final int width=20;
		public static final int height=100;
		public static Color color = new Color(255, 255, 255);
		public static int speed = 10;
		public static final int xOffset = 5;
	}
	
	public static class Ball {
		public static final int width = 20, height = 20;
		public static final int speed = 10;
		public static final String bounceSound = "files/bounce.wav";
	}
	
	public static class ColorOpt {
		public static final int width = 32, height = 32;
	}
}
