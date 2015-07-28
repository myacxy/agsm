package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.view.ViewGroup;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.DatabaseServerFinder;
import net.myacxy.agsm.views.ServerCardView;
import net.myacxy.agsm.views.ServerCardView_;
import net.myacxy.agsm.views.ViewWrapper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class ServerCardAdapter extends RecyclerViewAdapterBase<GameServerEntity, ServerCardView> {

    @Bean(DatabaseServerFinder.class)
    ServerFinder serverFinder;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter()
    {
        items = serverFinder.findAll();
    }

    @Override
    protected ServerCardView onCreateItemView(ViewGroup parent, int viewType) {
        return ServerCardView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ServerCardView> holder, int position) {
        ServerCardView view = holder.getView();
        GameServerEntity server = items.get(position);

        view.bind(server);
    }
} // ServerCardAdapter
