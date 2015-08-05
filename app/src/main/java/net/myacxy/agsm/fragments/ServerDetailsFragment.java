package net.myacxy.agsm.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.views.adapters.ServerDetailsParameterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_server_details)
public class ServerDetailsFragment extends Fragment
{
    @ViewById(R.id.server_details_list)
    RecyclerView detailsList;

    @Bean(ServerDetailsParameterAdapter.class)
    ServerDetailsParameterAdapter serverDetailsParameterAdapter;

    @FragmentArg
    int gameServerId;

    @AfterViews
    void initialize()
    {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);

        detailsList.setLayoutManager(layoutManager);

        serverDetailsParameterAdapter.setGameServerId(gameServerId);
        detailsList.setAdapter(serverDetailsParameterAdapter);
    }
} // ServerDetailsFragment
