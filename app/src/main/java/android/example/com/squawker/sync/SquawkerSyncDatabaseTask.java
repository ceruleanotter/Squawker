/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package android.example.com.squawker.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;

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
    // The website where we can download all squawk messages as JSON. The JSON format looks like
    // this :
    //    {
    //        "author": "TheRealAsser",
    //        "message": "Meanwhile in Australia...",
    //        "date": 1484358455343
    //    }
    private static final String SQUAWK_MESSAGES_URL = "http://10.1.10.172:8080/messages";

    /**
     * Syncs the local phone data with the server. This is done by downloading all of the messages
     * from the server and replacing the local cache. This is a *very* naive way to deal with
     * syncing our data because it will re-download a lot of data. We'll talk about less "toy"
     * ways to deal with syncing in the course.
     */
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

    /**
     * Gets the message JSON from the Squawk messages site
     *
     * @return A String containing all the messages in JSON
     */
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

    /**
     * Converts message JSON to content values
     *
     * @param json The JSON filled with squawks
     * @return A ContentValues array is all of the messages
     */
    private static ContentValues[] parseContentValuesFromJson(String json) throws JSONException {
        JSONArray jsonSquawkArray = new JSONArray(json);
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
