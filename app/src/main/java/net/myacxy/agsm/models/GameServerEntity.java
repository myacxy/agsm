package net.myacxy.agsm.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import net.myacxy.jgsq.models.GameServer;

import java.util.List;
import java.util.Map;

@Table(name = "game_servers")
public class GameServerEntity extends Model
{
    @Column(name = "game")
    public GameEntity game;
    @Column(name = "colored_host_name")
    public String coloredHostName;
    @Column(name = "host_name")
    public String hostName;
    @Column(name = "map_anme")
    public String mapName;
    @Column(name = "ip_address")
    public String ipAddress;
    @Column(name = "port")
    public int port;
    @Column(name = "max_clients")
    public int maxClients;
    @Column(name = "online")
    public boolean isOnline;
    @Column(name = "rcon")
    public String rcon;
    @Column(name = "query_port")
    public String queryPort;
    @Column(name ="parameters")
    public Map<String, String> parameters;

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
        ipAddress = server.ipAddress;
        maxClients = server.maxClients;
        isOnline = server.isOnline;
        parameters = server.parameters;
        rcon = server.rcon;
        queryPort = server.queryPort;
    }

    public List<PlayerEntity> getPlayers()
    {
        return getMany(PlayerEntity.class, "game_server");
    }

} // GameServerEntity
