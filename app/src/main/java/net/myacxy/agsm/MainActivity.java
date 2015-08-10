package net.myacxy.agsm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

import net.myacxy.agsm.fragments.AddServerFragment_;
import net.myacxy.agsm.fragments.ServerFragment;
import net.myacxy.agsm.fragments.ServerFragment_;
import net.myacxy.agsm.interfaces.OnServerAddedListener;
import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;
import net.myacxy.agsm.views.adapters.ServerCardAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements OnServerAddedListener
{
    protected static final int IDENTIFIER_HOME = -1;
    protected static final int IDENTIFIER_NOTIFICATIONS = -2;
    protected static final int IDENTIFIER_SETTINGS = -3;
    protected static final int IDENTIFIER_ADD_SERVER = -4;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.main_content_layout)
    FrameLayout mainLayout;

    @ViewById(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @ViewById(R.id.fab)
    FloatingActionButton addServerButton;

    @Bean(ActiveServerFinder.class)
    ServerFinder serverFinder;

    @Bean
    ServerCardAdapter serverCardAdapter;

    protected Drawer drawer;
    protected Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @AfterViews
    void initialize()
    {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.drawer_home_title);

        setupDrawer();
        addServersToDrawer(drawer, serverFinder.findAll());
        setupViews();
        bindAdapter();
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
                        switch (identifier)
                        {
                            case IDENTIFIER_HOME:
                                break;
                            case IDENTIFIER_NOTIFICATIONS:
                                break;
                            case IDENTIFIER_SETTINGS:
                                break;
                            case IDENTIFIER_ADD_SERVER:
                                break;
                            default:
                                transitionToServerFragment(serverFinder.findById(identifier));
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

    @Click(R.id.fab)
    void showAddServerDialog()
    {
        AddServerFragment_ fragment = new AddServerFragment_();
        fragment.setOnServerAddedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_content_layout, fragment, "add_server")
                .addToBackStack(null)
                .commit();
    }

    private void transitionToServerFragment(GameServerEntity gameServerEntity)
    {
        ServerFragment serverFragment = ServerFragment_
                .builder()
                .gameServerId(gameServerEntity.getId().intValue())
                .build();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.main_content_layout, serverFragment, "server " + gameServerEntity.getId())
                .addToBackStack(null)
                .commit();
    }

    protected void addServersToDrawer(Drawer drawer, List<GameServerEntity> serverEntities)
    {
        for (final GameServerEntity serverEntity : serverEntities) {
            drawer.addItem(new SecondaryDrawerItem()
                    .withIcon(FontAwesome.Icon.faw_server)
                    .withName(serverEntity.hostName.trim())
                    .withBadge(String.valueOf(serverEntity.getPlayers().size()))
                    .withIdentifier(serverEntity.getId().intValue()));
        }
    }

    @Override
    public void onServerAdded(GameServerEntity server)
    {
        serverCardAdapter.addItem(server);
    }

} // MainActivity
