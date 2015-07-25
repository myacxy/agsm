package net.myacxy.agsm.fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.thedeanda.lorem.Lorem;

import net.myacxy.agsm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_home)
@OptionsMenu(R.menu.menu_home)
public class HomeFragment extends BaseToolbarFragment
{
    @ViewById(R.id.cardTitle1)
    TextView cardTitle1;

    @ViewById(R.id.cardText1)
    TextView cardText1;

    @ViewById(R.id.cardTitle2)
    TextView cardTitle2;

    @ViewById(R.id.cardText2)
    TextView cardText2;

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
    void addServer()
    {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_content_layout, new AddServerFragment_(), "add_server")
                .addToBackStack(null)
                .commit();
    }

} // ServerOverviewFragment
