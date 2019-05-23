package application;


import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JukeBox2 extends JFXPanel {

    public static final String EXPLOSION    = "sounds/Explosion.mp3";
    public static final String ENEMYHIT     = "sounds/enemyhit.mp3";
    public static final String NEWLEVEL     = "sounds/newLevel.mp3";
    public static final String POINTSBLIP     = "sounds/Beep1.mp3";
    public static final String SNAKEHIT    = "sounds/Hit1.wav";
    public static final String SNAKEHIT2    = "sounds/Hit2.wav";
    public static final String GAMEOVER    = "sounds/GameOver.mp3";

    private static Map<String, MediaPlayer> looping = new HashMap<>();

    private static JFXPanel panel = new JFXPanel();

    public static final void playMP3(String file) {
        playMP3Times(file, 1);
    }

    public static final void stopAllLoopsExcept(String file){
        for(String key : looping.keySet()){
            if(!key.equals(file)) {
                stopLoop(key);
            }
        }
    }

    public static final void stopLoop(){
        for(String file : looping.keySet()){
            stopLoop(file);
        }
    }

    public static final void stopLoop(String file){
        if(looping.containsKey(file)){
            looping.get(file).setCycleCount(0);
            looping.get(file).stop();
            looping.remove(file);
        }
    }


    public static final void playMP3(String file, boolean loop){
        if(loop)
            playMP3Times(file, -1);
        else
            playMP3Times(file, 1);
    }
    public static final void playMP3Times(String file, int count){

        if(!looping.containsKey(file)){
            Media hit = new Media(new File(file).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setCycleCount(count);
            if(count == -1)
                looping.put(file, mediaPlayer);
            mediaPlayer.play();
            mediaPlayer.setVolume(1);
        }


    }

}
