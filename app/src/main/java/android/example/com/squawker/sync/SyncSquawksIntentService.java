package android.example.com.squawker.sync;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SyncSquawksIntentService extends IntentService {


    public SyncSquawksIntentService() {
        super("SyncSquawksIntentService");
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, SyncSquawksIntentService.class);
        context.startService(intentToSyncImmediately);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SquawkerSyncDatabaseTask.syncWithServer(this);
    }

}
