package android.example.com.squawker.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.example.com.squawker.data.SquawkContract;
import android.example.com.squawker.data.SquawkProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by lyla on 1/12/17.
 */

public class SquawkerSyncDatabaseTask {
    private static final String SQUAWK_MESSAGES_URL = "http://10.44.107.100:8080/messages";

    synchronized public static void syncWithServer(Context context) {
        String json = null;
        ContentValues[] squawksFromServer = null;
        try {
            json = getJsonResponseFromWeb();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            squawksFromServer = parseContentValuesFromJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // When we download the new squawks, we delete our current cache and add everything that
        // was sent down
        if (squawksFromServer != null && squawksFromServer.length > 0) {
            ContentResolver resolver = context.getContentResolver();
            resolver.delete(SquawkProvider.SquawkMessages.CONTENT_URI, null, null);
            resolver.bulkInsert(SquawkProvider.SquawkMessages.CONTENT_URI,
                    squawksFromServer);
        }

    }

    private static String getJsonResponseFromWeb() throws IOException {
        URL messagesUrl = new URL(SQUAWK_MESSAGES_URL);
        HttpURLConnection urlConnection = (HttpURLConnection) messagesUrl.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    private static ContentValues[] parseContentValuesFromJson(String json) throws JSONException {

        JSONArray jsonSquawkArray = new JSONArray(json);

        // Might need to add some error checking

        ContentValues[] squawkContentValues = new ContentValues[jsonSquawkArray.length()];

        for (int i = 0; i < jsonSquawkArray.length(); i++) {

            JSONObject messageJson = jsonSquawkArray.getJSONObject(i);

            long date = messageJson.getLong(SquawkContract.COLUMN_DATE);
            String message = messageJson.getString(SquawkContract.COLUMN_MESSAGE);
            String author = messageJson.getString(SquawkContract.COLUMN_AUTHOR);

            ContentValues squawkMessageValues = new ContentValues();
            squawkMessageValues.put(SquawkContract.COLUMN_DATE, date);
            squawkMessageValues.put(SquawkContract.COLUMN_MESSAGE, message);
            squawkMessageValues.put(SquawkContract.COLUMN_AUTHOR, author);

            squawkContentValues[i] = squawkMessageValues;
        }
        return squawkContentValues;
    }

}
