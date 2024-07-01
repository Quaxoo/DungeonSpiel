package Util;

import Main.Game;
import Main.Thread.Update;
import States.Settings;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class Music implements Updateable{

    public static Music music;
    private static ArrayList<Clip> audios = new ArrayList<>();
    private Stack<Clip> queue = new Stack<>();
    private Clip clip;
    private boolean timeout;
    private int cooldown;
    private float fade;
    private final int fadelength = 16;
    private final float gainRangeCompromise = 50f;
    private float rate;
    private FloatControl volumeControl;

    private static final float[] GainAdjustments = {0, 10f};

    public Music(){
        music = this;
        cooldown = 2 * Game.UPS;
        timeout = true;
        Update.add(Update.Always, this);
    }

    @Override
    public void update() {
        if (queue.isEmpty()){
            reloadQueue();
        }
        if (!isClipPlaying() && !timeout){
            //setTimeout();
        }
        if (timeout){
            cooldown--;
            if(cooldown <= 0 && !queue.isEmpty()){
                //play(queue.pop());
            }
        }
        if (isClipPlaying()){
            //fade();
        }
    }

    private void reloadQueue(){
        int index = Random.get(0, audios.size() - 1);
        for (int i = 0; i < audios.size(); i++){
            if (index >= audios.size()){
                index = 0;
            }
            queue.push(audios.get(index));
            index++;
        }
    }

    private void play(Clip clip){
        timeout = false;
        this.clip = clip;
        rate = fadelength * clip.getFormat().getFrameRate();
        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);
        fade();
        changeVolume();
        clip.start();
    }

    private boolean isClipPlaying(){
        return clip != null && clip.getFramePosition() < clip.getFrameLength();
    }

    private void setTimeout(){
        timeout = true;
        cooldown = Random.get(6, 9) * Game.UPS;
    }

    public static void load(){
        String path = Files.getPath() + "assets/audio/music/";
        try {
            Clip c1 = AudioSystem.getClip();
            c1.open(AudioSystem.getAudioInputStream(new File(path + "1.wav")));
            audios.add(c1);

            Clip c2 = AudioSystem.getClip();
            c2.open(AudioSystem.getAudioInputStream(new File(path + "2.wav")));
            audios.add(c2);
        }catch (Exception e){
            System.out.println(e.getMessage() + " " + path);
        }
    }

    private void fade(){
        if(clip.getFramePosition() <= rate){
            fade = clip.getFramePosition()/rate;
            changeVolume();
        }else if(clip.getFramePosition() >= clip.getFrameLength() - rate){
            fade = (clip.getFrameLength() - clip.getFramePosition())/rate;
            changeVolume();
        }else{
            fade = 1;
        }
    }

    public static Music get(){
        return music;
    }

    private float getVolume(){
        return Settings.MUSIC.get() * 0.01f;
    }

    public void changeVolume() {
        if (volumeControl != null) {
            float range = volumeControl.getMaximum() - volumeControl.getMinimum() - gainRangeCompromise;
            float gain = (range * getVolume() * fade) + volumeControl.getMinimum() + gainRangeCompromise /2f + GainAdjustments[audios.indexOf(clip)];
            gain = getVolume() == 0 ? volumeControl.getMinimum() : gain;
            volumeControl.setValue(gain);
        }
    }
}
