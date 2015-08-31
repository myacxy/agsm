package net.myacxy.agsm.interfaces;

public interface ServerUpdateListener
{
    void onUpdateServer(long gameServerId);

    void onServerUpdated(long gameServerId);

    void onServersUpdated();
}
