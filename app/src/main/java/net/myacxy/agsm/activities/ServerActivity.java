package net.myacxy.agsm.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.myacxy.agsm.R;
import net.myacxy.agsm.fragments.ServerDetailsFragment;
import net.myacxy.agsm.fragments.ServerDetailsFragment_;
import net.myacxy.agsm.fragments.ServerOverviewFragment;
import net.myacxy.agsm.fragments.ServerOverviewFragment_;
import net.myacxy.agsm.fragments.ServerRconFragment;
import net.myacxy.agsm.fragments.ServerRconFragment_;
import net.myacxy.agsm.interfaces.DatabaseManager;
import net.myacxy.agsm.interfaces.EventManager;
import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.interfaces.OnServerUpdatedListener;
import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.interfaces.ServerManager;
import net.myacxy.agsm.managers.ActiveDatabaseManager;
import net.myacxy.agsm.managers.GreenEventManager;
import net.myacxy.agsm.managers.JgsqServerManager;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.utils.AgsmKeys;
import net.myacxy.agsm.utils.JgsqGameFinder;
import net.myacxy.agsm.views.adapters.ServerFragmentPagerAdapter;
import net.myacxy.jgsq.helpers.ServerResponseStatus;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_server)
@OptionsMenu(R.menu.menu_server)
public class ServerActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener
{
    @ViewById(R.id.tb_server)           Toolbar toolbar;
    @ViewById(R.id.tl_server)           TabLayout tabLayout;
    @ViewById(R.id.vp_server)           ViewPager viewPager;
    @ViewById(R.id.srl_server)          SwipeRefreshLayout swipeContainer;
    @ViewById(R.id.iv_server_backdrop)  ImageView backdrop;
    @ViewById(R.id.ctl_server)          CollapsingToolbarLayout collapsingToolbarLayout;
    @ViewById(R.id.abl_server)          AppBarLayout appBarLayout;
    @Bean(JgsqServerManager.class)      ServerManager serverManager;
    @Bean(JgsqGameFinder.class)         GameFinder gameFinder;
    @Bean(ActiveServerFinder.class)     ServerFinder serverFinder;
    @Bean(ActiveDatabaseManager.class)  DatabaseManager databaseManager;
    @Bean(GreenEventManager.class)      EventManager eventManager;
    @Extra                              long gameServerId;

    private Game game;
    private GameServerEntity gameServerEntity;
    private ServerFragmentPagerAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        appBarLayout.removeOnOffsetChangedListener(this);
        super.onPause();
    }

    @AfterViews
    void initialize()
    {
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

        collapsingToolbarLayout.setTitle(gameServerEntity.getHostName());
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

        setupSwipeContainer();
    } // initialize

    private void setupSwipeContainer()
    {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                serverManager.update(
                        game,
                        gameServerEntity.ipAddress,
                        gameServerEntity.port,
                        new OnServerUpdatedListener() {
                            @Override
                            public void onServerUpdated(GameServer gameServer) {

                                databaseManager.update(gameServer);

                                if (gameServer.getProtocol().getResponseStatus() != ServerResponseStatus.OK) {
                                    Snackbar.make(
                                            swipeContainer,
                                            gameServer.getProtocol().getResponseStatus().toString(),
                                            Snackbar.LENGTH_SHORT
                                    ).show();
                                }

                                Intent intent = new Intent(AgsmKeys.Action.Server.ON_SERVER_UPDATED)
                                        .putExtra(MainActivity.EXTRA_GAME_SERVER_ID, gameServerId);
                                sendBroadcast(intent);

                                reinitialize();
                            }
                        } // OnServerUpdatedListener
                );
            }
        });
    }

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

    @OptionsItem(R.id.action_server_remove)
    void remove(MenuItem menuItem)
    {
        // TODO themed button color?
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(
                        android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // remove database entry
                                databaseManager.remove(gameServerEntity);
                                // notify receivers
                                Intent intent = new Intent(AgsmKeys.Action.Server.ON_SERVER_REMOVED);
                                intent.putExtra(MainActivity.EXTRA_GAME_SERVER_ID, gameServerId);
                                sendBroadcast(intent);
                                // close this activity
                                finish();
                            }
                        })
                .setNegativeButton(
                        android.R.string.cancel,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        })
                .setTitle("Remove Server")
                .setMessage("Are you sure?")
                .create();
        dialog.show();
    }


    @UiThread
    void reinitialize()
    {
//        overviewFragment.update();
//        detailsFragment.update();
//        rconFragment.update();

        swipeContainer.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @OptionsItem(android.R.id.home)
    boolean homeSelected(MenuItem item)
    {
        onBackPressed();
        return true;
    }

    @Receiver(actions = AgsmKeys.Action.Server.ON_UPDATE_SERVERS)
    void onUpdateServers()
    {
        swipeContainer.setRefreshing(true);
    }

    @Receiver(actions = AgsmKeys.Action.Server.ON_SERVERS_UPDATED)
    void onServersUpdated()
    {
        reinitialize();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        swipeContainer.setEnabled(verticalOffset == 0);
    }
} // ServerFragment
