package net.myacxy.agsm.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;


import net.myacxy.agsm.R;
import net.myacxy.agsm.interfaces.AddServerListener;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.views.adapters.ServerCardAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_home)
@OptionsMenu(R.menu.menu_home)
public class HomeFragment extends BaseToolbarFragment implements AddServerListener
{
    @ViewById(R.id.home_recycler_view)
    RecyclerView recyclerView;
    private GridLayoutManager layoutManager;

    @ViewById(R.id.fab)
    FloatingActionButton addServerButton;

    @Bean
    ServerCardAdapter serverCardAdapter;

    @AfterViews
    void setupViews()
    {
        layoutManager = new GridLayoutManager(getActivity(), 1);

        recyclerView.setLayoutManager(layoutManager);
    }

    @AfterViews
    void bindAdapter()
    {
        recyclerView.setAdapter(serverCardAdapter);
    }

    @AfterViews
    void initialize()
    {
        super.initialize();
    }

    @OptionsItem(R.id.action_example)
    void actionExampleSelected(MenuItem item)
    {
        Toast.makeText(getActivity().getApplicationContext(), "example action " + item.toString(), Toast.LENGTH_SHORT).show();
    }

    @OptionsItem(android.R.id.home)
    boolean homeSelected(MenuItem item)
    {
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    @Click(R.id.fab)
    void showAddServerDialog()
    {
        AddServerFragment_ fragment = new AddServerFragment_();
        fragment.setAddServerListener(this);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_content_layout, fragment, "add_server")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onServerAdded(GameServerEntity server)
    {
        serverCardAdapter.addItem(server);
    }
} // ServerOverviewFragment
