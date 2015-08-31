package net.myacxy.agsm.views;

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
public class ServerCardView extends LinearLayout
{
    private GameServerEntity mGameServerEntity;

    @ViewById(R.id.server_card_title)           TextView title;
    @ViewById(R.id.server_card_status)          TextView status;
    @ViewById(R.id.server_card_ping)            TextView ping;
    @ViewById(R.id.server_card_game)            TextView game;
    @ViewById(R.id.server_card_map)             TextView map;
    @ViewById(R.id.server_card_player_count)    TextView playerCount;
    @ViewById(R.id.server_card_toolbar)         Toolbar toolbar;
    @ViewById(R.id.server_card_progress)        ProgressBar progressBar;
    @ViewById(R.id.server_card_divider)         View divider;

    private Context context;

    public ServerCardView(Context context)
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
                switch (item.getItemId())
                {
                    case R.id.action_server_card_remove:
                        Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @Click(R.id.server_card)
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

        title.setText(mGameServerEntity.hostName.trim());
        status.setText(mGameServerEntity.isOnline ? "online" : "offline");
        ping.setText(String.format("%d ms", mGameServerEntity.ping));
        game.setText(mGameServerEntity.game.alternativeName);
        map.setText(mGameServerEntity.mapName);
        playerCount.setText(
                getPlayerCountText(
                        mGameServerEntity.getPlayers(),
                        mGameServerEntity.maxClients)
        );
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
        divider.setVisibility(View.GONE);
    }

    public void hideProgress()
    {
        progressBar.setVisibility(View.GONE);
        divider.setVisibility(VISIBLE);
    }

    @Override
    public String toString() {
        return String.format("ServerCardView %d", mGameServerEntity.getId());
    }
} // ServerCardView
