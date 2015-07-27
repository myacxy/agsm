package net.myacxy.agsm.models;

import com.orm.SugarRecord;

import net.myacxy.jgsq.model.Game;

public class GameEntity extends SugarRecord<GameEntity>
{
    int defaultPort;
    String abbreviatedName;
    String alternativeName;
    String name;
    String serverProtocolType;

    public GameEntity() {}

    public GameEntity(Game game)
    {
        defaultPort = game.defaultPort;
        abbreviatedName = game.abbreviatedName;
        alternativeName = game.alternativeName;
        name = game.name;
        serverProtocolType = game.serverProtocolType.toString();
    }
} // GameEntity
