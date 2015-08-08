package net.myacxy.agsm.fragments;

import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.WindowManager;

import net.myacxy.agsm.R;
import net.myacxy.agsm.interfaces.DatabaseManager;
import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.interfaces.OnServerUpdatedListener;
import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.interfaces.ServerManager;
import net.myacxy.agsm.managers.ActiveDatabaseManager;
import net.myacxy.agsm.managers.JgsqServerManager;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.utils.JgsqGameFinder;
import net.myacxy.agsm.views.adapters.ServerFragmentPagerAdapter;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_server)
@OptionsMenu(R.menu.menu_server)
public class ServerFragment extends BaseToolbarFragment
{
    public static final String ARG_GAME_SERVER_ID = "game_server_id";

    @ViewById(R.id.viewpager)
    ViewPager viewPager;

    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @ViewById(R.id.fab)
    FloatingActionButton refreshButton;

    @FragmentArg
    int gameServerId;

    @Bean(JgsqServerManager.class)
    ServerManager serverManager;

    @Bean(JgsqGameFinder.class)
    GameFinder gameFinder;

    @Bean(ActiveServerFinder.class)
    ServerFinder serverFinder;

    @Bean(ActiveDatabaseManager.class)
    DatabaseManager databaseManager;

    private ServerOverviewFragment overviewFragment;
    private ServerDetailsFragment detailsFragment;
    private ServerRconFragment rconFragment;
    private ServerFragmentPagerAdapter adapter;

    @AfterViews
    void initialize()
    {
        super.initialize();

        setupViewPager(viewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        if (Build.VERSION.SDK_INT >= 99) // replace 21
        {
            // Set the status bar to dark-semi-transparentish
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // Set paddingTop of toolbar to height of status bar.
            // Fixes statusbar covers toolbar issue
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            toolbar.setMinimumHeight(toolbar.getHeight() + toolbar.getPaddingTop());
        }

    } // initialize

    private void setupViewPager(ViewPager viewPager)
    {
        overviewFragment = ServerOverviewFragment_
                .builder()
                .gameServerId(gameServerId)
                .build();

        detailsFragment = ServerDetailsFragment_
                .builder()
                .gameServerId(gameServerId)
                .build();

        rconFragment = ServerRconFragment_
                .builder()
                .gameServerId(gameServerId)
                .build();

        adapter = new ServerFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(overviewFragment, "Overview");
        adapter.addFragment(detailsFragment, "Details");
        adapter.addFragment(rconFragment, "RCON");
        viewPager.setAdapter(adapter);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Click(R.id.fab)
    void refresh()
    {
        GameServerEntity gameServerEntity = serverFinder.findById(gameServerId);

        Game game = gameFinder.find(gameServerEntity.game.name);

        serverManager.update(game, gameServerEntity.ipAddress, gameServerEntity.port, new OnServerUpdatedListener() {
            @Override
            public void onServerUpdated(GameServer gameServer) {
                databaseManager.update(gameServer);

                reinitialize();
            }
        });
    }

    @UiThread
    void reinitialize()
    {
//        overviewFragment.update();
//        detailsFragment.update();
//        rconFragment.update();
        
        adapter.notifyDataSetChanged();
    }

    @OptionsItem(android.R.id.home)
    boolean homeSelected(MenuItem item)
    {
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
} // ServerFragment
