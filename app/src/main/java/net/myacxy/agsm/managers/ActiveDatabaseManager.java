package net.myacxy.agsm.managers;

import com.activeandroid.query.Select;

import net.myacxy.agsm.interfaces.DatabaseManager;
import net.myacxy.agsm.models.GameEntity;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.models.PlayerEntity;
import net.myacxy.jgsq.model.Game;
import net.myacxy.jgsq.model.GameServer;
import net.myacxy.jgsq.model.Player;

import java.util.List;

/**
 * DatabaseManager for ActiveAndroid ORM database
 */
public class ActiveDatabaseManager implements DatabaseManager
{
    public void save(Game game)
    {
        GameEntity gameEntity = getGameEntity(game);
        if(gameEntity == null)
        {
            gameEntity = new GameEntity(game);
            gameEntity.save();
        }
        else
        {
            update(game);
        }
    }

    public void save(GameServer gameServer)
    {
        GameServerEntity gameServerEntity = getGameServerEntity(gameServer);
        if(gameServerEntity == null)
        {
            gameServerEntity = new GameServerEntity(gameServer);
            save(gameServer.game);
            gameServerEntity.save();

            for (Player player: gameServer.players)
            {
                save(player, gameServerEntity);
            }
        }
        else
        {
            update(gameServer);
        }
    }

    public void save(Player player, GameServerEntity gameServerEntity)
    {

    }

    public void update(Game game)
    {
        GameEntity gameEntity = getGameEntity(game);
        gameEntity.name = game.name;
        gameEntity.abbreviatedName = game.abbreviatedName;
        gameEntity.alternativeName = game.alternativeName;
        gameEntity.defaultPort = game.defaultPort;
        gameEntity.serverProtocolType = game.serverProtocolType.toString();
        gameEntity.save();
    }

    public void update(Player player)
    {

    }

    public void update(GameServer gameServer)
    {
        // update parameters
        GameServerEntity gameServerEntity = getGameServerEntity(gameServer);
        gameServerEntity.coloredHostName = gameServer.coloredHostName;
        gameServerEntity.hostName = gameServer.hostName;
        gameServerEntity.ipAddress = gameServer.ipAddress;
        gameServerEntity.isOnline = gameServer.isOnline;
        gameServerEntity.mapName = gameServer.mapName;
        gameServerEntity.maxClients = gameServer.maxClients;
        gameServerEntity.parameters = gameServer.parameters;
        gameServerEntity.port = gameServer.port;
        gameServerEntity.queryPort = gameServer.queryPort;
        gameServerEntity.rcon = gameServer.rcon;

        // delete old player entries
        for (PlayerEntity playerEntity : gameServerEntity.getPlayers())
        {
            playerEntity.delete();
        }

        gameServerEntity.save();

        // register current players
        for (Player player: gameServer.players)
        {
            save(player, gameServerEntity);
        }
    }

    protected GameEntity getGameEntity(Game game)
    {
        return new Select()
                .from(GameEntity.class)
                .where("name = ?", game.name)
                .executeSingle();
    }


    protected GameServerEntity getGameServerEntity(GameServer gameServer)
    {
        return new Select()
                .from(GameServerEntity.class)
                .where("ip_address = ?", gameServer.ipAddress)
                .and("port = ?", gameServer.port)
                .executeSingle();
    }
}
