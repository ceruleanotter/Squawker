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
package android.example.com.squawker.provider;

import android.content.SharedPreferences;
import android.example.com.squawker.R;
import android.support.v7.preference.PreferenceManager;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to define the columns in a
 * content provider baked by a database
 */

public class SquawkContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_MESSAGE = "message";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_DATE = "date";


    // Names and topic keys as matching what is found in the database
    public static final String ASSER_NAME = "TheRealAsser";
    public static final String CEZANNE_NAME = "TheRealCezanne";
    public static final String JLIN_NAME = "TheRealJLin";
    public static final String LYLA_NAME = "TheRealLyla";
    public static final String NIKITA_NAME = "TheRealNikita";

    public static final String ASSER_KEY = "key_asser";
    public static final String CEZANNE_KEY = "key_cezanne";
    public static final String JLIN_KEY = "key_jlin";
    public static final String LYLA_KEY = "key_lyla";
    public static final String NIKITA_KEY = "key_nikita";

    public static final String[] INSTRUCTOR_KEYS = {
            ASSER_KEY, CEZANNE_KEY, JLIN_KEY, LYLA_KEY, NIKITA_KEY
    };

    public static String nameToKey(String name) {
        switch (name) {
            case ASSER_NAME: return ASSER_KEY;
            case CEZANNE_NAME: return CEZANNE_KEY;
            case JLIN_NAME: return JLIN_KEY;
            case LYLA_NAME: return LYLA_KEY;
            case NIKITA_NAME: return NIKITA_KEY;
        }
        return null;
    }

    public static String keyToName(String key) {
        switch (key) {
            case ASSER_KEY: return ASSER_NAME;
            case CEZANNE_KEY: return CEZANNE_NAME;
            case JLIN_KEY: return JLIN_NAME;
            case LYLA_KEY: return LYLA_NAME;
            case NIKITA_KEY: return NIKITA_NAME;
        }
        return null;
    }

    public static String createSelectionForCurrentFollowers(SharedPreferences preferences) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(COLUMN_AUTHOR + " IN  (");
        String placeholder = "";

        for (String key : INSTRUCTOR_KEYS) {
            if (preferences.getBoolean(key, false)) {
                stringBuilder.append(placeholder);
                stringBuilder.append("'" + keyToName(key) + "'");
                placeholder = ",";
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
