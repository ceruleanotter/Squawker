package android.example.com.squawker.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by lyla on 12/23/16.
 */

@Database(
        version = SquawkDatabase.VERSION)
public class SquawkDatabase {

    public static final int VERSION = 2;

    @Table(SquawkContract.class)
    public static final String SQUAWK_MESSAGES = "squawk_messages";

}
