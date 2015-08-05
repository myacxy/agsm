package net.myacxy.agsm.utils;

import com.activeandroid.query.Select;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;

import org.androidannotations.annotations.EBean;

import java.util.List;

/***
 * ServerFinder for ActiveAndroid ORM database
 */
@EBean(scope = EBean.Scope.Singleton)
public class ActiveServerFinder implements ServerFinder
{
    @Override
    public List<GameServerEntity> findAll()
    {
        return new Select()
                .all()
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
    public GameServerEntity findByAddress(String ipAddress, int port)
    {
        return new Select()
                .from(GameServerEntity.class)
                .where("ip_address = ? AND port = ?", ipAddress, port)
                .executeSingle();
    }

    @Override
    public GameServerEntity findById(int id)
    {
        return new Select()
                .from(GameServerEntity.class)
                .where("Id = ?", id)
                .executeSingle();
    }

    @Override
    public boolean isRegistered(String ipAddress, int port)
    {
        return findByAddress(ipAddress, port) != null;
    }
} // ActiveServerFinder
