package net.myacxy.agsm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.FontAwesome;
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
import net.myacxy.agsm.views.adapters.ServerCardAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_home)
public class MainActivity extends AppCompatActivity implements ServerUpdateListener
{
    public static final String ACTION_ENSURE_PERIODIC_REFRESH = "net.myacxy.agsm.action.ENSURE_PERIODIC_REFRESH";
    public static final String ACTION_PERIODIC_REFRESH = "net.myacxy.agsm.action.PERIODIC_REFRESH";

    public static final String ACTION_ON_SERVER_ADDED = "net.myacxy.agsm.action.ON_SERVER_ADDED";
    public static final String ACTION_ON_SERVER_REMOVED = "net.myacxy.agsm.action.ON_SERVER_REMOVED";

    public static final String ACTION_ON_UPDATE_SERVER = "net.myacxy.agsm.action.ON_UPDATE_SERVER";
    public static final String ACTION_ON_SERVER_UPDATED = "net.myacxy.agsm.action.ON_SERVER_UPDATED";

    public static final String ACTION_UPDATE_SERVERS = "net.myacxy.agsm.action.UPDATE_SERVERS";
    public static final String ACTION_ON_UPDATE_SERVERS = "net.myacxy.agsm.action.ON_UPDATE_SERVERS";
    public static final String ACTION_ON_SERVERS_UPDATED = "net.myacxy.agsm.action.ON_SERVERS_UPDATED";

    public static final int UPDATE_REASON_MANUAL = 0;
    public static final int UPDATE_REASON_PERIODIC = 1;

    public static final String EXTRA_UPDATE_REASON = "net.myacxy.agsm.extra.UPDATE_REASON";
    public static final String EXTRA_GAME_SERVER_ID = "net.myacxy.agsm.extra.GAME_SERVER_ID";

    private static final int IDENTIFIER_HOME = -1;
    private static final int IDENTIFIER_NOTIFICATIONS = -2;
    private static final int IDENTIFIER_SETTINGS = -3;
    private static final int IDENTIFIER_ADD_SERVER = -4;
    // 'Add Server' item is always last but drawer positions start a 1
    // Home + Notifications + Settings + Server Section Header = 4 items
    private static final int DRAWER_SERVER_ITEM_OFFSET = 4;

    @ViewById(R.id.home_toolbar)        Toolbar toolbar;
    @ViewById(R.id.main_content_layout) FrameLayout mainLayout;
    @ViewById(R.id.home_recycler_view)  RecyclerView recyclerView;
    @ViewById(R.id.home_fab)            FloatingActionButton addServerButton;
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
        Intent intent = new Intent(ACTION_ENSURE_PERIODIC_REFRESH);
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
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_home)
                                .withName(R.string.drawer_home_title),
                        new PrimaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_notifications)
                                .withName(R.string.drawer_notifications_title),
                        new PrimaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_settings)
                                .withName(R.string.drawer_settings_title),
                        new SectionDrawerItem()
                                .withName(R.string.drawer_servers_title),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_add)
                                .withName("Add Server")
                                .withIdentifier(IDENTIFIER_ADD_SERVER)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int position, long id, IDrawerItem iDrawerItem) {
                        int identifier = iDrawerItem.getIdentifier();

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
        drawer.setSelection(0);
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

    @Click(R.id.home_fab)
    void showAddServerDialog()
    {
        AddServerActivity_.intent(MainActivity.this).start();
    }

    protected void addServersToDrawer(Drawer drawer, List<GameServerEntity> serverEntities)
    {
        for (final GameServerEntity serverEntity : serverEntities)
        {
            String name;
            String badge;

            if(serverEntity.isOnline)
            {
                name = serverEntity.hostName.trim();
                badge = String.valueOf(serverEntity.getPlayers().size());
            }
            else
            {
                name = serverEntity.ipAddress + ":" + serverEntity.port;
                badge = "\u2014";
            }

            IDrawerItem item = new SecondaryDrawerItem()
                    .withIcon(FontAwesome.Icon.faw_server)
                    .withName(name)
                    .withBadge(badge)
                    .withIdentifier(serverEntity.getId().intValue());
            int position = drawer.getDrawerItems().size() - 1;
            drawer.addItem(item, position);
        }
    }

    //<editor-fold desc="ServerUpdateListener">
    @Receiver(actions = ACTION_ON_UPDATE_SERVER)
    @Override
    public void onUpdateServer(@Receiver.Extra(EXTRA_GAME_SERVER_ID) long gameServerId)
    {
        serverCardAdapter.showProgress(gameServerId);
    }

    @Receiver(actions = ACTION_ON_SERVER_UPDATED)
    @Override
    public void onServerUpdated(@Receiver.Extra(EXTRA_GAME_SERVER_ID) long gameServerId) {
        serverCardAdapter.hideProgress(gameServerId);
        serverCardAdapter.notifyDataSetChanged();
    }

    @Receiver(actions = ACTION_ON_SERVERS_UPDATED)
    @Override
    public void onServersUpdated()
    {
        List<GameServerEntity> gameServerEntities = serverFinder.findAll();
        // update player count badge in navigation drawer
        if(gameServerEntities != null)
        {
            for(int i = 0; i < gameServerEntities.size(); i++)
            {
                GameServerEntity gse = gameServerEntities.get(i);
                if(gse.isOnline)
                {
                    String playerCount = String.valueOf(gse.getPlayers().size());
                    drawer.updateBadge(playerCount, i + DRAWER_SERVER_ITEM_OFFSET);
                }
                else
                {
                    drawer.updateBadge("\u2014", i + DRAWER_SERVER_ITEM_OFFSET);
                }
            }
        }

        drawer.getAdapter().notifyDataSetChanged();
        serverCardAdapter.notifyDataSetChanged();
    }
    //</editor-fold>

    //<editor-fold desc="OnServerAdded / OnServerRemoved">
    @Receiver(actions = MainActivity.ACTION_ON_SERVER_ADDED)
    public void onServerAdded(@Receiver.Extra(EXTRA_GAME_SERVER_ID) int id)
    {
        GameServerEntity serverEntity = serverFinder.findById(id);
        serverCardAdapter.addItem(serverEntity);
        recyclerView.getAdapter().notifyDataSetChanged();

        // add new server item in front of 'add server' item
        drawer.addItem(
                new SecondaryDrawerItem()
                        .withIcon(FontAwesome.Icon.faw_server)
                        .withName(serverEntity.hostName.trim())
                        .withBadge(String.valueOf(serverEntity.getPlayers().size()))
                        .withIdentifier(serverEntity.getId().intValue()),
                drawer.getDrawerItems().size() - 1);
    }

    @Receiver(actions = ACTION_ON_SERVER_REMOVED)
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

    @OptionsItem(R.id.action_home_refresh)
    void onRefreshClicked()
    {
        Intent intent = AgsmService_.intent(this)
                .action(MainActivity.ACTION_UPDATE_SERVERS)
                .extra(MainActivity.EXTRA_UPDATE_REASON, UPDATE_REASON_MANUAL)
                .get();
        startService(intent);
    }

} // MainActivity
