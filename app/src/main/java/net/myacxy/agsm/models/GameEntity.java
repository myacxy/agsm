package net.myacxy.agsm.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import net.myacxy.jgsq.model.Game;

@Table(name = "games")
public class GameEntity extends Model
{
    @Column(name = "default_port")
    public int defaultPort;
    @Column(name = "abbreviated_name")
    public String abbreviatedName;
    @Column(name = "alternative_name")
    public String alternativeName;
    @Column(name = "name", unique = true)
    public String name;
    @Column(name = "server_protocol_type")
    public String serverProtocolType;

    public GameEntity() {
        super();
    }

    public GameEntity(Game game)
    {
        super();
        defaultPort = game.defaultPort;
        abbreviatedName = game.abbreviatedName;
        alternativeName = game.alternativeName;
        name = game.name;
        serverProtocolType = game.serverProtocolType.toString();
    }
} // GameEntity
