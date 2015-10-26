package net.myacxy.agsm.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.interfaces.ServerManager;
import net.myacxy.agsm.managers.JgsqServerManager;
import net.myacxy.agsm.views.adapters.ServerDetailsParameterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_server_details)
public class ServerDetailsFragment extends Fragment
{
    @ViewById(R.id.rv_server_details)           RecyclerView detailsList;
    @Bean(ServerDetailsParameterAdapter.class)  ServerDetailsParameterAdapter serverDetailsParameterAdapter;
    @Bean(JgsqServerManager.class)              ServerManager serverManager;
    @FragmentArg                                long gameServerId;

    @AfterViews
    void initialize()
    {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);

        detailsList.setLayoutManager(layoutManager);

        serverDetailsParameterAdapter.setGameServerId(gameServerId);
        detailsList.setAdapter(serverDetailsParameterAdapter);

    }

    @UiThread
    public void update()
    {
        serverDetailsParameterAdapter.notifyDataSetChanged();
    }
} // ServerDetailsFragment
