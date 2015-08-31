package net.myacxy.agsm;

import android.content.Intent;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import net.myacxy.agsm.activities.MainActivity;
import net.myacxy.agsm.activities.MainActivity_;
import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.models.PlayerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.Receiver;

import java.util.List;

@EService
public class AgsmDashClockExtension extends DashClockExtension
{
    @Bean(ActiveServerFinder.class) ServerFinder serverFinder;

    @Override
    protected void onUpdateData(int reason)
    {
        List<GameServerEntity> gameServerEntities = serverFinder.findAll();
        boolean visible = gameServerEntities.size() > 0;
        String status;
        String expandedTitle;
        StringBuilder expandedBody = new StringBuilder();
        Intent clickIntent = new Intent(this, MainActivity_.class);

        int totalPlayerCount = 0;
        int totalBotCount = 0;

        for (GameServerEntity gameServerEntity : gameServerEntities)
        {
            int playerCount = 0;
            int botCount = 0;
            List<PlayerEntity> playerEntities = gameServerEntity.getPlayers();
            for (PlayerEntity playerEntity : playerEntities)
            {
                if(playerEntity.ping > 0) playerCount++;
                else botCount++;
            }

            totalPlayerCount += playerCount;
            totalBotCount += botCount;

            // v1.03 | 0(0)/16
            if(botCount > 0)
            {
                expandedBody.append(
                        String.format(
                                "%s | %d (%d) / %d",
                                gameServerEntity.hostName.trim(),
                                playerCount,
                                botCount,
                                gameServerEntity.maxClients)
                );
            }
            else
            {
                expandedBody.append(
                        String.format(
                                "%s | %d / %d",
                                gameServerEntity.hostName.trim(),
                                playerCount,
                                gameServerEntity.maxClients)
                );
            }


            if(gameServerEntities.indexOf(gameServerEntity) != gameServerEntities.size() - 1)
            {
                expandedBody.append("\n");
            }
        }

//        visible = totalPlayerCount > 0;
        status = String.valueOf(totalPlayerCount);

        expandedTitle = String.format(
                "%d %s online",
                totalPlayerCount,
                totalPlayerCount == 1 ? "player" : "players"
        );

        publishUpdate(new ExtensionData()
                .visible(visible)
                .icon(android.R.drawable.sym_def_app_icon)
                .status(status)
                .expandedTitle(expandedTitle)
                .expandedBody(expandedBody.toString())
                .clickIntent(clickIntent)
        );
    } // onUpdateData

    @Receiver(actions = {
            MainActivity.ACTION_ON_SERVER_ADDED,
            MainActivity.ACTION_ON_SERVER_UPDATED,
            MainActivity.ACTION_ON_SERVERS_UPDATED,
            MainActivity.ACTION_ON_SERVER_REMOVED } )
    void onServersUpdated()
    {
        onUpdateData(DashClockExtension.UPDATE_REASON_MANUAL);
    }
} // AgsmDashClockExtension
