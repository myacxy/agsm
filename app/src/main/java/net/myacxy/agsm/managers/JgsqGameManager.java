package net.myacxy.agsm.managers;

import net.myacxy.agsm.interfaces.GameManager;
import net.myacxy.jgsq.factories.GameFactory;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.utils.Utilities;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class JgsqGameManager implements GameManager
{
    private GameFactory gameFactory;

    public JgsqGameManager()
    {
        gameFactory = new GameFactory();
        gameFactory.loadConfig(Utilities.getResourceAsStream("games.conf.json"));
    }

    @Override
    public Game create(String name)
    {
        return gameFactory.getGame(name);
    }
} // JgsqGameManager
