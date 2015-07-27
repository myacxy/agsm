package net.myacxy.agsm.models;

import com.orm.SugarRecord;

import net.myacxy.jgsq.model.Player;

public class PlayerEntity extends SugarRecord<PlayerEntity>
{
    public String coloredName;
    public int deaths;
    public String name;
    public int ping;
    public int score;
    public int time;

    public PlayerEntity() {}

    public PlayerEntity(Player player)
    {
        coloredName = player.coloredName;
        deaths = player.deaths;
        name = player.name;
        ping = player.ping;
        score = player.score;
        time = player.time;
    }
} // PlayerEntity
