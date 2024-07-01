package States;

import Main.GameFrame;

public abstract class Constants {

    public static float globalScale = (float) Settings.ScreenSize.getWidth() == 1920 ? 1.25f : 1;
    public static double uiBottom = GameFrame.get().getHeight() * 0.9;
}
