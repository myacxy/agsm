package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.view.ViewGroup;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.views.items.ItemServerCardView;
import net.myacxy.agsm.views.items.ItemServerCardView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class ServerCardAdapter extends RecyclerViewAdapterBase<GameServerEntity, ItemServerCardView>
{
    private List<ItemServerCardView> views = new ArrayList<>();

    @Bean(ActiveServerFinder.class) ServerFinder serverFinder;
    @RootContext                    Context context;

    @AfterInject
    void initAdapter()
    {
        items = serverFinder.findAll();
    }

    @Override
    protected ItemServerCardView onCreateItemView(ViewGroup parent, int viewType)
    {
        return ItemServerCardView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ItemServerCardView> holder, int position)
    {
        ItemServerCardView view = holder.getView();
        GameServerEntity server = items.get(position);
        view.bind(server);

        ensureNoDuplicates(view);
        views.add(view);
    }

    public void showProgress(long gameServerId)
    {
        ItemServerCardView view = getView(gameServerId);
        if (view != null)
        {
            view.showProgress();
        }
    }

    public void hideProgress(long gameServerId)
    {
        ItemServerCardView view = getView(gameServerId);
        if (view != null)
        {
            view.hideProgress();
        }
    }

    private ItemServerCardView getView(long gameServerId)
    {
        for (ItemServerCardView view : views)
        {
            if(view.getGameServer().getId() == gameServerId)
            {
                return view;
            }
        }
        return null;
    }

    private void ensureNoDuplicates(ItemServerCardView newView)
    {
        List<ItemServerCardView> duplicateViews = new ArrayList<>();

        for (ItemServerCardView oldView : views)
        {
            if(newView.getGameServer().getId().equals(oldView.getGameServer().getId()))
            {
                duplicateViews.add(oldView);
            }
        }

        views.removeAll(duplicateViews);
    }
} // ServerCardAdapter
