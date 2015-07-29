package net.myacxy.agsm.fragments;

import net.myacxy.agsm.BuildConfig;
import net.myacxy.agsm.MainActivity_;
import net.myacxy.agsm.R;
import net.myacxy.agsm.interfaces.GameFinder;
import net.myacxy.agsm.utils.JsonGameFinder_;
import net.myacxy.jgsq.model.Game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

import static junit.framework.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "app/src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk=21)
public class AddServerFragmentTest
{
    private MainActivity_ activity;
    private AddServerFragment_ fragment;

    @Before
    public void setUp()
    {
        // start activity
        activity = Robolectric.buildActivity(MainActivity_.class)
                .create()
                .start()
                .resume()
                .get();
        assertNotNull(activity);

        // show fragment
        fragment = new AddServerFragment_();
        activity.getSupportFragmentManager().beginTransaction().add(fragment, null).commit();
        assertNotNull(fragment.getView());
    }

    @Test
    public void checkViews()
    {
        assertNotNull(fragment.doneButton);
        assertNotNull(fragment.gameSpinner);
        assertNotNull(fragment.addressTextView);
        assertNotNull(fragment.portTextView);
        assertNotNull(fragment.queryPortTextView);
    }

    @Test
    public void checkGameSpinner()
    {
        MaterialSpinner spinner = fragment.gameSpinner;
        // check initialization
        assertNotNull(spinner);
        assertNotNull(fragment.gameSpinnerAdapter);
        // actual adapter is a HintAdapter
        assertNotSame(spinner.getAdapter(), fragment.gameSpinnerAdapter);

        // compare supported games size
        GameFinder gf = JsonGameFinder_.getInstance_(activity);
        gf.setConfig("games.conf.json");
        Map<String, Game> gamesMap = gf.findAll();
        List<Game> gamesList = new ArrayList<>(gamesMap.values());
        assertNotNull(fragment.gameFinder);
        assertEquals(fragment.gameFinder.findAll().size(), gamesMap.size());

        // check hint
        String gameHint = activity.getResources().getString(R.string.server_add_game_hint);
        assertEquals(spinner.getAdapter().getItem(0), gameHint);
        // check item count (take material spinner hint into account)
        assertEquals(spinner.getCount(), gamesMap.size() + 1);
        // first game (actual adapter index +1 due to hint)
        String firstGameName = spinner.getAdapter().getItem(1).toString();
        assertEquals(gamesList.get(0).name, firstGameName);
        // last game
        int index = gamesList.size() - 1;
        String lastGameName = spinner.getAdapter().getItem(index + 1).toString();
        assertEquals(gamesList.get(index).name, lastGameName);

    }
} // AddServerFragmentTest