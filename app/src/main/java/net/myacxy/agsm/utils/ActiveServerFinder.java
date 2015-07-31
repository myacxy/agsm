package net.myacxy.agsm.utils;

import com.activeandroid.query.Select;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;

import org.androidannotations.annotations.EBean;

import java.util.List;

/***
 * ServerFinder for ActiveAndroid ORM database
 */
@EBean
public class ActiveServerFinder implements ServerFinder
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

    @Override
    public GameServerEntity getEntity(String ipAddress, int port)
    {
        return new Select()
                .from(GameServerEntity.class)
                .where("ip_address = ? AND port = ?", ipAddress, port)
                .executeSingle();
    }

    @Override
    public boolean isRegistered(String ipAddress, int port)
    {
        return getEntity(ipAddress, port) != null;
    }

} // ActiveServerFinder
