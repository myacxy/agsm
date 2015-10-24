package net.myacxy.agsm.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.jgsq.models.Game;

public class GameIconHelper
{

    public static Drawable getDrawable16(Context context, String gameName)
    {
        GameFinder gameFinder = new JgsqGameFinder();
        Game game = gameFinder.find(gameName);
        String drawableName = String.format("ic_%s_16", game.abbreviatedName.toLowerCase());
        int id = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        return ContextCompat.getDrawable(context, id);
    }

    public static Drawable getDrawable24(Context context, String gameName)
    {
        GameFinder gameFinder = new JgsqGameFinder();
        Game game = gameFinder.find(gameName);
        String drawableName = String.format("ic_%s_24", game.abbreviatedName.toLowerCase());
        int id = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        return ContextCompat.getDrawable(context, id);
    }
}
