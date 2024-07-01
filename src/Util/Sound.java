package Util;

import States.Settings;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.ArrayList;

public class Sound {

    public static ArrayList<Clip> Steps = new ArrayList<>();
    public static ArrayList<Clip> ChestOpening = new ArrayList<>();
    public static ArrayList<Clip> ChestClosing = new ArrayList<>();
    public static ArrayList<Clip> Equip = new ArrayList<>();
    public static ArrayList<Clip> Upgrade = new ArrayList<>();
    public static ArrayList<Clip> GoblinHunt = new ArrayList<>();
    private static final float gainrangeCompromise = 50f;
    private static final String path = Files.getPath() + "assets/audio/sfx/";


    public static void play(ArrayList<Clip> clips){
        Clip clip = clips.get(Random.get(0, clips.size() - 1));
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = volumeControl.getMaximum() - volumeControl.getMinimum() - gainrangeCompromise;
        float gain = (range * getVolume()) + volumeControl.getMinimum() + gainrangeCompromise/2f;
        gain = getVolume() == 0 ? volumeControl.getMinimum() : gain;
        volumeControl.setValue(gain);
        clip.setFramePosition(0);
        clip.start();
    }
    public static void load(){
        add("chest_opening", ChestOpening);
        add("chest_closing", ChestClosing);
        add("equip", Equip);
        add("anvil", Upgrade);
        add("goblinhunt1", GoblinHunt);
        add("goblinhunt2", GoblinHunt);
    }

    private static void add(String file, ArrayList<Clip> clips){
        try {
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(new File(path + file + ".wav")));
            clips.add(c);
        }catch (Exception e){
            System.out.println(e.getMessage() + " " + file);
        }
    }

    private static float getVolume(){
        return Settings.SFX.get() * 0.01f;
    }
}
