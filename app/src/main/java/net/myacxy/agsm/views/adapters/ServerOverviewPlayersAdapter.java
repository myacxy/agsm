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
    private List<PlayerEntity> players = new ArrayList<>();

    @Bean(ActiveServerFinder.class)
    ServerFinder serverFinder;

    @RootContext
    Context context;

    private long gameServerId;

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public PlayerEntity getItem(int position)
    {
        return players.get(position);
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

        item.bind(players.get(position));

        return item;
    } // getView


    void initAdapter()
    {
        GameServerEntity gameServerEntity = serverFinder.findById(gameServerId);
        if(gameServerEntity != null)
        {
            players = gameServerEntity.getPlayers();
            Collections.sort(players, new Comparator<PlayerEntity>()
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
