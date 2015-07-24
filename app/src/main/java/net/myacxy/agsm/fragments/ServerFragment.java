package net.myacxy.agsm.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import net.myacxy.agsm.R;
import net.myacxy.agsm.views.adapters.ServerFragmentPagerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_server)
public class ServerFragment extends BaseToolbarFragment
{
    @ViewById(R.id.viewpager)
    ViewPager viewPager;

    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @ViewById(R.id.fab)
    FloatingActionButton refreshButton;

    private ServerOverviewFragment_ overviewFragment;
    private ServerDetailsFragment_ detailsFragment;
    private ServerRconFragment_ rconFragment;
    private ServerFragmentPagerAdapter adapter;

    @AfterViews
    void initialize()
    {
        super.initialize();

        overviewFragment = new ServerOverviewFragment_();
        detailsFragment = new ServerDetailsFragment_();
        rconFragment = new ServerRconFragment_();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

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

    }

    private void setupViewPager(ViewPager viewPager)
    {
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
        Snackbar.make(refreshButton, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
