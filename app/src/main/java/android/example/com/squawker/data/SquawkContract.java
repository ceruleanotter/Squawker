package android.example.com.squawker.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by lyla on 12/23/16.
 */

public class SquawkContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR = "title";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_MESSAGE = "message";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_DATE = "date";


}
