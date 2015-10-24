package net.myacxy.agsm.views.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.utils.GameIconHelper;
import net.myacxy.jgsq.models.Game;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_dialog_choose_game)
public class ItemGameChooserView extends RelativeLayout
{
    @ViewById(R.id.iv_gdc_item) protected ImageView gameIcon;
    @ViewById(R.id.tv_gdc_item) protected TextView gameTitle;

    public ItemGameChooserView(Context context)
    {
        super(context);
    }

    public void bind(Game game)
    {
        gameIcon.setImageDrawable(GameIconHelper.getDrawable24(getContext(), game.name));
        gameTitle.setText(game.name);
    }
} // ItemServerDetailsParameterView
