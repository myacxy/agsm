package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.utils.JgsqGameFinder;
import net.myacxy.agsm.views.items.ItemGameChooserView;
import net.myacxy.agsm.views.items.ItemGameChooserView_;
import net.myacxy.jgsq.models.Game;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Map;

@EBean
public class GameChooserAdapter extends RecyclerViewAdapterBase<Game, ItemGameChooserView>
{
    @Bean(JgsqGameFinder.class) protected GameFinder gameFinder;
    @RootContext                protected Context context;

    private GameChooserListener listener;

    public void initAdapter(@NonNull GameChooserListener listener)
    {
        this.listener = listener;
        Map<String, Game> games = gameFinder.findAll();
        if(games != null)
        {
            for (Game game : games.values())
            {
                items.add(game);
            }
        }
    } // initAdapter

    @Override
    protected ItemGameChooserView onCreateItemView(ViewGroup parent, int viewType)
    {
        return ItemGameChooserView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ItemGameChooserView> holder, int position)
    {
        ItemGameChooserView view = holder.getView();
        final Game game = items.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(game);
            }
        });

        view.bind(game);
    }

    public interface GameChooserListener
    {
        void onItemClicked(Game game);
    }
} // GameChooserAdapter
