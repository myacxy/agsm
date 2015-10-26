package net.myacxy.agsm.managers;

import net.myacxy.agsm.interfaces.EventManager;

import org.androidannotations.annotations.EBean;

import de.greenrobot.event.EventBus;

@EBean(scope = EBean.Scope.Singleton)
public class GreenEventManager implements EventManager
{
    private final EventBus eventBus;

    public GreenEventManager()
    {
        eventBus = EventBus.getDefault();
    }

    @Override
    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

}
