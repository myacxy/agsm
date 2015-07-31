package net.myacxy.agsm.managers;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.interfaces.ServerManager;
import net.myacxy.agsm.models.GameEntity;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.models.PlayerEntity;
import net.myacxy.jgsq.factories.GameServerFactory;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;
import net.myacxy.jgsq.models.Player;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class JgsqServerManager implements ServerManager
{
    private final GameServerFactory gameServerFactory = new GameServerFactory();

    @Bean
    protected ServerFinder serverFinder;

    @AfterInject
    public void setUp()
    {

    }

    @Background
    public GameServer create(Game game, String ipAddress, int port)
    {
        GameServer server = gameServerFactory.getGameServer(game);
        server.connect(ipAddress, port);

        return server;
    }

    public boolean isOnline(Game game, String ipAddress, int port)
    {
        return false;
    }

    public void save()
    {
        GameServerEntity gse = new GameServerEntity(server);
        GameEntity knownGame = new Select()
                .from(GameEntity.class)
                .where("name = ?", gse.game.name)
                .executeSingle();

        if(knownGame == null)
        {
            gse.game.save();
        }
        else
        {
            gse.game = knownGame;
        }

        gse.save();

        ActiveAndroid.beginTransaction();
        try
        {
            for (Player player: server.players) {
                PlayerEntity pe = new PlayerEntity(player);
                pe.gameServer = gse;
                pe.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
}
