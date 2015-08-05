package net.myacxy.agsm.utils;

import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.jgsq.factories.GameFactory;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.helpers.ServerProtocolType;
import net.myacxy.jgsq.utils.Utilities;

import org.androidannotations.annotations.EBean;

import java.util.Map;

@EBean(scope = EBean.Scope.Singleton)
public class JsonGameFinder implements GameFinder
{
    private final GameFactory gameFactory = new GameFactory();
    private boolean configLoaded;

    @Override
    public Map<String, Game> findAll()
    {
        if(configLoaded) return gameFactory.getSupportedGames();
        return null;
    }

    @Override
    public Map<String, Game> findWithType(ServerProtocolType protocolType)
    {
        if(configLoaded) return gameFactory.getSupportedGames(protocolType);
        return null;
    }

    @Override
    public Game get(String name)
    {
        if(configLoaded) return gameFactory.getGame(name);
        return null;
    }

    @Override
    public void setConfig(String configName)
    {
        Map<String, Game> games = gameFactory.loadConfig(Utilities.getResourceAsStream(configName));
        if(games != null) configLoaded = true;
    }
} // JsonGameFinder
