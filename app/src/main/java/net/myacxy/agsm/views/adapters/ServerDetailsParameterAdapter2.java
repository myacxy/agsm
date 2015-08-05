package net.myacxy.agsm.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.views.ItemServerDetailsParameterView;
import net.myacxy.agsm.views.ItemServerDetailsParameterView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EBean
public class ServerDetailsParameterAdapter2 extends BaseAdapter
{
    private Map<String, String> parametersMap = new HashMap<>();
    private ArrayList<String> parametersList = new ArrayList<>();

    @Bean(ActiveServerFinder.class)
    ServerFinder serverFinder;

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return parametersMap.size();
    }

    @Override
    public String getItem(int position)
    {
        String key = parametersList.get(position);
        String value = parametersMap.get(key);
        return value;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ItemServerDetailsParameterView item;

        if(convertView == null)
        {
            item = ItemServerDetailsParameterView_.build(context);
        }
        else
        {
            item = (ItemServerDetailsParameterView) convertView;
        }
        item.bind(parametersList.get(position), getItem(position));
        return item;
    } // getView


    @AfterInject
    void initAdapter()
    {
        GameServerEntity gameServerEntity = serverFinder.findById(1);
        if(gameServerEntity != null)
        {
            parametersMap = gameServerEntity.parameters;
            parametersList = new ArrayList<>(parametersMap.keySet());
        }
    } // initAdapter
} // ServerDetailsParameterAdapter2
