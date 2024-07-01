package Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Text{

    public static String Error = "error";
    public static String Music = "music";
    public static String Language = "lang";
    public static String Dagger = "dagger";

    public static String Settings = "settings";
    public static String Continue = "continue";
    public static String Leave = "leave";
    public static String Grafik = "graphics";
    public static String Audio = "audio";
    public static String Fps = "fps";

    public static String SFX = "sfx";
    public static String KeyMissingText = "keymissing";
    public static String KillsMissing = "killsmissing";
    public static String EnteringLevel = "levelentry";

    private static final String chars = " ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ0123456789%.";
    private static final Sprite[] charSprites = new Sprite[chars.length()];
    private static final Sprite[] charSpritesHovered = new Sprite[chars.length()];
    private static final Sprite[] charSpritesMarked = new Sprite[chars.length()];
    private static final HashMap<Float, Sprite[]> scaledBuchstabenSprites = new HashMap<>();
    private static final HashMap<Float, Sprite[]> scaledBuchstabenSpritesHovered = new HashMap<>();
    private static final HashMap<Float, Sprite[]> scaledCharSpritesMarked = new HashMap<>();
    private static final float scale = 1;
    private static final float width = 9 * scale, height = 10 * scale;
    private static final float numberswidth = 10 * scale;
    private static final float gap = 15;


    public static void load(){
        Sprite sprite = new Sprite(Sprite.Chars, scale);
        Sprite spriteH = new Sprite(Sprite.CharsHovered, scale);
        Sprite spriteM = new Sprite(Sprite.CharsMarked, scale);

        for (int i = 0; i < chars.indexOf('0'); i++){
            charSprites[i] = new Sprite(sprite.getSubimage(i * width,0,width,height));
            charSprites[i].trim();
            charSpritesHovered[i] = new Sprite(spriteH.getSubimage(i * width,0,width,height));
            charSpritesHovered[i].trim();
            charSpritesMarked[i] = new Sprite(spriteM.getSubimage(i * width,0,width,height));
            charSpritesMarked[i].trim();
        }
        for (int i = chars.indexOf('0'); i < chars.length(); i++){
            float x = (i - chars.indexOf('0')) * numberswidth + chars.indexOf('0') * width;
            charSprites[i] = new Sprite(sprite.getSubimage(x,0,numberswidth,height));
            charSprites[i].trim();
            charSpritesHovered[i] = new Sprite(spriteH.getSubimage(x,0,numberswidth,height));
            charSpritesHovered[i].trim();
            charSpritesMarked[i] = new Sprite(spriteM.getSubimage(x,0,width,numberswidth));
            charSpritesMarked[i].trim();
        }
        scaledBuchstabenSprites.put(scale, charSprites);
        scaledBuchstabenSpritesHovered.put(scale, charSpritesHovered);
        scaledCharSpritesMarked.put(scale, charSpritesHovered);
    }
    public static Sprite get(String content, float scale){
        return Text(content, scale, charSprites);
    }
    public static Sprite getHovered(String content, float scale){
        return Text(content, scale, charSpritesHovered);
    }
    public static Sprite getMarked(String content, float scale){
        return Text(content,scale, charSpritesMarked);
    }
    public static Sprite getTranslated(String content, float scale){
        return get(Util.Language.get(content), scale);
    }
    public static Sprite getTranslatedHovered(String content, float scale){
        return getHovered(Util.Language.get(content), scale);
    }
    public static Sprite getTranslated(String content, String variable, float scale){
        return VariableText(Util.Language.get(content), variable, scale, charSprites);
    }
    public static Sprite getTranslatedHovered(String content, String variable, float scale){
        return VariableText(Util.Language.get(content), variable, scale, charSpritesHovered);
    }

    private static Sprite Text(String content, float s, Sprite[] zeichen){
        Sprite[] sprites = scale(zeichen, s);
        int line = 0;
        ArrayList<Sprite> lines = new ArrayList<>();
        lines.add(new Sprite(sprites[chars.contains(String.valueOf(content.charAt(0))) ? chars.indexOf(content.charAt(0)) : 0]));
        for (int i = 1; i < content.length(); i++){
            if (content.charAt(Math.min(i + 1, content.length()-1)) == '/'){
                line++;
                i += 3;
                lines.add(new Sprite(sprites[chars.contains(String.valueOf(content.charAt(i))) ? chars.indexOf(content.charAt(i)) : 0]));
            }else{
                int index = chars.contains(String.valueOf(content.charAt(i))) ? chars.indexOf(content.charAt(i)) : 0;
                lines.get(line).addAtRightBottom(sprites[index], s);
            }
        }

        for (int l = 1; l < lines.size(); l++){
            lines.get(0).addAtBottomCenter(lines.get(l), 3*s);
        }

        return lines.get(0);
    }

    private static Sprite VariableText(String content, String variable, float s, Sprite[] zeichen){
        Sprite[] sprites = scale(zeichen, s);
        int line = 0;
        ArrayList<Sprite> lines = new ArrayList<>();
        lines.add(new Sprite(sprites[chars.contains(String.valueOf(content.charAt(0))) ? chars.indexOf(content.charAt(0)) : 0]));
        for (int i = 1; i < content.length(); i++){
            if (content.charAt(Math.min(i + 1, content.length()-1)) == '/'){
                line++;
                i += 3;
                lines.add(new Sprite(sprites[chars.contains(String.valueOf(content.charAt(i))) ? chars.indexOf(content.charAt(i)) : 0]));
            }else{
                if (content.charAt(i) == '#'){
                    Sprite v = get(variable, s);
                    v.scaleY(lines.get(line).getHeight());
                    lines.get(line).addAtRightBottom(v, s);
                }else {
                    int index = chars.contains(String.valueOf(content.charAt(i))) ? chars.indexOf(content.charAt(i)) : 0;
                    lines.get(line).addAtRightBottom(sprites[index], s);
                }
            }
        }

        for (int l = 1; l < lines.size(); l++){
            lines.get(0).addAtBottomCenter(lines.get(l), 3*s);
        }

        return lines.get(0);
    }

    private static Sprite[] scale(Sprite[] sprites,float scale){
        if(getHashSet(sprites).containsKey(scale)){
            return getHashSet(sprites).get(scale);
        }
        Sprite[] newSprites = new Sprite[sprites.length];
        for (int i = 0; i < sprites.length; i++){
            newSprites[i] = new Sprite(sprites[i]);
            newSprites[i].scale(scale);
        }
        newSprites[0].cut(3.5 * scale,3 * scale, 3.5 * scale,3 * scale);
        for (Sprite s: newSprites){
            s.trim();
        }
        getHashSet(sprites).put(scale,newSprites);
        return newSprites;
    }

    private static HashMap<Float, Sprite[]> getHashSet(Sprite[] sprites){
        if (Arrays.equals(sprites, charSprites)) {
            return scaledBuchstabenSprites;
        }else if(Arrays.equals(sprites, charSpritesHovered)){
            return scaledBuchstabenSpritesHovered;
        }else if(Arrays.equals(sprites, charSpritesMarked)){
            return scaledCharSpritesMarked;
        }else {
            return null;
        }
    }

}
