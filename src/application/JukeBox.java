package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class JukeBox {
	private MediaPlayer mediaPlayer;
	private ArrayList<String> songList = new ArrayList<>();
	private Iterator<String> itr;
	private double volume;

	
	public JukeBox(String song1) {
		songList.add(song1);
		itr = songList.iterator();
	}
	
	public JukeBox(String song1, String song2) {
		songList.add(song1);
		songList.add(song2);
		itr = songList.iterator();
	}

	public JukeBox(String song1, String song2, String song3) {
		songList.add(song1);
		songList.add(song2);
		songList.add(song3);
		itr = songList.iterator();
	}
	public JukeBox(ArrayList<String> songList) {
		this.songList = songList;
		itr = songList.iterator();
	}

	// Sets the the music and starts it.
	public void play() {
		
		Media sound = new Media(new File(itr.next()).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		mediaPlayer.setVolume(0.1);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				mediaPlayer.stop();
				if(itr.hasNext()) {
					mediaPlayer.play();
				}
				return;
			}
		});
	}


	public void playWithCustomVol(double volume) {
		this.volume = volume;
		Media sound = new Media(new File(itr.next()).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		mediaPlayer.setVolume(volume);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.stop();
				if(itr.hasNext()) {
					mediaPlayer.play();
				}
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
	public boolean isMute() {
		return mediaPlayer.isMute();
	}
}