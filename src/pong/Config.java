package pong;

import java.awt.Color;

public class Config {

	public static class Window {
		public static final int width = 1000;
		public static final int height = 700;
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
}
