package net.myacxy.agsm.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewAdapterBase<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected List<T> items = new ArrayList<T>();

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int position);

    // additional methods to manipulate the items

    public void addItem(T item)
    {
        if(!items.contains(item)) items.add(item);
    }

    public void removeItem(T item)
    {
        if(items.contains(item)) items.remove(item);
    }

    public void removeItem(int location)
    {
        if(location >= 0 && location < items.size()) items.remove(location);
    }

}
