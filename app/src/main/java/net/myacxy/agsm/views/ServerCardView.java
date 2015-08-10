package net.myacxy.agsm.views;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.fragments.ServerFragment;
import net.myacxy.agsm.fragments.ServerFragment_;
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

    public ServerCardView(Context context)
    {
        super(context);
    }

    public ServerCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Click(R.id.server_card)
    void openServerFragment()
    {
        ServerFragment serverFragment = ServerFragment_
                .builder()
                .gameServerId(mGameServer.getId().intValue())
                .build();

        FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.main_content_layout, serverFragment, "server " + mGameServer.getId())
                .addToBackStack(null)
                .commit();
    }

    public void bind(GameServerEntity gameServerEntity)
    {
        mGameServer = gameServerEntity;
        game.setText(gameServerEntity.game.alternativeName);
        title.setText(gameServerEntity.hostName.trim());
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
