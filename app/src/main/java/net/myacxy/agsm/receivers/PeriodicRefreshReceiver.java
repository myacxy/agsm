package net.myacxy.agsm.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

import net.myacxy.agsm.AgsmService_;
import net.myacxy.agsm.utils.SharedItems;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.annotations.SystemService;

@EReceiver
public class PeriodicRefreshReceiver extends WakefulBroadcastReceiver
{
    @SystemService AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // empty, will be overridden in generated subclass
    }

    @ReceiverAction(actions = SharedItems.Action.Service.PERIODIC_REFRESH)
    void periodicRefresh(Context context)
    {
        startWakefulService(context, getIntent(context, SharedItems.UpdateReason.PERIODIC));
    }

    private Intent getIntent(Context context, int reason)
    {
        return AgsmService_.intent(context)
                .action(SharedItems.Action.Server.UPDATE_SERVERS)
                .extra(SharedItems.Extra.UPDATE_REASON, reason)
                .get();
    }

    @ReceiverAction(actions = SharedItems.Action.Service.ENSURE_PERIODIC_REFRESH)
    void ensurePeriodicRefresh(Context context)
    {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0, // request code?
                new Intent(
                        context,
                        PeriodicRefreshReceiver_.class
                ).setAction(SharedItems.Action.Service.PERIODIC_REFRESH),
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        // trigger update between 15 to 45 minutes from now
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_HALF_HOUR,
                pendingIntent
        );
//        alarmManager.setInexactRepeating(
//                AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + 1000,
//                10000,
//                pendingIntent
//        );
    }
}
