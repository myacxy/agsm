package net.myacxy.agsm.interfaces;

import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;

public interface ServerManager
{
    boolean isOnline(Game game, String ipAddress, int port);

    GameServer create(Game game, String ipAddress, int port);

}
