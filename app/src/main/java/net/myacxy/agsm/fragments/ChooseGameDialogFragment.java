package net.myacxy.agsm.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.events.GameSelectedEvent;
import net.myacxy.agsm.misc.DividerItemDecoration;
import net.myacxy.agsm.views.adapters.GameChooserAdapter;
import net.myacxy.jgsq.models.Game;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.solovyev.android.views.llm.LinearLayoutManager;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.dialog_choose_game)
public class ChooseGameDialogFragment extends DialogFragment
{
    @ViewById(R.id.rv_dcg_games)    protected RecyclerView gamesList;
    @Bean(GameChooserAdapter.class) protected GameChooserAdapter gamesAdapter;

    @AfterViews
    protected void initialize()
    {
        gamesList.setLayoutManager(new LinearLayoutManager(getContext()));
        gamesList.setItemAnimator(new DefaultItemAnimator());
        gamesList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        gamesAdapter.initAdapter(new GameChooserAdapter.GameChooserListener() {
            @Override
            public void onItemClicked(final Game game) {

                EventBus.getDefault().post(new GameSelectedEvent(game));
                dismiss();
            }
        });
        gamesList.setAdapter(gamesAdapter);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(STYLE_NO_TITLE);
        return dialog;
    }
}
