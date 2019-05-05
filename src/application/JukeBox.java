package application;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class JukeBox {
	private MediaPlayer mediaPlayer;
	private String song1;
	private String song2;
	private String song3;
	private ArrayList<String> songList = new ArrayList<>();

	
	public JukeBox(String song1) {
		this.song1 = song1;
		songList.add(song1);
	}
	
	public JukeBox(String song1, String song2) {
		this.song1 = song1;
		this.song2 = song2;
		songList.add(song1);
		songList.add(song2);
	}

	public JukeBox(String song1, String song2, String song3) {
		this.song1 = song1;
		this.song2 = song2;
		this.song3 = song3;
		songList.add(song1);
		songList.add(song2);
		songList.add(song3);
	}

	// Sets the the login music and starts it.
	public void play() {
		
		Media sound = new Media(new File(songList.get(0)).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		mediaPlayer.setVolume(0.1);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				mediaPlayer.stop();
			}
		});
	}

	// Mutes the login music if the music is playing, otherwise the function unmutes
	// the music.
	public void muteUnmute() {
		if (mediaPlayer.isMute()) {
			mediaPlayer.setMute(false);
			mediaPlayer.setVolume(0.1);

		} else {
			mediaPlayer.setMute(true);
		}
	}

	// Stops the music.
	public void stopSound() {
		if (mediaPlayer.getStatus() == Status.PLAYING) {
			mediaPlayer.stop();
		}
	}
}
