package net.myacxy.agsm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

    @ViewById(R.id.fab)
    FloatingActionButton fab;

    @AfterViews
    void initialize()
    {
        super.initialize();

        cardTitle1.setText(Lorem.getTitle(1));
        cardText1.setText(Lorem.getParagraphs(1, 1));

        cardTitle2.setText(Lorem.getTitle(2));
        cardText2.setText(Lorem.getParagraphs(1, 1));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OptionsItem(R.id.action_example)
    void actionExampleSelected(MenuItem item)
    {
        Toast.makeText(getActivity().getApplicationContext(), "example action " + item.toString(), Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.fab)
    void add()
    {
        Snackbar.make(fab, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

} // ServerOverviewFragment
