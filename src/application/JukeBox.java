package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * The class JukeBox plays and pauses sound files via JavaFX Mediaplayer class.
 * 
 * @author Andreas Andersson
 *
 */

public class JukeBox {
	private MediaPlayer mediaPlayer;
	private ArrayList<String> songList = new ArrayList<>();
	private Iterator<String> itr;
	private boolean playing = false;
	private static String song1 = "sounds/Lobby-Sound-1.mp3";
	private static String song2 = "sounds/Lobby-Sound-2.mp3";
	private static String song3 = "sounds/Lobby-Sound-3.mp3";
	private static String song4 = "sounds/Lobby-Sound-4.mp3";
	private static String song5 = "sounds/Lobby-Sound-5.mp3";
	private static String song6 = "sounds/Lobby-Sound-6.mp3";
	private static String song7 = "sounds/Lobby-Sound-7.mp3";
	private static String intro = "sounds/Welcome-Sound.mp3";

	/**
	 * The Constructor which takes the soundfiles as Strings and puts them in an
	 * ArrayList. Then shuffles them so that the songs plays randomly except for the
	 * intro, which always plays first. It also adds and Iterator to the ArrayList.
	 */
	public JukeBox() {
		songList.add(song1);
		songList.add(song2);
		songList.add(song3);
		songList.add(song4);
		songList.add(song5);
		songList.add(song6);
		songList.add(song7);
		Collections.shuffle(songList);
		songList.add(0, intro);
		itr = songList.iterator();
	}

	/**
	 * Constructor that takes one song.
	 * 
	 * @param song1 the song that is going to be played.
	 */
	public JukeBox(String song1) {
		songList.add(song1);
		itr = songList.iterator();
	}

	/**
	 * Constructor that takes an ArrayList of songs.
	 * 
	 * @param songList the song list with the songs that are going to be played.
	 */
	public JukeBox(ArrayList<String> songList) {
		this.songList = songList;
		itr = songList.iterator();
	}

	/**
	 * Sets the the music and starts it.
	 */
	public void play() {
		Media sound = new Media(new File(itr.next()).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		playing = true;
		mediaPlayer.setVolume(0.1);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.stop();
				if (itr.hasNext()) {
					play();
				}
			}
		});
	}

	/**
	 * Sets the music with an assigned value and starts it.
	 * 
	 * @param volume the volume of the music
	 */
	public void playWithCustomVol(double volume) {
		Media sound = new Media(new File(itr.next()).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		playing = true;
		mediaPlayer.setVolume(volume);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.stop();
				if (itr.hasNext()) {
					play();
				}
			}
		});
	}

	/**
	 * Pauses the music if the music is playing, otherwise the function plays the
	 * music.
	 */
	public void pauseOrPlay() {
		if (!playing) {
			mediaPlayer.play();
			playing = true;
			mediaPlayer.setVolume(0.1);
		} else if (playing) {
			mediaPlayer.pause();
			playing = false;
		}
	}

	/**
	 * Stops the music if the music is playing.
	 */
	public void stopSound() {
		if (playing) {
			mediaPlayer.stop();
			playing = false;
		}
	}

	/**
	 * @return returns true if music is paused. If music is Playing returns false.
	 */
	public boolean isPaused() {
		if (!playing) {
			return true;
		} else {
			return false;
		}
	}

}