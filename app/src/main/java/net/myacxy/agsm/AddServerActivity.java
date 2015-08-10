package net.myacxy.agsm;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.rengwuxian.materialedittext.MaterialEditText;

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
import net.myacxy.agsm.views.adapters.GameSpinnerAdapter;
import net.myacxy.jgsq.helpers.ServerResponseStatus;
import net.myacxy.jgsq.models.Game;
import net.myacxy.jgsq.models.GameServer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import fr.ganfra.materialspinner.MaterialSpinner;


@EActivity(R.layout.activity_add_server)
@OptionsMenu(R.menu.menu_add_server)
public class AddServerActivity extends AppCompatActivity
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

    @Bean(JgsqGameFinder.class)
    protected GameFinder gameFinder;

    @Bean(GameSpinnerAdapter.class)
    protected GameSpinnerAdapter gameSpinnerAdapter;

    @Bean(JgsqServerManager.class)
    protected ServerManager serverManager;

    @Bean(ActiveDatabaseManager.class)
    protected DatabaseManager databaseManager;

    @AfterViews
    protected void initialize()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_back).color(Color.WHITE).sizeDp(18));
        getSupportActionBar().setTitle(getString(R.string.server_add_title));

    }

    @AfterViews
    protected void bindAdapter()
    {
        gameSpinner.setAdapter(gameSpinnerAdapter);

        addressTextView.addValidator(new IpAddressAndDomainValidator("malformed address"));
        portTextView.addValidator(new PortValidator("port out of range"));
        queryPortTextView.addValidator(new PortValidator("port out of range"));
    }

    protected void initializeServer(Game game, String address, int port)
    {
        showProgress(doneButton);
        serverManager.create(game, address, port, new OnServerCreatedListener() {
            @Override
            public void onServerCreated(GameServer gameServer) {
                if (gameServer.connect() == ServerResponseStatus.CONNECTED) {
                    GameServerEntity gameServerEntity = databaseManager.getGameServerEntity(gameServer);
                    if (gameServerEntity != null) {
                        showSnackbar(
                                String.format("%s:%s is already known.",
                                        gameServerEntity.ipAddress,
                                        gameServerEntity.port));
                    } else if (gameServer.update() == ServerResponseStatus.OK) {
                        gameServerEntity = databaseManager.save(gameServer);

                        Intent data = new Intent(MainActivity.RECEIVER_SERVER_ADDED);
                        data.putExtra("id", gameServerEntity.getId().intValue());
                        sendBroadcast(data);

                        back();
                    } else {
                        showSnackbar(String.format("%s", gameServer.getProtocol().getResponseStatus()));
                    }
                } else {
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
        Game game = gameSpinnerAdapter.getItem("Star Wars Jedi Knight 2: Jedi Outcast");
        int position = gameSpinnerAdapter.getPosition(game);
        gameSpinner.setSelection(position, true);

        addressTextView.setText("85.25.149.26");
        portTextView.setText("28070");
    }

    @OptionsItem(R.id.menu_add_server_myacxy)
    void addMyacxy(MenuItem item)
    {
        Game game = gameSpinnerAdapter.getItem("Star Wars Jedi Knight 2: Jedi Outcast");
        int position = gameSpinnerAdapter.getPosition(game);
        gameSpinner.setSelection(position, true);

        addressTextView.setText("myacxy.net");
        portTextView.setText("28070");
    }

    @Click(R.id.fab)
    protected void doneSelected()
    {
        if(validateInput())
        {
            Game game = (Game) gameSpinner.getSelectedItem();
            String address = addressTextView.getText().toString().trim();
            int port = Integer.parseInt(portTextView.getText().toString().trim());

            initializeServer(game, address, port);
            doneButton.setIndeterminate(true);
            doneButton.setClickable(false);
        }
    }

    @OptionsItem(android.R.id.home)
    boolean homePressed()
    {
        onBackPressed();
        return false;
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
        String selection = spinner.getSelectedItem().toString();
        if(selection.equals("select a game"))
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

} // AddServerFragment
