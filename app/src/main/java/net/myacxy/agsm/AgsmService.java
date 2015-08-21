package net.myacxy.agsm;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.annotation.Nullable;

import net.myacxy.agsm.interfaces.DatabaseManager;
import net.myacxy.agsm.interfaces.ServerManager;
import net.myacxy.agsm.managers.ActiveDatabaseManager;
import net.myacxy.agsm.managers.JgsqServerManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

@EService
public class AgsmService extends Service
{
    public static final String ACTION_UPDATE_SERVERS = "net.myacxy.agsm.action.ACTION_UPDATE_SERVERS";
    public static final String EXTRA_UPDATE_REASON = "net.myacxy.agsm.action.EXTRA_UPDATE_REASON";
    public static final int UPDATE_REASON_MANUAL = 0;
    public static final int UPDATE_REASON_PERIODIC = 1;

    @Bean(JgsqServerManager.class) ServerManager serverManager;
    @Bean(ActiveDatabaseManager.class) DatabaseManager databaseManager;

    private Looper looper;
    private ServiceHandler serviceHandler;

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("AgsmService", THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper = thread.getLooper();
        serviceHandler = new ServiceHandler(looper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message message = serviceHandler.obtainMessage();
        message.arg1 = startId;
        serviceHandler.sendMessage(message);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO update servers
            System.out.println("stopping service " + msg.arg1);
            stopSelf(msg.arg1);
        }
    }
}
