package Util;

import States.Settings;

import java.util.ArrayList;
import java.util.HashMap;

public class Language {

    private final String id;
    private final int index;
    public static int amount = 0;
    public static ArrayList<Language> languages = new ArrayList<>();
    private static HashMap<String, String> languagetable;
    public static Language German = new Language("de");
    public static Language English = new Language("en");
    public static Language Dutch = new Language("nl");
    public static Language French = new Language("fr");
    public static Language Italian = new Language("it");
    public static Language Spanish = new Language("es");
    public static Language Boarisch = new Language("by");
    private Language(String id){
        this.id = id;
        this.index = amount;
        amount++;
        languages.add(this);
    }

    public String getFile() {
        return "data/lang/" + id + ".txt";
    }
    public int getIndex() {
        return index;
    }

    public static Language getLanguage(int index){
        for (Language s: languages){
            if (s.getIndex() == index)
                return s;
        }
        return German;
    }

    public static void load(){
        languagetable = Files.loadHashMap(getLanguage(Settings.LANGUAGE.get()).getFile());
    }

    public static String get(String id){
        return languagetable.get(id) != null ? languagetable.get(id) : languagetable.get(Text.Error);
    }

}