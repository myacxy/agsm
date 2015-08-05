package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.utils.JsonGameFinder;
import net.myacxy.agsm.views.GameSpinnerDropdownItemView;
import net.myacxy.agsm.views.GameSpinnerDropdownItemView_;
import net.myacxy.agsm.views.ItemGameSpinnerView;
import net.myacxy.agsm.views.ItemGameSpinnerView_;
import net.myacxy.jgsq.models.Game;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EBean
public class GameSpinnerAdapter extends BaseAdapter
{
    private Map<String, Game> gamesMap = new HashMap<>();
    private ArrayList<Game> gamesList = new ArrayList<>();

    @Bean(JsonGameFinder.class)
    GameFinder gameFinder;

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return gamesMap.size();
    }

    @Override
    public Game getItem(int position)
    {
        return gamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return createDropDownItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent)
    {
        ItemGameSpinnerView item;

        if(convertView == null)
        {
            item = ItemGameSpinnerView_.build(context);
        }
        else
        {
            item = (ItemGameSpinnerView) convertView;
        }
        item.bind(getItem(position));
        return item;
    }

    private View createDropDownItemView(int position, View convertView, ViewGroup parent)
    {
        GameSpinnerDropdownItemView item;

        if(convertView == null)
        {
            item = GameSpinnerDropdownItemView_.build(context);
        }
        else
        {
            item = (GameSpinnerDropdownItemView) convertView;
        }
        item.bind(getItem(position));
        return item;
    }

    @AfterInject
    void initAdapter()
    {
        gameFinder.setConfig("games.conf.json");
        gamesMap = gameFinder.findAll();

        gamesList = new ArrayList<>(gamesMap.values());
    }

    //<editor-fold desc="add and remove item">
    public void addGame(Game game)
    {
        if(!gamesMap.containsValue(game))
        {
            gamesMap.put(game.name, game);
        }
    } // addGame

    public void addGame(String name)
    {
        if(!gamesMap.containsKey(name))
        {
            Game game = gameFinder.get(name);
            if(game != null)
            {
                addGame(game);
            }
        }
    } // addGame

    public void removeGame(Game game)
    {
        if(gamesMap.containsValue(game))
        {
            gamesMap.remove(game.name);
        }
    } // removeGame


    public void removeGame(String name)
    {
        if(gamesMap.containsKey(name))
        {
            gamesMap.remove(name);
        }
        else
        {
            Game game = gameFinder.get(name);
            if(game != null)
            {
                removeGame(game);
            }
        }
    } // removeGame
    //</editor-fold>
} // GameSpinnerAdapter
