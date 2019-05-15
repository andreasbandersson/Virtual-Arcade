package pong;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound implements Runnable {
	
	private Thread thread;
	private String fileToPlay;

	private Clip clip;
	private AudioInputStream inputStream;
	private boolean stopped;
	
	public Sound(String file) {
		this.fileToPlay = file;

		this.start();
	}
	
	public void start() {
		if(this.thread == null) {
			this.thread = new Thread(this);
			this.thread.start();
		}
	}
	
	
	public void run() {
		init();
	}
	
	private void init(){
		this.stopped = false;
		try {
			System.out.println("init..");
			this.clip = AudioSystem.getClip();
			this.inputStream = AudioSystem.getAudioInputStream(new File(this.fileToPlay));
			this.clip.open(this.inputStream);
			this.clip.start();
			
		} catch(Exception e) {
			System.out.println("play sound error: " + e.getMessage() + " for " + this.fileToPlay);
		}	
	}
	
	public void stopSound() {
		if(!this.stopped) {
			this.clip.stop();
			this.stopped = true;
		}
	}
	
	public void resumeSound() {
		if(this.stopped) {
			this.clip.start();
			this.stopped = false;
		}
	}
	
	
    public static synchronized void play(final String fileName) 
    {
        // Note: use .wav files             
        new Thread(new Runnable() { 
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fileName));
                    clip.open(inputStream);
                    clip.start(); 
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
                }

                	System.out.println(Game.test);
            }
        }).start();
    }

}