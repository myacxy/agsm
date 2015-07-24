package net.myacxy.agsm.fragments;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.thedeanda.lorem.Lorem;

import net.myacxy.agsm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_server_overview)
public class ServerDetailsFragment extends Fragment
{
    @ViewById(R.id.server_overview_text)
    TextView overviewText;

    @AfterViews
    void initialize()
    {
        overviewText.setText(Lorem.getParagraphs(3, 5));
    }
}
