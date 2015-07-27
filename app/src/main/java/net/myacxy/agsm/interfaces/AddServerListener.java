package net.myacxy.agsm.interfaces;

import net.myacxy.agsm.models.GameServerEntity;

public interface AddServerListener
{
    void onServerAdded(GameServerEntity server);
}