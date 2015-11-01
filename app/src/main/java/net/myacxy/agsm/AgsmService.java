package net.myacxy.agsm;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import net.myacxy.agsm.activities.MainActivity;
import net.myacxy.agsm.interfaces.DatabaseManager;
import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.interfaces.OnServerUpdatedListener;
import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.interfaces.ServerManager;
import net.myacxy.agsm.managers.ActiveDatabaseManager;
import net.myacxy.agsm.managers.JgsqServerManager;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.utils.AgsmKeys;
import net.myacxy.agsm.utils.JgsqGameFinder;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import java.util.List;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

@EService
public class AgsmService extends Service
{
    private Looper looper;
    private UpdateServersHandler serviceHandler;

    @Bean(JgsqServerManager.class)      ServerManager serverManager;
    @Bean(ActiveDatabaseManager.class)  DatabaseManager databaseManager;
    @Bean(ActiveServerFinder.class)     ServerFinder serverFinder;
    @Bean(JgsqGameFinder.class)         GameFinder gameFinder;

    @Override
    public void onCreate()
    {
        stopService(AgsmService_.intent(getApplication()).get());
        HandlerThread thread = new HandlerThread("AgsmService", THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper = thread.getLooper();
        serviceHandler = new UpdateServersHandler(looper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Message message = serviceHandler.obtainMessage();
        message.arg1 = startId;
        serviceHandler.sendMessage(message);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private class UpdateServersHandler extends Handler implements OnServerUpdatedListener
    {
        private List<GameServerEntity> gameServerEntities;
        private int count = 0;
        private int startId = -1;

        public UpdateServersHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
            startId = msg.arg1;
            gameServerEntities = serverFinder.findAll();
            if(gameServerEntities != null && gameServerEntities.size() != 0)
            {
                Intent intent = new Intent(AgsmKeys.Action.Server.ON_UPDATE_SERVERS);
                sendBroadcast(intent);

                count = gameServerEntities.size();
                for (GameServerEntity gameServerEntity : gameServerEntities)
                {
                    updateServer(gameServerEntity);
                }
            }
            else
            {
                count = 0;
                stopSelf(startId);
                getLooper().quit();
            }
        } // handleMessage

        private void updateServer(GameServerEntity gameServerEntity)
        {
            Intent intent = new Intent(AgsmKeys.Action.Server.ON_UPDATE_SERVER)
                    .putExtra(MainActivity.EXTRA_GAME_SERVER_ID, gameServerEntity.getId());
            sendBroadcast(intent);

            Game game = gameFinder.find(gameServerEntity.game.name);

            serverManager.update(
                    game,
                    gameServerEntity.ipAddress,
                    gameServerEntity.port,
                    this
            );
        } // updateServer

        @Override
        public void onServerUpdated(GameServer gameServer)
        {
            databaseManager.update(gameServer);

            long gameServerId = databaseManager.getGameServerEntity(gameServer).getId();
            Intent intent = new Intent(AgsmKeys.Action.Server.ON_SERVER_UPDATED)
                    .putExtra(MainActivity.EXTRA_GAME_SERVER_ID, gameServerId);
            sendBroadcast(intent);

            if(--count == 0)
            {
                onAllServerUpdated();
            }
        } // onServerUpdated

        private void onAllServerUpdated()
        {
            Intent intent = new Intent(AgsmKeys.Action.Server.ON_SERVERS_UPDATED);
            sendBroadcast(intent);

            stopSelf(startId);
            getLooper().quit();
        }
    } // UpdateServersHandler
} // AgsmService
