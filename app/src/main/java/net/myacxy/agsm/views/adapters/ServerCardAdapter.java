package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.view.ViewGroup;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.views.ServerCardView;
import net.myacxy.agsm.views.ServerCardView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class ServerCardAdapter extends RecyclerViewAdapterBase<GameServerEntity, ServerCardView>
{
    private List<ServerCardView> views = new ArrayList<>();

    @Bean(ActiveServerFinder.class) ServerFinder serverFinder;
    @RootContext                    Context context;

    @AfterInject
    void initAdapter()
    {
        items = serverFinder.findAll();
    }

    @Override
    protected ServerCardView onCreateItemView(ViewGroup parent, int viewType)
    {
        return ServerCardView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ServerCardView> holder, int position)
    {
        ServerCardView view = holder.getView();
        GameServerEntity server = items.get(position);
        view.bind(server);

        ensureNoDuplicates(view);
        views.add(view);
    }

    public void showProgress(long gameServerId)
    {
        ServerCardView view = getView(gameServerId);
        if (view != null)
        {
            view.showProgress();
        }
    }

    public void hideProgress(long gameServerId)
    {
        ServerCardView view = getView(gameServerId);
        if (view != null)
        {
            view.hideProgress();
        }
    }

    private ServerCardView getView(long gameServerId)
    {
        for (ServerCardView view : views)
        {
            if(view.getGameServer().getId() == gameServerId)
            {
                return view;
            }
        }
        return null;
    }

    private void ensureNoDuplicates(ServerCardView newView)
    {
        List<ServerCardView> duplicateViews = new ArrayList<>();

        for (ServerCardView oldView : views)
        {
            if(newView.getGameServer().getId() == oldView.getGameServer().getId())
            {
                duplicateViews.add(oldView);
            }
        }

        views.removeAll(duplicateViews);
    }
} // ServerCardAdapter
