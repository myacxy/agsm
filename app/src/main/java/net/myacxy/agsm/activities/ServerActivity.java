package net.myacxy.agsm.activities;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.myacxy.agsm.AgsmService;
import net.myacxy.agsm.R;
import net.myacxy.agsm.fragments.ServerDetailsFragment;
import net.myacxy.agsm.fragments.ServerDetailsFragment_;
import net.myacxy.agsm.fragments.ServerOverviewFragment;
import net.myacxy.agsm.fragments.ServerOverviewFragment_;
import net.myacxy.agsm.fragments.ServerRconFragment;
import net.myacxy.agsm.fragments.ServerRconFragment_;
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
import net.myacxy.jgsq.helpers.ServerResponseStatus;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_server)
@OptionsMenu(R.menu.menu_server)
public class ServerActivity extends AppCompatActivity
{
    public static final String ARG_GAME_SERVER_ID = "game_server_id";

    @ViewById(R.id.server_toolbar)      Toolbar toolbar;
    @ViewById(R.id.server_tabs)         TabLayout tabLayout;
    @ViewById(R.id.server_viewpager)    ViewPager viewPager;
    @ViewById(R.id.server_fab)          FloatingActionButton refreshButton;
    @Bean(JgsqServerManager.class)      ServerManager serverManager;
    @Bean(JgsqGameFinder.class)         GameFinder gameFinder;
    @Bean(ActiveServerFinder.class)     ServerFinder serverFinder;
    @Bean(ActiveDatabaseManager.class)  DatabaseManager databaseManager;
    @ViewById(R.id.server_backdrop)     ImageView backdrop;
    @ViewById(R.id.collapsing_toolbar)  CollapsingToolbarLayout collapsingToolbarLayout;

    private int gameServerId;
    private Game game;
    private GameServerEntity gameServerEntity;
    private ServerFragmentPagerAdapter adapter;

    @AfterViews
    void initialize()
    {
        gameServerId = getIntent().getExtras().getInt("game_server_id");
        gameServerEntity = serverFinder.findById(gameServerId);
        game = gameFinder.find(gameServerEntity.game.name);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(
                new IconicsDrawable(
                        this,
                        GoogleMaterial.Icon.gmd_arrow_back).color(Color.WHITE).sizeDp(18)
        );
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        collapsingToolbarLayout.setTitle(gameServerEntity.hostName.trim());

        setupViewPager(viewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        int drawableId = getResources().getIdentifier(
                "header_" + game.abbreviatedName.toLowerCase(),
                "drawable",
                getPackageName()
        );
        backdrop.setImageResource(drawableId);
    } // initialize

    private void setupViewPager(ViewPager viewPager)
    {
        ServerOverviewFragment overviewFragment = ServerOverviewFragment_
                .builder()
                .gameServerId(gameServerId)
                .build();

        ServerDetailsFragment detailsFragment = ServerDetailsFragment_
                .builder()
                .gameServerId(gameServerId)
                .build();

        ServerRconFragment rconFragment = ServerRconFragment_
                .builder()
                .gameServerId(gameServerId)
                .build();

        adapter = new ServerFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(overviewFragment, "Overview");
        adapter.addFragment(detailsFragment, "Details");
        adapter.addFragment(rconFragment, "RCON");
        viewPager.setAdapter(adapter);
    }


    @Click(R.id.server_fab)
    void refresh()
    {
        showProgress(refreshButton);

        serverManager.update(
                game,
                gameServerEntity.ipAddress,
                gameServerEntity.port,
                new OnServerUpdatedListener() {
                    @Override
                    public void onServerUpdated(GameServer gameServer) {
                        if (gameServer.getProtocol().getResponseStatus() == ServerResponseStatus.OK) {
                            databaseManager.update(gameServer);
                            reinitialize();
                        } else {
                            Snackbar.make(
                                    refreshButton,
                                    gameServer.getProtocol().getResponseStatus().toString(),
                                    Snackbar.LENGTH_SHORT
                            ).show();
                        }
                        hideProgress(refreshButton);
                    }
                } // OnServerUpdatedListener
        );
    }

    @UiThread
    protected void showProgress(FloatingActionButton fab)
    {
        fab.setIndeterminate(true);
        fab.setClickable(false);
    }

    @UiThread
    protected void hideProgress(FloatingActionButton fab)
    {
        fab.setIndeterminate(false);
        fab.setClickable(true);
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
        onBackPressed();
        return true;
    }

    @Receiver(actions = AgsmService.ACTION_ON_UPDATE_SERVERS)
    void onUpdateServers()
    {
        showProgress(refreshButton);
    }

    @Receiver(actions = AgsmService.ACTION_SERVERS_UPDATED)
    void onServersUpdated()
    {
        hideProgress(refreshButton);
        reinitialize();
    }
} // ServerFragment
