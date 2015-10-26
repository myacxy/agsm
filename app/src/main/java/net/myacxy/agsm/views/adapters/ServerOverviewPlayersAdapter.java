package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.models.PlayerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.views.items.ItemServerOverviewPlayerView;
import net.myacxy.agsm.views.items.ItemServerOverviewPlayerView_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@EBean
public class ServerOverviewPlayersAdapter extends BaseAdapter
{
    //<editor-fold desc="Members">
    private List<PlayerEntity> clients = new ArrayList<>();
    private long gameServerId;

    @Bean(ActiveServerFinder.class) ServerFinder serverFinder;
    @RootContext                    Context context;
    //</editor-fold>

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public PlayerEntity getItem(int position)
    {
        return clients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ItemServerOverviewPlayerView item;

        if(convertView == null)
        {
            item = ItemServerOverviewPlayerView_.build(context);
        }
        else
        {
            item = (ItemServerOverviewPlayerView) convertView;
        }

        item.bind(clients.get(position));

        return item;
    } // getView


    void initAdapter()
    {
        GameServerEntity gameServerEntity = serverFinder.findById(gameServerId);
        if(gameServerEntity != null)
        {
            clients = gameServerEntity.getClients();
            Collections.sort(clients, new Comparator<PlayerEntity>()
            {
                @Override
                public int compare(PlayerEntity lhs, PlayerEntity rhs)
                {
                    if(lhs.score > rhs.score) return -1;
                    else if(lhs.score < rhs.score) return 1;
                    return 0;
                }
            });
        }
    } // initAdapter

    public void setGameServerId(long gameServerId)
    {
        this.gameServerId = gameServerId;
        initAdapter();
    }
} // ServerOverviewGeneralAdapter
