package net.myacxy.agsm.interfaces;

import net.myacxy.jgsq.models.Game;

public interface ServerManager
{
    boolean isOnline(Game game, String ipAddress, int port);

    // TODO serverCreatedListener return server
    void create(Game game, String ipAddress, int port, OnServerCreatedListener listener);
}
