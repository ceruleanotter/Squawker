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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Downloads message data off of the main thread
 */
public class SyncSquawksIntentService extends IntentService {


    public SyncSquawksIntentService() {
        super("SyncSquawksIntentService");
    }

    /**
     * Starts immediate synchronization with the server using this IntentService
     * @param context
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, SyncSquawksIntentService.class);
        context.startService(intentToSyncImmediately);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SquawkerSyncDatabaseTask.syncWithServer(this);
    }

}
