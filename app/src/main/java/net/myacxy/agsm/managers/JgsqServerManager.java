package net.myacxy.agsm.managers;

import net.myacxy.agsm.interfaces.OnServerCreatedListener;
import net.myacxy.agsm.interfaces.OnServerUpdatedListener;
import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.interfaces.ServerManager;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.jgsq.factories.GameServerFactory;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class JgsqServerManager implements ServerManager
{
    private final GameServerFactory gameServerFactory = new GameServerFactory();

    @Bean(ActiveServerFinder.class)
    protected ServerFinder serverFinder;

    @AfterInject
    public void setUp()
    {

    }

    @Background
    public void create(Game game, String ipAddress, int port, OnServerCreatedListener listener)
    {
        GameServer gameServer = gameServerFactory.getGameServer(game);
        gameServer.connect(ipAddress, port);

        if(listener != null)
        {
            listener.onServerCreated(gameServer);
        }
    }

    @Background
    public void update(Game game, String ipAddress, int port, OnServerUpdatedListener listener)
    {
        GameServer gameServer = gameServerFactory.getGameServer(game);
        gameServer.connect(ipAddress, port);
        gameServer.update();

        listener.onServerUpdated(gameServer);
    }

    @Override
    public boolean isOnline(Game game, String ipAddress, int port)
    {
        return false;
    }
}
