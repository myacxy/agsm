package net.myacxy.agsm.interfaces;

import net.myacxy.jgsq.models.Game;

public interface ServerManager
{
    boolean isOnline(Game game, String ipAddress, int port);

    void create(Game game, String ipAddress, int port, OnServerCreatedListener listener);

    void update(Game game, String ipAddress, int port, OnServerUpdatedListener listener);
}
