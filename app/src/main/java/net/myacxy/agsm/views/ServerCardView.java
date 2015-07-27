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
    @ViewById(R.id.server_card_title)
    TextView title;

    @ViewById(R.id.server_card_text)
    TextView text;

    public ServerCardView(Context context)
    {
        super(context);
    }
    public ServerCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void bind(GameServerEntity server)
    {
        title.setText(server.hostName);
        text.setText(server.mapName);
    }
}
