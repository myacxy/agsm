package net.myacxy.agsm.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.myacxy.agsm.R;
import net.myacxy.agsm.events.GameSelectedEvent;
import net.myacxy.agsm.fragments.ChooseGameDialogFragment;
import net.myacxy.agsm.fragments.ChooseGameDialogFragment_;
import net.myacxy.agsm.interfaces.DatabaseManager;
import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.interfaces.OnServerCreatedListener;
import net.myacxy.agsm.interfaces.ServerManager;
import net.myacxy.agsm.managers.ActiveDatabaseManager;
import net.myacxy.agsm.managers.JgsqServerManager;
import net.myacxy.agsm.models.GameServerEntity;
import net.myacxy.agsm.utils.IpAddressAndDomainValidator;
import net.myacxy.agsm.utils.JgsqGameFinder;
import net.myacxy.agsm.utils.PortValidator;
import net.myacxy.agsm.utils.SharedItems;
import net.myacxy.jgsq.helpers.ServerResponseStatus;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

@EActivity(R.layout.activity_add_server)
@OptionsMenu(R.menu.menu_add_server)
public class AddServerActivity extends AppCompatActivity
{
    @ViewById(R.id.add_server_done_button)  protected FloatingActionButton doneButton;
    @ViewById(R.id.tv_as_cg_subtitle)       protected TextView selectedGameTextView;
    @ViewById(R.id.server_add_address)      protected MaterialEditText addressTextView;
    @ViewById(R.id.server_add_port)         protected MaterialEditText portTextView;
    @ViewById(R.id.server_add_query_port)   protected MaterialEditText queryPortTextView;
    @Bean(JgsqGameFinder.class)             protected GameFinder gameFinder;
    @Bean(JgsqServerManager.class)          protected ServerManager serverManager;
    @Bean(ActiveDatabaseManager.class)      protected DatabaseManager databaseManager;
    @InstanceState                          protected String selectedGame;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @AfterViews
    protected void initialize()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_back).color(Color.WHITE).sizeDp(18));
        getSupportActionBar().setTitle(getString(R.string.server_add_title));

        if(selectedGame != null) {
            selectedGameTextView.setText(selectedGame);
        }
    }

    @AfterViews
    protected void bindAdapter() {
        addressTextView.addValidator(
                new IpAddressAndDomainValidator(getString(R.string.error_server_invalid_address)));
        portTextView.addValidator(
                new PortValidator(getString(R.string.error_server_invalid_port)));
        queryPortTextView.addValidator(
                new PortValidator(getString(R.string.error_server_invalid_port)));
    }

    protected void initializeServer(Game game, String address, int port)
    {
        // show progress on the floating action button
        showProgress(doneButton);

        serverManager.create(game, address, port, new OnServerCreatedListener() {
            @Override
            public void onServerCreated(GameServer gameServer) {
                if (gameServer.connect() == ServerResponseStatus.CONNECTED) {
                    // server already exists?
                    GameServerEntity gameServerEntity = databaseManager.getGameServerEntity(gameServer);
                    if (gameServerEntity != null) {
                        showSnackbar(String.format("%s:%s is already known.",
                                        gameServerEntity.ipAddress,
                                        gameServerEntity.port)
                        );
                    }
                    // update server status information
                    else if (gameServer.update() == ServerResponseStatus.OK) {
                        gameServerEntity = databaseManager.save(gameServer);

                        // notify receivers that server was successfully added
                        Intent data = new Intent(SharedItems.Action.Server.ON_SERVER_ADDED);
                        data.putExtra(
                                SharedItems.Extra.GAME_SERVER_ID,
                                gameServerEntity.getId().intValue()
                        );
                        sendBroadcast(data);

                        // go back to main / home activity
                        back();
                    }
                    // show error
                    else {
                        showSnackbar(String.format("%s", gameServer.getProtocol().getResponseStatus()));
                    }
                }
                // show error
                else {
                    showSnackbar(String.format("%s", gameServer.getProtocol().getResponseStatus()));
                }
                hideProgress(doneButton);
            } // onServerCreated
        });
    } // initializeServer


    @UiThread
    protected void showProgress(FloatingActionButton fab)
    {
        fab.setIndeterminate(true);
        fab.setClickable(false);
    }

    @UiThread
    protected void hideProgress(FloatingActionButton fab)
    {
        fab.setIndeterminate(false);
        fab.setClickable(true);
    }

    @UiThread
    protected void back()
    {
        onBackPressed();
    }

    @OptionsItem(R.id.menu_add_server_tfj)
    void addTfj(MenuItem item)
    {
        Game game = gameFinder.find("Star Wars Jedi Knight 2: Jedi Outcast");
        selectedGame = game.name;
        selectedGameTextView.setText(selectedGame);
        addressTextView.setText("85.25.149.26");
        portTextView.setText("28070");
    }

    @OptionsItem(R.id.menu_add_server_myacxy)
    void addMyacxy(MenuItem item)
    {
        Game game = gameFinder.find("Star Wars Jedi Knight 2: Jedi Outcast");
        selectedGame = game.name;
        selectedGameTextView.setText(selectedGame);
        addressTextView.setText("myacxy.net");
        portTextView.setText("28070");
    }

    @OptionsItem(R.id.menu_add_server_q3)
    void addQ3(MenuItem item)
    {
        Game game = gameFinder.find("Quake III Arena");
        selectedGame = game.name;
        selectedGameTextView.setText(selectedGame);
        addressTextView.setText("108.61.179.59");
        portTextView.setText("28356");
    }

    @Click(R.id.add_server_done_button)
    protected void onDoneClicked()
    {
        if(validateInput())
        {
            Game game = gameFinder.find(selectedGame);
            String address = addressTextView.getText().toString().trim();
            int port = Integer.parseInt(portTextView.getText().toString().trim());

            initializeServer(game, address, port);
        }
    }

    @Click(R.id.rl_as_choose_game)
    protected void onChooseGameClicked()
    {
        ChooseGameDialogFragment dialogFragment = ChooseGameDialogFragment_.builder().build();

        getSupportFragmentManager().beginTransaction()
                .add(dialogFragment, null)
                .commit();
    }

    @OptionsItem(android.R.id.home)
    boolean homePressed()
    {
        back();
        return false;
    }

    protected boolean validateInput()
    {
        // valid game?
        boolean valid = validateGame();

        // valid address?
        if(addressTextView.getText().toString().trim().length() > 0)
        {
            if(!validateText(addressTextView)) valid = false;
        }
        else
        {
            addressTextView.setError(getString(R.string.error_server_address_required));
            valid = false;
        }

        // valid port?
        if(portTextView.getText().toString().length() > 0)
        {
            if(!validateText(portTextView)) valid = false;
        }
        else
        {
            portTextView.setError(getString(R.string.error_server_port_required));
            valid = false;
        }

        // valid query port?
        if(queryPortTextView.getText().length() > 0)
        {
            if(!validateText(queryPortTextView)) valid = false;
        }
        return valid;
    }

    protected boolean validateGame()
    {
        if(selectedGame != null)
        {
            return true;
        }
        else {
            showSnackbar(getString(R.string.error_server_game_required));
            return false;
        }
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

    @Subscribe
    public void onGameSelected(GameSelectedEvent event)
    {
        if(event.game == null) {
            selectedGame = null;
            selectedGameTextView.setText("None");
            return;
        }

        selectedGame = event.game.name;
        selectedGameTextView.setText(selectedGame);
    }

} // AddServerFragment
