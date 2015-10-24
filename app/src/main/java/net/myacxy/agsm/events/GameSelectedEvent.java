package net.myacxy.agsm.events;


import net.myacxy.jgsq.models.Game;

public class GameSelectedEvent
{
    public Game game;

    public GameSelectedEvent(Game game)
    {
        this.game = game;
    }
}
