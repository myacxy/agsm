package net.myacxy.agsm.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.models.GameServerEntity;

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

    public ServerCardView(Context context)
    {
        super(context);
    }
    public ServerCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void bind(GameServerEntity gameServerEntity)
    {
        mGameServer = gameServerEntity;
        game.setText(gameServerEntity.game.alternativeName);
        title.setText(gameServerEntity.hostName);
        map.setText(gameServerEntity.mapName);
        playerCount.setText(String.format(
                "%d / %d",
                gameServerEntity.getPlayers().size(),
                gameServerEntity.maxClients));
    }

    public GameServerEntity getGameServer()
    {
        return mGameServer;
    }
} // ServerCardView
