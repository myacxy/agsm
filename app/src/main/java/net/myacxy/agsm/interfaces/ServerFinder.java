package net.myacxy.agsm.interfaces;

import net.myacxy.agsm.models.GameServerEntity;

import java.util.List;

public interface ServerFinder
{
    List<GameServerEntity> findAll();

    List<GameServerEntity> findOnline();

    List<GameServerEntity> findOffline();
}
