package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.ViewGroup;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.views.ItemServerDetailsParameterView;
import net.myacxy.agsm.views.ItemServerDetailsParameterView_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Map;

@EBean
public class ServerDetailsParameterAdapter
        extends RecyclerViewAdapterBase<Pair<String, String>, ItemServerDetailsParameterView>
{
    @Bean(ActiveServerFinder.class)
    ServerFinder serverFinder;

    private int gameServerId;

    @RootContext
    Context context;

    void initAdapter()
    {
        GameServerEntity gameServerEntity = serverFinder.findById(gameServerId);
        if(gameServerEntity != null)
        {
            for (Map.Entry entry : gameServerEntity.parameters.entrySet())
            {
                items.add(new Pair<>(entry.getKey().toString(), entry.getValue().toString()));
            }

        }
    } // initAdapter

    @Override
    protected ItemServerDetailsParameterView onCreateItemView(ViewGroup parent, int viewType)
    {
        return ItemServerDetailsParameterView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ItemServerDetailsParameterView> holder, int position)
    {
        ItemServerDetailsParameterView view = holder.getView();
        Pair<String, String> pair = items.get(position);

        view.bind(pair.first, pair.second);
    }

    public void setGameServerId(int gameServerId)
    {
        this.gameServerId = gameServerId;
        initAdapter();
    }
} // ServerCardAdapter
