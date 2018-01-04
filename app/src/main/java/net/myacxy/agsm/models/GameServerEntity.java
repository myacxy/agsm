package net.myacxy.agsm.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import net.myacxy.jgsq.models.GameServer;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Table(name = "game_servers")
public class GameServerEntity extends Model
{
    @Column(name = "game")
    public GameEntity game;
    @Column(name = "colored_host_name")
    public String coloredHostName;
    @Column(name = "host_name")
    public String hostName;
    @Column(name = "map_name")
    public String mapName;
    @Column(name = "ip_address")
    public String ipAddress;
    @Column(name = "port")
    public int port;
    @Column(name = "ping")
    public int ping;
    @Column(name = "max_clients")
    public int maxClients;
    @Column(name = "online")
    public boolean isOnline;
    @Column(name = "rcon")
    public String rcon;
    @Column(name = "query_port")
    public String queryPort;
    @Column(name = "parameters")
    public TreeMap<String, String> parameters;
    @Column(name = "last_update")
    public DateTime lastUpdate;
    @Column(name = "password_protected")
    public boolean isPasswordProtected;

    public GameServerEntity()
    {
        super();
    }

    public GameServerEntity(GameServer server)
    {
        super();
        game = new GameEntity(server.game);
        coloredHostName = server.coloredHostName.trim();
        hostName = server.hostName.trim();
        mapName = server.mapName;
        port = server.port;
        ping = server.ping;
        ipAddress = server.ipAddress;
        maxClients = server.maxClients;
        isOnline = server.isOnline;
        parameters = server.parameters;
        rcon = server.rcon;
        queryPort = server.queryPort;
        isPasswordProtected = server.isPasswordProtected;
    }

    public List<PlayerEntity> getClients()
    {
        return getMany(PlayerEntity.class, "game_server");
    }

    public List<PlayerEntity> getPlayers()
    {
        List<PlayerEntity> clients = getClients();
        List<PlayerEntity> players = new ArrayList<>();
        for (PlayerEntity client : clients)
        {
            if(client.ping > 0) players.add(client);
        }
        return players;
    }

    public List<PlayerEntity> getBots()
    {
        List<PlayerEntity> clients = getClients();
        List<PlayerEntity> bots = new ArrayList<>();
        for (PlayerEntity client : clients)
        {
            if(client.ping == 0) bots.add(client);
        }
        return bots;
    }

    public String getHostName()
    {
        return hostName == null ? ipAddress + ":" + port : hostName.trim();
    }
} // GameServerEntity
