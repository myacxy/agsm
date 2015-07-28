package net.myacxy.agsm.interfaces;

import net.myacxy.jgsq.model.Game;
import net.myacxy.jgsq.model.ServerProtocolType;

import java.util.Map;

public interface GameFinder
{
    Map<String, Game> findAll();

    Map<String, Game> findWithType(ServerProtocolType protocolType);

    Game get(String name);

    void setConfig(String configName);
}
