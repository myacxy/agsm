package net.myacxy.agsm.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.activities.MainActivity;
import net.myacxy.agsm.activities.ServerActivity_;
import net.myacxy.agsm.models.GameServerEntity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_server_card)
public class ServerCardView extends CardView
{
    private GameServerEntity mGameServer;

    @ViewById(R.id.server_card_game)
    TextView game;

    @ViewById(R.id.server_card_title)
    TextView title;

    @ViewById(R.id.server_card_map)
    TextView map;

    @ViewById(R.id.server_card_player_count)
    TextView playerCount;

    private Context context;

    public ServerCardView(Context context)
    {
        super(context);
        this.context = context;
    }
    public ServerCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
    }

    @Click(R.id.server_card)
    void startServerActivity()
    {
        Intent intent = ServerActivity_.intent(context)
                .extra(MainActivity.EXTRA_GAME_SERVER_ID, mGameServer.getId().intValue())
                .get();

        context.startActivity(intent);
    }

    public void bind(GameServerEntity gameServerEntity)
    {
        mGameServer = gameServerEntity;
        game.setText(gameServerEntity.game.alternativeName);
        title.setText(gameServerEntity.hostName.trim());
        map.setText(gameServerEntity.mapName);
        playerCount.setText(
                String.format(
                        "%d / %d",
                        gameServerEntity.getPlayers().size(),
                        gameServerEntity.maxClients)
        );
    }

    public GameServerEntity getGameServer()
    {
        return mGameServer;
    }
} // ServerCardView
