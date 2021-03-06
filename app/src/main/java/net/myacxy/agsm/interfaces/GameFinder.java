package net.myacxy.agsm.interfaces;

import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.helpers.ServerProtocolType;

import java.util.Map;

public interface GameFinder
{
    Map<String, Game> findAll();

    Map<String, Game> findWithType(ServerProtocolType protocolType);

    Game find(String name);

    void setConfig(String configName);
}
