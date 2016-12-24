package android.example.com.squawker;

import android.content.ContentValues;
import android.database.Cursor;
import android.example.com.squawker.data.SquawkContract;
import android.example.com.squawker.data.SquawkProvider;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID_MESSAGES = 0;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SquawkAdapter mAdapter;

    static final String[] MESSEGES_PROJECTION =
            {
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

        // specify an adapter (see also next example)
        mAdapter = new SquawkAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //Load it up
        getSupportLoaderManager().initLoader(LOADER_ID_MESSAGES, null, this);

        saveDummyData();

    }

    private void saveDummyData() {
        AsyncTask saveDummyDataTask = new AsyncTask() {
            String[] dummyNames = {
                    "TheRealLyla","TheRealAsser","TheRealCezanne","TheRealJLin","TheRealNikita"
            };
            String[] dummyMessages = {
                    "Hello world", "LAWL right!", "I love java", "What a world we live in!",
                    "Merry Christmas", "Happy holidays"
            };

            private String pickRandom(String[] arr) {
                int pos = (int) (Math.random()*arr.length);
                return arr[pos];
            }


            @Override
            protected Object doInBackground(Object[] objects) {
                getContentResolver().delete(SquawkProvider.SquawkMessages.CONTENT_URI, null, null);
                ContentValues[] dummyData = new ContentValues[10];

                for (int i = 0; i < 10; i++) {
                    ContentValues cv = new ContentValues();
                    cv.put(SquawkContract.COLUMN_AUTHOR, pickRandom(dummyNames));
                    cv.put(SquawkContract.COLUMN_MESSAGE, pickRandom(dummyMessages));
                    cv.put(SquawkContract.COLUMN_DATE, System.currentTimeMillis());
                    dummyData[i] = cv;
                }

                getContentResolver().bulkInsert(SquawkProvider.SquawkMessages.CONTENT_URI, dummyData);
                return null;
            }
        };
        saveDummyDataTask.execute();
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

    public void onTokenClicked(View view) {
        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(LOG_TAG, msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
