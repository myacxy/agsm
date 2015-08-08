package net.myacxy.agsm.fragments;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.thedeanda.lorem.Lorem;

import net.myacxy.agsm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_server_rcon)
public class ServerRconFragment extends Fragment
{
    @ViewById(R.id.server_rcon_text)
    TextView rconText;

    @FragmentArg
    int gameServerId;

    @AfterViews
    void initialize()
    {
        rconText.setText(Lorem.getParagraphs(3, 5));
    }

    @UiThread
    public void update()
    {

    }
} // ServerRconFragment
