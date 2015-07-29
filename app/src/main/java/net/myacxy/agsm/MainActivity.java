package net.myacxy.agsm;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import net.myacxy.agsm.fragments.HomeFragment_;
import net.myacxy.agsm.fragments.ServerFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
{
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.drawer_view)
    NavigationView drawerView;

    @ViewById(R.id.main_content_layout)
    FrameLayout mainLayout;

    private MenuItem previousMenuItem;
    private Menu mDrawerMenu;

    protected HomeFragment_ homeFragment;
    protected ServerFragment_ serverFragment;

    @AfterViews
    void initialize()
    {
        if (drawerView != null) {
            setupDrawerContent(drawerView);
            mDrawerMenu = drawerView.getMenu();
        }

        changeFragment(R.id.drawer_home);
    }

    @OptionsItem(android.R.id.home)
    boolean homeSelected(MenuItem item)
    {
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
                        if(previousMenuItem != null)
                        {
                            if(previousMenuItem == menuItem) return true;
                            previousMenuItem.setChecked(false);
                        }

                        // check current selection
                        menuItem.setCheckable(true);
                        menuItem.setChecked(true);
                        previousMenuItem = menuItem;
                        drawerLayout.closeDrawers();

                        // drawer needs refresh due to a bug
                        // @see https://code.google.com/p/android/issues/detail?id=176300
                        navigationView.invalidate();
                        mDrawerMenu.getItem(mDrawerMenu.size() - 1).setTitle(getString(R.string.drawer_servers_title));

                        changeFragment(menuItem.getItemId());
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
                if(serverFragment == null) serverFragment = new ServerFragment_();
                tag = "server";
                fragment = serverFragment;
                break;
            default:
                fragment = null;
        }

        if(fragment != null)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.main_content_layout, fragment, tag)
                    .addToBackStack(null).commit();
        }
    }

} // MainActivity
