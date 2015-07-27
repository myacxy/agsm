package net.myacxy.agsm.utils;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;

import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class InDatabaseServerFinder implements ServerFinder
{
    @Override
    public List<GameServerEntity> findAll() {
        return GameServerEntity.listAll(GameServerEntity.class);
    }

    @Override
    public List<GameServerEntity> findOnline() {
        return GameServerEntity.find(GameServerEntity.class, "online = ?", "true");
    }

    @Override
    public List<GameServerEntity> findOffline() {
        return GameServerEntity.find(GameServerEntity.class, "online = ?", "false");
    }
}
