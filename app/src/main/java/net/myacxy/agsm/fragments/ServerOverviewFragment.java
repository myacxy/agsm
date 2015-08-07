package net.myacxy.agsm.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.myacxy.agsm.R;
import net.myacxy.agsm.views.adapters.ServerOverviewGeneralAdapter;
import net.myacxy.agsm.views.adapters.ServerOverviewPlayersAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_server_overview)
public class ServerOverviewFragment extends Fragment
{
    @Bean(ServerOverviewGeneralAdapter.class)
    ServerOverviewGeneralAdapter generalAdapter;

    @Bean(ServerOverviewPlayersAdapter.class)
    ServerOverviewPlayersAdapter playersAdapter;

    @FragmentArg(ServerFragment.ARG_GAME_SERVER_ID)
    int gameServerId;

    @ViewById(R.id.server_overview_general_list)
    ListView generalList;

    @ViewById(R.id.server_overview_players_list)
    ListView playerList;

    @AfterViews
    void initialize()
    {
        generalAdapter.setGameServerId(gameServerId);
        generalList.setAdapter(generalAdapter);
        adjustHeightToHeightOfAllChildren(generalList);

        playersAdapter.setGameServerId(gameServerId);
        playerList.setAdapter(playersAdapter);
        adjustHeightToHeightOfAllChildren(playerList);

    }

    private int getTotalListViewHeight(ListView listView)
    {
        if(listView.getAdapter() == null) return 0;
        else if(listView.getAdapter().getCount() == 0) return 0;

        View view = listView.getAdapter().getView(0, null, listView);
        view.measure(0, 0);

        int totalViewHeight = view.getMeasuredHeight() * (generalAdapter.getCount() + 1);
        int totalDividerHeight = generalList.getDividerHeight() * (generalAdapter.getCount() - 1);

        return totalViewHeight + totalDividerHeight;
    }

    private void adjustHeightToHeightOfAllChildren(ListView listView)
    {
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = getTotalListViewHeight(listView);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
} // ServerOverviewFragment
