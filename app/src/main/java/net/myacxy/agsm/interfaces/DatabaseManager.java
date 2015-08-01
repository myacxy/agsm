package net.myacxy.agsm.interfaces;

import net.myacxy.agsm.models.GameEntity;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.models.PlayerEntity;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;
import net.myacxy.jgsq.models.Player;

public interface DatabaseManager
{
    GameEntity save(Game game);

    GameServerEntity save(GameServer gameServer);

    PlayerEntity save(Player player, GameServerEntity gameServerEntity);

    GameEntity update(Game game);

    PlayerEntity update(Player player);

    GameServerEntity update(GameServer gameServer);

    GameEntity getGameEntity(Game game);

    GameServerEntity getGameServerEntity(GameServer gameServer);
}
