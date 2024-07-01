package Util;

import CustomFiles.Inventory.InventoryFile;
import CustomFiles.Inventory.InventoryFileReader;
import Main.Game;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Files {

    public static final String Settings = "settings.txt";

    public static String load(String file){
        try {
            FileReader reader = new FileReader(getSavesFolder() + file);
            StringBuilder content = new StringBuilder();
            int data = reader.read();
            while (data != -1){
                content.append((char) data);
                data = reader.read();
            }
            reader.close();
            return content.toString();
        }catch (IOException c){
            return null;
        }
    }
    public static String save(String file, String content){
        if (!java.nio.file.Files.exists(Path.of(getSavesFolder()))){
            try{
                java.nio.file.Files.createDirectory(Path.of(getSavesFolder()));
            }catch (Exception e){

            }
        }
        try {
            FileWriter writer = new FileWriter(getSavesFolder() + file);
            writer.write(content);
            writer.close();
            return content;
        }catch (IOException c){
            System.out.println(c.getMessage());
            return null;
        }
    }

    public static JSONObject loadJSONObject(String filelocation){
        JSONParser parser = new JSONParser();
        try{
            Object object = parser.parse(new FileReader(Files.getPath() + filelocation));
            return (JSONObject) object;
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            return null;

        }
    }

    public static JSONArray loadJSONArray(String filelocation){
        JSONParser parser = new JSONParser();
        try{
            Object object = parser.parse(new FileReader(getPath() + filelocation));
            return (JSONArray) object;
        } catch (Exception e) {
            try {
                InputStream stream = Files.class.getResourceAsStream("/res/" + filelocation);
                InputStreamReader reader = new  InputStreamReader(stream);
                Object object = parser.parse(reader);
                return (JSONArray) object;
            }catch (Exception g){
                System.out.println(e.getMessage());
                return null;
            }
        }
    }
    public static InventoryFile loadInventory(String filelocation){
        InventoryFileReader reader = new InventoryFileReader(getPath() + filelocation);
        return reader.read();
    }
    public static ArrayList<String> loadArray(String file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getPath() + file));
            ArrayList<String> array = new ArrayList<>();
            while (reader.ready()){
                array.add(reader.readLine());
            }
            return array;
        }catch (Exception e){
            return null;
        }
    }
    public static HashMap<String, String> loadHashMap(String file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getPath() + file));
            HashMap<String, String> map = new HashMap<>();
            while (reader.ready()){
                String line = reader.readLine();
                String key = line.substring(0, line.indexOf(':'));
                key = key.replace("\"", "");
                String value = line.substring(line.indexOf(':') + 2);
                map.put(key,value);
            }
            return map;
        }catch (Exception e){
            return null;
        }
    }

    public static boolean exists(String filelocation){
        return new File(getPath() + filelocation).exists();
    }

    public static boolean existsDirectory(String directorylocation){
        return java.nio.file.Files.isDirectory(Path.of(directorylocation));
    }

    public static String getPath(){
        return getDirectoryPath();
    }

    public static String getJarPath(){
        return String.valueOf(Game.resFolderInJarfile);
    }

    public static String getDirectoryPath(){
        return new File(System.getProperty("user.dir")).getAbsolutePath() + "/resource root/res/";
    }

    public static String getSavesFolder(){
        return new File(System.getProperty("user.dir")).getAbsolutePath() + "/saves/";
    }

}
