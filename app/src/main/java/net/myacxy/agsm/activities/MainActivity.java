package net.myacxy.agsm.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import net.myacxy.agsm.AgsmService_;
import net.myacxy.agsm.R;
import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.interfaces.ServerUpdateListener;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.utils.AgsmKeys;
import net.myacxy.agsm.utils.GameIconHelper;
import net.myacxy.agsm.views.adapters.ServerCardAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_home)
public class MainActivity extends AppCompatActivity implements ServerUpdateListener
{
    public static final int UPDATE_REASON_MANUAL = 0;
    public static final int UPDATE_REASON_PERIODIC = 1;

    public static final String EXTRA_UPDATE_REASON = "net.myacxy.agsm.extra.UPDATE_REASON";
    public static final String EXTRA_GAME_SERVER_ID = "net.myacxy.agsm.extra.GAME_SERVER_ID";

    private static final int IDENTIFIER_HOME = -10;
    private static final int IDENTIFIER_NOTIFICATIONS = -11;
    private static final int IDENTIFIER_SETTINGS = -12;
    private static final int IDENTIFIER_ADD_SERVER = -13;
    // 'Add Server' item is always last but drawer positions start a 1
    // Home + Notifications + Settings + Server Section Header = 4 items
    private static final int DRAWER_SERVER_ITEM_OFFSET = 4;

    @ViewById(R.id.tb_home)             Toolbar toolbar;
    @ViewById(R.id.fl_main_content)     FrameLayout mainLayout;
    @ViewById(R.id.srl_home_container)  SwipeRefreshLayout swipeContainer;
    @ViewById(R.id.rv_home)             RecyclerView recyclerView;
    @ViewById(R.id.fab_home)            FloatingActionButton addServerButton;
    @Bean(ActiveServerFinder.class)     ServerFinder serverFinder;
    @Bean                               ServerCardAdapter serverCardAdapter;

    protected Drawer drawer;
    protected Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        // TODO ensure periodic refresh
        Intent intent = new Intent(AgsmKeys.Action.Service.ENSURE_PERIODIC_REFRESH);
        sendBroadcast(intent);
    }

    @AfterViews
    void initialize()
    {
        setSupportActionBar(toolbar);

        setupDrawer();
        addServersToDrawer(drawer, serverFinder.findAll());
        setupViews();
        bindAdapter();

        toolbar.setTitle(R.string.drawer_home_title);
    }

    void setupDrawer()
    {
        drawer = new DrawerBuilder(this)
                .withRootView(R.id.fl_drawer_container)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_home)
                                .withName(R.string.drawer_home_title)
                                .withIdentifier(IDENTIFIER_HOME),
                        new PrimaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_notifications)
                                .withName(R.string.drawer_notifications_title)
                                .withIdentifier(IDENTIFIER_NOTIFICATIONS),
                        new PrimaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_settings)
                                .withName(R.string.drawer_settings_title)
                                .withIdentifier(IDENTIFIER_SETTINGS),
                        new SectionDrawerItem()
                                .withName(R.string.drawer_servers_title),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_plus)
                                .withName("Add Server")
                                .withSelectable(false)
                                .withIdentifier(IDENTIFIER_ADD_SERVER)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem == null) return false;

                        int identifier = drawerItem.getIdentifier();

                        switch (identifier) {
                            case IDENTIFIER_HOME:
                                break;
                            case IDENTIFIER_NOTIFICATIONS:
                                break;
                            case IDENTIFIER_SETTINGS:
                                break;
                            case IDENTIFIER_ADD_SERVER:
                                AddServerActivity_.intent(MainActivity.this).start();
                                break;
                            default:
                                Intent intent = ServerActivity_
                                        .intent(MainActivity.this)
                                        .gameServerId(identifier)
                                        .get();
                                startActivity(intent);
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    void setupViews()
    {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = AgsmService_.intent(MainActivity.this)
                        .action(AgsmKeys.Action.Server.UPDATE_SERVERS)
                        .extra(MainActivity.EXTRA_UPDATE_REASON, UPDATE_REASON_MANUAL)
                        .get();
                startService(intent);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(layoutManager);
    }

    void bindAdapter()
    {
        recyclerView.setAdapter(serverCardAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        onServersUpdated();
        drawer.setSelectionAtPosition(0);
    }

    @Override
    public void onBackPressed()
    {
        if (drawer != null && drawer.isDrawerOpen())
        {
            drawer.closeDrawer();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Click(R.id.fab_home)
    void showAddServerDialog()
    {
        AddServerActivity_.intent(MainActivity.this).start();
    }

    protected void addServersToDrawer(Drawer drawer, List<GameServerEntity> serverEntities)
    {
        for (final GameServerEntity server : serverEntities)
        {
            Drawable icon = GameIconHelper.getDrawable24(this, server.game.name);
            String name = server.getHostName();
            String badge;

            if(server.isOnline)
            {
                int players = server.getPlayers().size();
                int bots = server.getBots().size();
                if(bots > 0) badge = String.format("%d (%d)", players, bots);
                else badge = String.valueOf(players);

            }
            else
            {
                badge = "\u2014";
            }

            IDrawerItem item = new SecondaryDrawerItem()
                    .withIcon(icon)
                    .withName(name)
                    .withBadge(badge)
                    .withSelectable(false)
                    .withIdentifier(server.getId().intValue());
            int position = drawer.getDrawerItems().size() - 1;
            drawer.addItemAtPosition(item, position);
        }
    }

    //<editor-fold desc="ServerUpdateListener">
    @Receiver(actions = AgsmKeys.Action.Server.ON_UPDATE_SERVER)
    @Override
    public void onUpdateServer(@Receiver.Extra(EXTRA_GAME_SERVER_ID) long gameServerId)
    {
        serverCardAdapter.showProgress(gameServerId);
    }

    @Receiver(actions = AgsmKeys.Action.Server.ON_SERVER_UPDATED)
    @Override
    public void onServerUpdated(@Receiver.Extra(EXTRA_GAME_SERVER_ID) long gameServerId) {
        serverCardAdapter.hideProgress(gameServerId);
        serverCardAdapter.notifyDataSetChanged();
    }

    @Receiver(actions = AgsmKeys.Action.Server.ON_SERVERS_UPDATED)
    @Override
    public void onServersUpdated()
    {
        List<GameServerEntity> gameServerEntities = serverFinder.findAll();
        // update player count badge in navigation drawer
        if(gameServerEntities != null)
        {
            for(int i = 0; i < gameServerEntities.size(); i++)
            {
                GameServerEntity server = gameServerEntities.get(i);

                Drawable icon = GameIconHelper.getDrawable24(this, server.game.name);
                String name = server.getHostName();
                String badge;

                if(server.isOnline)
                {
                    int players = server.getPlayers().size();
                    int bots = server.getBots().size();
                    if(bots > 0) badge = String.format("%d (%d)", players, bots);
                    else badge = String.valueOf(players);

                }
                else
                {
                    badge = "\u2014";
                }

                IDrawerItem item = new SecondaryDrawerItem()
                        .withIcon(icon)
                        .withName(name)
                        .withBadge(badge)
                        .withSelectable(false)
                        .withIdentifier(server.getId().intValue());
                int position = i + DRAWER_SERVER_ITEM_OFFSET;
                drawer.updateItemAtPosition(item, position);
            }
        }

        drawer.getAdapter().notifyDataSetChanged();
        serverCardAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }
    //</editor-fold>

    //<editor-fold desc="Events">
    @Receiver(actions = AgsmKeys.Action.Server.ON_SERVER_ADDED)
    public void onServerAdded(@Receiver.Extra(EXTRA_GAME_SERVER_ID) int id)
    {
        GameServerEntity server = serverFinder.findById(id);
        serverCardAdapter.addItem(server);
        recyclerView.getAdapter().notifyDataSetChanged();

        Drawable icon = GameIconHelper.getDrawable24(this, server.game.name);
        int players = server.getPlayers().size();
        int bots = server.getBots().size();
        String badge;
        if(bots > 0) badge = String.format("%d (%d)", players, bots);
        else badge = String.valueOf(players);

        // add new server item in front of 'add server' item
        drawer.addItemAtPosition(
                new SecondaryDrawerItem()
                        .withIcon(icon)
                        .withName(server.getHostName())
                        .withBadge(badge)
                        .withSelectable(false)
                        .withIdentifier(server.getId().intValue()),
                drawer.getDrawerItems().size() - 1);
    }

    @Receiver(actions = AgsmKeys.Action.Server.ON_SERVER_REMOVED)
    void onServerRemoved(@Receiver.Extra(EXTRA_GAME_SERVER_ID) long id)
    {
        // remove item from drawer
        for (IDrawerItem drawerItem : drawer.getDrawerItems())
        {
            if (drawerItem.getIdentifier() == id)
            {
                int index = drawer.getDrawerItems().indexOf(drawerItem);
                drawer.removeItem(index);
                // remove server card
                serverCardAdapter.removeItem(index - DRAWER_SERVER_ITEM_OFFSET);
                break;
            }
        }

        drawer.getAdapter().notifyDataSetChanged();
    }
    //</editor-fold>

} // MainActivity
