package net.myacxy.agsm.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import net.myacxy.jgsq.model.Game;
import net.myacxy.jgsq.model.GameServer;
import net.myacxy.jgsq.model.Player;

import java.util.ArrayList;
import java.util.Map;

public class GameServerEntity extends SugarRecord<GameServerEntity>
{
    public GameEntity game;
    public String coloredHostName;
    public String hostName;
    public String mapName;
    public String ipAddress;
    public int port;
    public int maxClients;
    public boolean isOnline;
    public String parametersJson; // Map<String, String>
    public String playersJson; // ArrayList<PlayerEntity>
    public String rcon;
    public String queryPort;

    @Ignore
    public Map<String, String> parameters;

    @Ignore
    public ArrayList<PlayerEntity> players;

    public GameServerEntity() { }

    public GameServerEntity(GameServer server)
    {
        game = new GameEntity(server.game);
        coloredHostName = server.coloredHostName;
        hostName = server.hostName;
        mapName = server.mapName;
        port = server.port;
        ipAddress = server.ipAddress;
        maxClients = server.maxClients;
        isOnline = server.isOnline;
        parameters = server.parameters;
        rcon = server.rcon;
        queryPort = server.queryPort;

        setPlayers(server.players);
    }

    public void setPlayers(ArrayList<Player> players)
    {
        // ArrayList<Player> -> ArrayList<PlayerEntity>
        this.players = new ArrayList<>();
        for (Player player: players)
        {
            this.players.add(new PlayerEntity(player));
        }
    }

    @Override
    public void save()
    {
        // ArrayList<Player> -> JSON
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        playersJson = gson.toJson(players);

        // Map<String, String> -> JSON
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        parametersJson = gson.toJson(parameters);

        super.save();
    }
} // GameServerEntity
