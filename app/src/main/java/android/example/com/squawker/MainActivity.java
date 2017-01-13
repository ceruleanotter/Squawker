package android.example.com.squawker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.example.com.squawker.data.SquawkContract;
import android.example.com.squawker.data.SquawkProvider;
import android.example.com.squawker.fcm.SquawkFirebaseMessageService;
import android.example.com.squawker.settings.SettingsActivity;
import android.example.com.squawker.sync.SyncSquawksIntentService;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID_MESSAGES = 0;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SquawkAdapter mAdapter;

    static final String[] MESSEGES_PROJECTION = {
            SquawkContract.COLUMN_AUTHOR,
            SquawkContract.COLUMN_MESSAGE,
            SquawkContract.COLUMN_DATE
    };

    static final int COL_NUM_AUTHOR = 0;
    static final int COL_NUM_MESSAGE = 1;
    static final int COL_NUM_DATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_squawks);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Add dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // specify an adapter (see also next example)
        mAdapter = new SquawkAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Load it up
        getSupportLoaderManager().initLoader(LOADER_ID_MESSAGES, null, this);

        //Get Token
        String token = FirebaseInstanceId.getInstance().getToken();

        // Toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(LOG_TAG, msg);

        // Load initial messages
        // TODO only load inital message if empty
        //SyncSquawksIntentService.startImmediateSync(this);



        // TODO fix this epic mess
        // Check if this was opened by a Test FCM notification
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("test")) {
                // TODO is this bad because it's on the main thread?

                ContentValues newMessage = new ContentValues();
                newMessage.put(SquawkContract.COLUMN_AUTHOR,
                        extras.getString(SquawkContract.COLUMN_AUTHOR, "null"));
                newMessage.put(SquawkContract.COLUMN_MESSAGE,
                        extras.getString(SquawkContract.COLUMN_MESSAGE, "null").trim());
                newMessage.put(SquawkContract.COLUMN_DATE,
                        extras.getString(SquawkContract.COLUMN_DATE, "null"));
                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newMessage);

            } else if (getIntent().getAction().equals("SYNC_DATA_WITH_SQUAWKER")) {
                Log.e(LOG_TAG, "Sync now");
                SyncSquawksIntentService.startImmediateSync(this);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        if (id == R.id.action_refresh) {
            onRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, SquawkProvider.SquawkMessages.CONTENT_URI,
                MESSEGES_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void onRefresh() {
        Log.e("MainActivity", "REFRESHED");
        SyncSquawksIntentService.startImmediateSync(this);

    }
}
