package net.myacxy.agsm.interfaces;

public interface EventManager
{
    void register(Object subscriber);

    void unregister(Object subscriber);
}
