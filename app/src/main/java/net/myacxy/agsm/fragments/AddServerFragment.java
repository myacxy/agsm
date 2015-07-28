package net.myacxy.agsm.fragments;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.clans.fab.FloatingActionButton;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.myacxy.agsm.R;
import net.myacxy.agsm.interfaces.AddServerListener;
import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.IpAddressAndDomainValidator;
import net.myacxy.agsm.utils.JsonGameFinder;
import net.myacxy.agsm.utils.PortValidator;
import net.myacxy.agsm.views.adapters.GameSpinnerAdapter;
import net.myacxy.jgsq.factory.GameServerFactory;
import net.myacxy.jgsq.model.Game;
import net.myacxy.jgsq.model.GameServer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;


@EFragment(R.layout.fragment_add_server)
public class AddServerFragment extends BaseToolbarFragment
{
    @ViewById(R.id.fab)
    FloatingActionButton doneButton;

    @ViewById(R.id.server_add_game)
    MaterialSpinner gameSpinner;

    @ViewById(R.id.server_add_address)
    MaterialEditText addressTextView;

    @ViewById(R.id.server_add_port)
    MaterialEditText portTextView;

    @ViewById(R.id.server_add_query_port)
    MaterialEditText queryPortTextView;

    private AddServerListener listener;
    private GameServer server;

    @Bean(JsonGameFinder.class)
    GameFinder gameFinder;

    @Bean(GameSpinnerAdapter.class)
    GameSpinnerAdapter gameSpinnerAdapter;


    @AfterViews
    void initialize()
    {
        super.initialize();

        setHasOptionsMenu(true);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        actionBar.setTitle(getString(R.string.server_add_title));

        gameFinder.setConfig("games.conf.json");
    }

    @AfterViews
    void bindAdapter()
    {
        gameSpinner.setAdapter(gameSpinnerAdapter);

        addressTextView.addValidator(new IpAddressAndDomainValidator("malformed address"));
        portTextView.addValidator(new PortValidator("port out of range"));
        queryPortTextView.addValidator(new PortValidator("port out of range"));
    }

    @Background
    void initializeServer(Game game, String address, int port)
    {
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
            doneButton.setShowProgressBackground(false);
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
    } // initializeServer

    @OptionsItem(android.R.id.home)
    boolean closeSelected(MenuItem item)
    {
        getFragmentManager().popBackStack();
        return true;
    }

    @Click(R.id.fab)
    void doneSelected()
    {
        if(validateInput())
        {
            Game game = gameFinder.get(gameSpinner.getSelectedItem().toString());
            String address = addressTextView.getText().toString();
            int port = Integer.parseInt(portTextView.getText().toString());

            initializeServer(game, address, port);
            doneButton.setShowProgressBackground(true);
        }
    }

    private boolean validateInput()
    {
        boolean valid = true;
        if(!validateGame(gameSpinner)) valid = false;
        if(!validateText(addressTextView)) valid = false;
        if(!validateText(portTextView)) valid = false;
        if(queryPortTextView.getText().length() > 0)
        {
            if(!validateText(queryPortTextView)) valid = false;
        }
        return valid;
    }

    private boolean validateGame(MaterialSpinner spinner)
    {
        String gameName = spinner.getSelectedItem().toString();
        if(gameName.equals("select a game"))
        {
            gameSpinner.setError("game selection required");
            return false;
        }
        return true;
    }

    private boolean validateText(MaterialEditText text)
    {
        return text.validate();
    }

    private void showSnackbar(String message)
    {
        Snackbar.make(doneButton, message, Snackbar.LENGTH_LONG)
                .show();
    }

    public void setAddServerListener(AddServerListener listener)
    {
        this.listener = listener;
    }
} // AddServerFragment
