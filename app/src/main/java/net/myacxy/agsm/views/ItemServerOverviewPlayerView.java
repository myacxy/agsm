package net.myacxy.agsm.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.models.PlayerEntity;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_server_overview_player)
public class ItemServerOverviewPlayerView extends LinearLayout
{

    @ViewById(R.id.server_overview_player_name)
    TextView nameText;

    @ViewById(R.id.server_overview_player_score)
    TextView scoreText;

    @ViewById(R.id.server_overview_player_ping)
    TextView pingText;

    public ItemServerOverviewPlayerView(Context context) {
        super(context);
    }

    public void bind(PlayerEntity playerEntity)
    {
        nameText.setText(playerEntity.name);
        scoreText.setText(String.valueOf(playerEntity.score));
        pingText.setText(String.valueOf(playerEntity.ping));
    }
} // ItemServerDetailsParameterView
