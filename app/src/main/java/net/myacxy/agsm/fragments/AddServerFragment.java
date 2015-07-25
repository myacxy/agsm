package net.myacxy.agsm.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import net.myacxy.agsm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_add_server)
@OptionsMenu(R.menu.menu_add_server)
public class AddServerFragment extends BaseToolbarFragment
{
    @ViewById(R.id.fab)
    FloatingActionButton doneButton;

    @AfterViews
    void initialize()
    {
        super.initialize();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        actionBar.setTitle(getString(R.string.server_add_title));
    }

    @OptionsItem(R.id.menu_add_server_save)
    boolean saveSelected(MenuItem item)
    {
        Toast.makeText(getActivity(), "save", Toast.LENGTH_SHORT).show();
        return true;
    }

    @OptionsItem(android.R.id.home)
    boolean closeSelected(MenuItem item)
    {
        getFragmentManager().popBackStack();
        return true;
    }

    @Click(R.id.fab)
    void doneSelected()
    {
        Snackbar.make(doneButton, "Here's a Snackbar.", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }
} // AddServerFragment
