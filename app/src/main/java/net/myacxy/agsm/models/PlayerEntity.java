package net.myacxy.agsm.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import net.myacxy.jgsq.model.Player;

@Table(name = "players")
public class PlayerEntity extends Model
{
    @Column(name = "colored_name")
    public String coloredName;
    @Column(name = "deaths")
    public int deaths;
    @Column(name = "name")
    public String name;
    @Column(name = "ping")
    public int ping;
    @Column(name = "score")
    public int score;
    @Column(name = "time")
    public int time;
    @Column(name = "game_server")
    public GameServerEntity gameServer;

    public PlayerEntity()
    {
        super();
    }

    public PlayerEntity(Player player)
    {
        super();
        coloredName = player.coloredName;
        deaths = player.deaths;
        name = player.name;
        ping = player.ping;
        score = player.score;
        time = player.time;
    }
} // PlayerEntity
