package net.myacxy.agsm.views.items;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.myacxy.agsm.R;
import net.myacxy.agsm.activities.ServerActivity_;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.models.PlayerEntity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EViewGroup(R.layout.item_server_card)
public class ItemServerCardView extends LinearLayout
{
    private GameServerEntity mGameServerEntity;

    @ViewById(R.id.tv_sc_title)           TextView title;
    @ViewById(R.id.tv_sc_status)          TextView status;
    @ViewById(R.id.tv_sc_ping)            TextView ping;
    @ViewById(R.id.tv_sc_game)            TextView game;
    @ViewById(R.id.tv_sc_map)             TextView map;
    @ViewById(R.id.tv_sc_player_count)    TextView playerCount;
    @ViewById(R.id.tb_server_card)        Toolbar toolbar;
    @ViewById(R.id.pb_sc_progress)        ProgressBar progressBar;
    @ViewById(R.id.divider)               View divider;

    private Context context;

    public ItemServerCardView(Context context)
    {
        super(context);
        this.context = context;
    }

    @AfterViews
    protected void setupMenu()
    {
        toolbar.inflateMenu(R.menu.menu_server_card);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_server_card_remove:
                        Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @Click(R.id.mrl_server_card)
    void onClickServerCard()
    {
        Intent intent = ServerActivity_
                .intent(context)
                .gameServerId(mGameServerEntity.getId())
                .get();

        context.startActivity(intent);
    }

    public void bind(GameServerEntity gameServerEntity)
    {
        mGameServerEntity = gameServerEntity;

        if(mGameServerEntity.isOnline)
        {
            title.setText(mGameServerEntity.hostName.trim());
            status.setText("online");
            ping.setText(String.format("%d ms", mGameServerEntity.ping));
            map.setText(mGameServerEntity.mapName);
            playerCount.setText(
                    getPlayerCountText(
                            mGameServerEntity.getPlayers(),
                            mGameServerEntity.maxClients)
            );
        }
        else
        {
            title.setText(mGameServerEntity.ipAddress + ":" + mGameServerEntity.port);
            status.setText("offline");
            ping.setText("\u2014");
            map.setText("\u2014");
            playerCount.setText("\u2014");
        }
        game.setText(mGameServerEntity.game.alternativeName);
    }

    protected String getPlayerCountText(List<PlayerEntity> playerEntities, int maxClients)
    {
        int numberOfPlayers = 0;
        int numberOfBots = 0;

        for (PlayerEntity playerEntity : playerEntities)
        {
            if(playerEntity.ping > 0) numberOfPlayers++;
            else numberOfBots++;
        }

        if(numberOfBots > 0)
        {
            return String.format(
                    "%d (%d) / %d",
                    numberOfPlayers,
                    numberOfBots,
                    maxClients);
        }

        return String.format(
                "%d / %d",
                playerEntities.size(),
                maxClients);
    }

    public GameServerEntity getGameServer()
    {
        return mGameServerEntity;
    }

    public void showProgress()
    {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress()
    {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public String toString() {
        return String.format("ServerCardView %d", mGameServerEntity.getId());
    }
} // ServerCardView