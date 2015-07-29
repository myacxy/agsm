package net.myacxy.agsm.fragments;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.github.clans.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.myacxy.agsm.R;
import net.myacxy.agsm.interfaces.AddServerListener;
import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.models.GameEntity;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.models.PlayerEntity;
import net.myacxy.agsm.utils.IpAddressAndDomainValidator;
import net.myacxy.agsm.utils.JsonGameFinder;
import net.myacxy.agsm.utils.PortValidator;
import net.myacxy.agsm.views.adapters.GameSpinnerAdapter;
import net.myacxy.jgsq.factory.GameServerFactory;
import net.myacxy.jgsq.model.Game;
import net.myacxy.jgsq.model.GameServer;
import net.myacxy.jgsq.model.Player;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;


@EFragment(R.layout.fragment_add_server)
public class AddServerFragment extends BaseToolbarFragment
{
    @ViewById(R.id.fab)
    protected FloatingActionButton doneButton;

    @ViewById(R.id.server_add_game)
    protected MaterialSpinner gameSpinner;

    @ViewById(R.id.server_add_address)
    protected MaterialEditText addressTextView;

    @ViewById(R.id.server_add_port)
    protected MaterialEditText portTextView;

    @ViewById(R.id.server_add_query_port)
    protected MaterialEditText queryPortTextView;

    private AddServerListener listener;
    private GameServer server;

    @Bean(JsonGameFinder.class)
    protected GameFinder gameFinder;

    @Bean(GameSpinnerAdapter.class)
    protected GameSpinnerAdapter gameSpinnerAdapter;


    @AfterViews
    protected void initialize()
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
    protected void bindAdapter()
    {
        gameSpinner.setAdapter(gameSpinnerAdapter);

        addressTextView.addValidator(new IpAddressAndDomainValidator("malformed address"));
        portTextView.addValidator(new PortValidator("port out of range"));
        queryPortTextView.addValidator(new PortValidator("port out of range"));
    }

    @Background
    protected void initializeServer(Game game, String address, int port)
    {
        GameServerFactory gsf = new GameServerFactory();
        server = gsf.getGameServer(game);
        server.connect(address, port);
        server.update();

        boolean duplicate = new Select()
                .from(GameServerEntity.class)
                .where("ip_address = ? AND port = ?", server.ipAddress, server.port)
                .execute()
                .size() > 0;

        if(duplicate)
        {
            Snackbar.make(doneButton,
                    String.format("%s is already known.", server.hostName),
                    Snackbar.LENGTH_LONG)
                    .show();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hideProgress(doneButton);
        }
        else
        {
            GameServerEntity gse = new GameServerEntity(server);
            GameEntity knownGame = new Select()
                    .from(GameEntity.class)
                    .where("name = ?", gse.game.name)
                    .executeSingle();

            if(knownGame == null)
            {
                gse.game.save();
            }
            else
            {
                gse.game = knownGame;
            }

            gse.save();

            ActiveAndroid.beginTransaction();
            try
            {
                for (Player player: server.players) {
                    PlayerEntity pe = new PlayerEntity(player);
                    pe.gameServer = gse;
                    pe.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }

            getFragmentManager().popBackStack();

            if(listener != null)
            {
                listener.onServerAdded(gse);
            }
        }
    } // initializeServer

    @UiThread
    protected void hideProgress(FloatingActionButton fab)
    {
        fab.setIndeterminate(false);
        fab.setClickable(true);
    }

    @OptionsItem(android.R.id.home)
    boolean closeSelected(MenuItem item)
    {
        getFragmentManager().popBackStack();
        return true;
    }

    @Click(R.id.fab)
    protected void doneSelected()
    {
        if(validateInput())
        {
            Game game = gameFinder.get(gameSpinner.getSelectedItem().toString());
            String address = addressTextView.getText().toString();
            int port = Integer.parseInt(portTextView.getText().toString());

            initializeServer(game, address, port);
            doneButton.setIndeterminate(true);
            doneButton.setClickable(false);
        }
    }

    protected boolean validateInput()
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

    protected boolean validateGame(MaterialSpinner spinner)
    {
        String gameName = spinner.getSelectedItem().toString();
        if(gameName.equals("select a game"))
        {
            gameSpinner.setError("game selection required");
            return false;
        }
        return true;
    }

    protected boolean validateText(MaterialEditText text)
    {
        return text.validate();
    }

    protected void showSnackbar(String message)
    {
        Snackbar.make(doneButton, message, Snackbar.LENGTH_LONG)
                .show();
    }

    public void setAddServerListener(AddServerListener listener)
    {
        this.listener = listener;
    }
} // AddServerFragment
