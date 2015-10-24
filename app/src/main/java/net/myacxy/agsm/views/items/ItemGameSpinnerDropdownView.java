package net.myacxy.agsm.views.items;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.myacxy.agsm.R;
import net.myacxy.jgsq.models.Game;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_game_spinner_dropdown)
public class ItemGameSpinnerDropdownView extends LinearLayout
{

    @ViewById(R.id.add_server_spinner_item_icon)
    ImageView icon;

    @ViewById(R.id.add_server_spinner_item_name)
    TextView name;

    public ItemGameSpinnerDropdownView(Context context) {
        super(context);
    }

    public void bind(Game game)
    {
        name.setText(game.name);
        icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_settings_black_24dp));
    }
}
