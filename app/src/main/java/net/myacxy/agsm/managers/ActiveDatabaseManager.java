package net.myacxy.agsm.managers;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import net.myacxy.agsm.interfaces.DatabaseManager;
import net.myacxy.agsm.models.GameEntity;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.models.PlayerEntity;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;
import net.myacxy.jgsq.models.Player;

import org.androidannotations.annotations.EBean;
import org.joda.time.DateTime;

/**
 * DatabaseManager for ActiveAndroid ORM database
 */
@EBean(scope = EBean.Scope.Singleton)
public class ActiveDatabaseManager implements DatabaseManager
{
    public GameEntity save(Game game)
    {
        GameEntity gameEntity = getGameEntity(game);
        // not in database? -> save
        if(gameEntity == null)
        {
            gameEntity = new GameEntity(game);
            gameEntity.save();
            return gameEntity;
        }
        // in database? -> update
        else
        {
            return update(game);
        }
    } // save game

    public GameServerEntity save(GameServer gameServer)
    {
        GameServerEntity gameServerEntity = getGameServerEntity(gameServer);
        // not in database? -> save
        if(gameServerEntity == null)
        {
            // save server entity before saving players
            gameServerEntity = new GameServerEntity(gameServer);
            gameServerEntity.lastUpdate = DateTime.now();
            gameServerEntity.game = save(gameServer.game);
            gameServerEntity.save();

            // bulk insert players
            ActiveAndroid.beginTransaction();
            try
            {
                for (Player player: gameServer.players)
                {
                    PlayerEntity pe = new PlayerEntity(player);
                    pe.gameServer = gameServerEntity;
                    pe.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally
            {
                ActiveAndroid.endTransaction();
            }
            return gameServerEntity;
        }
        // in database? -> update
        else
        {
            return update(gameServer);
        }
    } // save gameServer

    public PlayerEntity save(Player player, GameServerEntity gameServerEntity)
    {
        // TODO
        throw  new UnsupportedOperationException();
    } // save player, gameServerEntity

    public GameEntity update(Game game)
    {
        GameEntity gameEntity = getGameEntity(game);
        gameEntity.name = game.name;
        gameEntity.abbreviatedName = game.abbreviatedName;
        gameEntity.alternativeName = game.alternativeName;
        gameEntity.defaultPort = game.defaultPort;
        gameEntity.serverProtocolType = game.serverProtocolType.toString();
        gameEntity.save();

        return gameEntity;
    } // update game

    public PlayerEntity update(Player player)
    {
        // TODO
        throw  new UnsupportedOperationException();
    } // update player

    public GameServerEntity update(GameServer gameServer)
    {
        // update parameters
        GameServerEntity gameServerEntity = getGameServerEntity(gameServer);
        gameServerEntity.lastUpdate = DateTime.now();
        gameServerEntity.coloredHostName = gameServer.coloredHostName;
        gameServerEntity.hostName = gameServer.hostName;
        gameServerEntity.ipAddress = gameServer.ipAddress;
        gameServerEntity.isOnline = gameServer.isOnline;
        gameServerEntity.mapName = gameServer.mapName;
        gameServerEntity.maxClients = gameServer.maxClients;
        gameServerEntity.parameters = gameServer.parameters;
        gameServerEntity.port = gameServer.port;
        gameServerEntity.ping = gameServer.ping;
        gameServerEntity.queryPort = gameServer.queryPort;
        gameServerEntity.rcon = gameServer.rcon;

        // delete old player entries
        for (PlayerEntity playerEntity : gameServerEntity.getPlayers())
        {
            playerEntity.delete();
        }

        gameServerEntity.save();

        // bulk register current players
        ActiveAndroid.beginTransaction();
        try
        {
            for (Player player: gameServer.players)
            {
                PlayerEntity pe = new PlayerEntity(player);
                pe.gameServer = gameServerEntity;
                pe.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally
        {
            ActiveAndroid.endTransaction();
        }

        return gameServerEntity;
    } // update gameServer

    public GameEntity getGameEntity(Game game)
    {
        return new Select()
                .from(GameEntity.class)
                .where("name = ?", game.name)
                .executeSingle();
    } // getGameEntity

    public GameServerEntity getGameServerEntity(GameServer gameServer)
    {
        return new Select()
                .from(GameServerEntity.class)
                .where("ip_address = ?", gameServer.ipAddress)
                .and("port = ?", gameServer.port)
                .executeSingle();
    } // getGameServerEntity
} // ActiveDatabaseManager
