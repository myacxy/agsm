package net.myacxy.agsm;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.SubMenu;
import android.widget.FrameLayout;

import net.myacxy.agsm.fragments.HomeFragment_;
import net.myacxy.agsm.fragments.ServerFragment;
import net.myacxy.agsm.fragments.ServerFragment_;
import net.myacxy.agsm.interfaces.ServerFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.ActiveServerFinder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Field;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
{
    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @ViewById(R.id.drawer_view)
    NavigationView mDrawerView;

    @ViewById(R.id.main_content_layout)
    FrameLayout mainLayout;

    @Bean(ActiveServerFinder.class)
    ServerFinder serverFinder;

    private MenuItem previousMenuItem;
    private Menu mDrawerMenu;
    private SubMenu mServerMenu;

    protected HomeFragment_ homeFragment;

    private int mCurrentPosition = 0;
    final static String ARG_DRAWER_POSITION = "drawer_position";

    @AfterViews
    void initialize()
    {
        if (mDrawerView != null) {
            setupDrawerContent(mDrawerView);
            mDrawerMenu = mDrawerView.getMenu();
            mServerMenu = mDrawerMenu.getItem(mDrawerMenu.size() - 1).getSubMenu();
        }

        changeFragment(R.id.drawer_home);
    }

    @OptionsItem(android.R.id.home)
    boolean homeSelected(MenuItem item)
    {
        List<GameServerEntity> serverEntities = serverFinder.findAll();

        if(mServerMenu.size() != serverEntities.size())
        {
            mServerMenu.clear();
            addServersToDrawer(serverEntities);
        }
        // let child fragments handle what happens
        return false;
    }

    private void setupDrawerContent(final NavigationView navigationView)
    {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // unchecked previous selection
                        if (previousMenuItem != null) {
                            if (previousMenuItem == menuItem) return true;
                            previousMenuItem.setChecked(false);
                        }

                        // check current selection
                        menuItem.setCheckable(true);
                        menuItem.setChecked(true);
                        previousMenuItem = menuItem;
                        mDrawerLayout.closeDrawers();

                        // drawer needs refresh due to a bug
                        // @see https://code.google.com/p/android/issues/detail?id=176300
                        navigationView.invalidate();
                        mDrawerMenu.getItem(mDrawerMenu.size() - 1).setTitle(getString(R.string.drawer_servers_title));

                        if(menuItem.getGroupId() != R.id.drawer_menu_group_servers)
                        {
                            changeFragment(menuItem.getItemId());
                        }
                        return true;
                    }
                });
    }

    private void changeFragment(int id)
    {
        Fragment fragment;
        String tag = "";
        switch (id)
        {
            case R.id.drawer_home:
                if(homeFragment == null) homeFragment = new HomeFragment_();
                tag = "home";
                fragment = homeFragment;
                break;
            case R.id.drawer_notifications:
                fragment = null;
                break;
            case R.id.drawer_settings:
                fragment = null;
                break;
            default:
                fragment = null;
                break;
        }

        mCurrentPosition = id;

        if(fragment != null)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.main_content_layout, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        }
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

    protected void addServersToDrawer(List<GameServerEntity> serverEntities)
    {
        for (final GameServerEntity serverEntity : serverEntities)
        {
            // create menu entry
            final MenuItem menuItem = mServerMenu.add(
                    R.id.drawer_menu_group_servers, // group id
                    serverEntity.getId().intValue(),      // item id
                    0,                              // order
                    serverEntity.hostName);               // title

            // switch to server fragment on click
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    transitionToServerFragment(serverEntity);

                    // let onNavigationItemSelected do the rest
                    return false;
                }
            });
            // TODO set game logo
            menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_dashboard));
        }

        // workaround to refresh navigation drawer to show new menu entries
        getNavigationMenuPresenter(mDrawerView).updateMenuView(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt(ARG_DRAWER_POSITION, mCurrentPosition);
    }

    private static NavigationMenuPresenter getNavigationMenuPresenter(NavigationView view){
        try {
            Field presenterField = NavigationView.class.getDeclaredField("mPresenter");
            presenterField.setAccessible(true);
            return (NavigationMenuPresenter) presenterField.get(view);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
} // MainActivity
