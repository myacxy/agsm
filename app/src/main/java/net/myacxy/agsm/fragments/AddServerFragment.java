package net.myacxy.agsm.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import net.myacxy.agsm.R;
import net.myacxy.agsm.interfaces.AddServerListener;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.jgsq.factory.GameFactory;
import net.myacxy.jgsq.factory.GameServerFactory;
import net.myacxy.jgsq.utils.Utilities;
import net.myacxy.jgsq.model.Game;
import net.myacxy.jgsq.model.GameServer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.fragment_add_server)
@OptionsMenu(R.menu.menu_add_server)
public class AddServerFragment extends BaseToolbarFragment
{
    @ViewById(R.id.fab)
    FloatingActionButton doneButton;

    @ViewById(R.id.server_add_game)
    Spinner gameSpinner;

    @ViewById(R.id.server_add_address)
    TextView addressTextView;

    @ViewById(R.id.server_add_port)
    TextView portTextView;

    private AddServerListener listener;
    private GameServer server;

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
        //String game = gameSpinner.getSelectedItem().toString();
        String gameName = "JK2";
        String address = addressTextView.getText().toString();
        int port = Integer.parseInt(portTextView.getText().toString());

        initializeServer(address, port, gameName);


        return true;
    }

    @Background
    void initializeServer(String address, int port, String gameName)
    {
        GameFactory gf = new GameFactory();
        gf.loadConfig(Utilities.getResourceAsStream("games.conf.json"));
        Game game = gf.getGame(gameName);
        GameServerFactory gsf = new GameServerFactory();
        server = gsf.getGameServer(game);
        server.connect(address, port);
        server.update();

        boolean duplicate = Select.from(GameServerEntity.class)
                .where(Condition.prop("ip_address").eq(server.ipAddress))
                .and(Condition.prop("port").eq(server.port)).count() > 0;

        if(duplicate)
        {
            Snackbar.make(doneButton,
                    String.format("%s is already known.", server.hostName),
                    Snackbar.LENGTH_LONG)
                    .show();
        }
        else
        {
            GameServerEntity gse = new GameServerEntity(server);
            gse.save();

            getFragmentManager().popBackStack();

            if(listener != null)
            {
                listener.onServerAdded(gse);
            }
        }
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
                .setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToastMessage("action");
                    }
                })
                .show();
    }

    public void setAddServerListener(AddServerListener listener)
    {
        this.listener = listener;
    }
} // AddServerFragment
