package net.myacxy.agsm.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thedeanda.lorem.Lorem;

import net.myacxy.agsm.R;
import net.myacxy.agsm.fragments.interfaces.AddServerListener;
import net.myacxy.jgsq.model.GameServer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EFragment(R.layout.fragment_home)
@OptionsMenu(R.menu.menu_home)
public class HomeFragment extends BaseToolbarFragment implements AddServerListener
{
    @ViewById(R.id.cardTitle1)
    TextView cardTitle1;

    @ViewById(R.id.cardText1)
    TextView cardText1;

    @ViewById(R.id.cardTitle2)
    TextView cardTitle2;

    @ViewById(R.id.cardText2)
    TextView cardText2;

    @ViewById(R.id.fab)
    FloatingActionButton addServerButton;

    @AfterViews
    void initialize()
    {
        super.initialize();

        cardTitle1.setText(Lorem.getTitle(1));
        cardText1.setText(Lorem.getParagraphs(1, 1));

        cardTitle2.setText(Lorem.getTitle(2));
        cardText2.setText(Lorem.getParagraphs(1, 1));
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
    public void onServerAdded(GameServer server)
    {
        Snackbar.make(addServerButton, server.hostName, Snackbar.LENGTH_LONG)
                .setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToastMessage("action");
                    }
                })
                .show();
    }
} // ServerOverviewFragment
