package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.views.ItemServerDetailsParameterView;
import net.myacxy.agsm.views.ItemServerDetailsParameterView_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@EBean
public class ServerOverviewGeneralAdapter extends BaseAdapter
{
    private Map<String, String> map = new LinkedHashMap<>();
    private ArrayList<String> list = new ArrayList<>();
    private GameServerEntity gameServerEntity;

    private static int POSITION_LAST_UPDATE = -1;
    private static int TIME_INTERVAL = 1000;
    private Timer timer = new Timer();
    private ItemServerDetailsParameterView lastUpdateView;

    @Bean(ActiveServerFinder.class)
    ServerFinder serverFinder;

    @RootContext
    Context context;

    private int gameServerId;

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public String getItem(int position)
    {
        String key = list.get(position);
        String value = map.get(key);
        return value;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ItemServerDetailsParameterView item;

        if(convertView == null)
        {
            item = ItemServerDetailsParameterView_.build(context);
        }
        else
        {
            item = (ItemServerDetailsParameterView) convertView;
        }
        item.bind(list.get(position), getItem(position));

        if(position == POSITION_LAST_UPDATE)
        {
            lastUpdateView = item;
            setLastUpdate(lastUpdateView);
        }
        return item;
    } // getView


    void initAdapter()
    {
        gameServerEntity = serverFinder.findById(gameServerId);
        if(gameServerEntity != null)
        {
            POSITION_LAST_UPDATE = 0;
            map.put("Last update", "0s ago");
            map.put("IP address", gameServerEntity.ipAddress);
            map.put("Port", String.valueOf(gameServerEntity.port));
            map.put("Status", gameServerEntity.isOnline ? "online" : "offline");
            map.put("Ping", String.format("%d ms", gameServerEntity.ping));
            map.put("Game", gameServerEntity.game.name);
            map.put("Host name", gameServerEntity.hostName.trim());
            map.put("Map name", gameServerEntity.mapName);
            map.put("Maximum clients", String.valueOf(gameServerEntity.maxClients));

            list = new ArrayList<>(map.keySet());
        }
    } // initAdapter

    public void setGameServerId(int gameServerId)
    {
        this.gameServerId = gameServerId;
        initAdapter();
    }

    @UiThread
    protected void setLastUpdate(final ItemServerDetailsParameterView item)
    {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendHours().appendSuffix("h ")
                .appendMinutes().appendSuffix("m ")
                .printZeroAlways()
                .appendSeconds().appendSuffix("s")
                .appendLiteral(" ago")
                .toFormatter();

        Period period = new Period(gameServerEntity.lastUpdate, DateTime.now());
        period = period.normalizedStandard(PeriodType.time());

        item.bind("Last update", formatter.print(period));

        try
        {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setLastUpdate(item);
                }
            }, TIME_INTERVAL);
        }
        catch (IllegalStateException e)
        {
            // do nothing
        }
    }

    public void cancelUpdateTimer()
    {
        timer.cancel();
    }

    public void restartUpdateTimer()
    {
        timer = new Timer();
        getView(POSITION_LAST_UPDATE, lastUpdateView, null);
    }
} // ServerOverviewGeneralAdapter
