package net.myacxy.agsm.utils;

import android.support.annotation.Nullable;

import com.activeandroid.query.Select;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;

import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class DatabaseServerFinder implements ServerFinder
{
    @Override
    public List<GameServerEntity> findAll()
    {
        return new Select()
                .from(GameServerEntity.class)
                .execute();
    }

    @Override
    public List<GameServerEntity> findOnline()
    {
        return new Select()
                .from(GameServerEntity.class)
                .where("online = ?", true)
                .execute();
    }

    @Override
    public List<GameServerEntity> findOffline()
    {
        return new Select()
                .from(GameServerEntity.class)
                .where("online = ?", false)
                .execute();
    }
} // DatabaseServerFinder
