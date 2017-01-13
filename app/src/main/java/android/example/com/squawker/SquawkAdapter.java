package android.example.com.squawker;

import android.database.Cursor;
import android.example.com.squawker.views.CircularImageView;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by lyla on 12/23/16.
 */

public class SquawkAdapter extends RecyclerView.Adapter<SquawkAdapter.SquawkViewHolder> {


    private Cursor mData;
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM");

    private static final long DAY_MILLIS = 24 * 60 * 60 * 1000;
    private static final long HOUR_MILLIS = 60 * 60 * 1000;
    private static final long MINUTE_MILLIS = 1000 * 60;

    @Override
    public SquawkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.squawk_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        SquawkViewHolder vh = new SquawkViewHolder(v);

        // To update time stamps
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                SquawkAdapter.this.notifyDataSetChanged();
                handler.postDelayed(this, 60 * 1000);
            }
        }, 60 * 1000);
        return vh;
    }

    @Override
    public void onBindViewHolder(SquawkViewHolder holder, int position) {
        mData.moveToPosition(position);

        String message = mData.getString(MainActivity.COL_NUM_MESSAGE);
        String author = mData.getString(MainActivity.COL_NUM_AUTHOR);

        // Get the date for displaying
        long dateMillis = mData.getLong(MainActivity.COL_NUM_DATE);
        String date = "";
        long now = System.currentTimeMillis();
        if (now - dateMillis < (DAY_MILLIS)) {
            if (now - dateMillis < (HOUR_MILLIS)) {
                long minutes = Math.round((now - dateMillis) / MINUTE_MILLIS);
                date = String.valueOf(minutes) + "m";
            } else {
                long minutes = Math.round((now - dateMillis) / HOUR_MILLIS);
                date = String.valueOf(minutes) + "h";
            }
        } else {
            Date dateDate = new Date(dateMillis);
            date = sDateFormat.format(dateDate);
        }

        date = "\u2022 " + date;

        holder.messageTextView.setText(message);
        holder.authorTextView.setText(author);
        holder.dateTextView.setText(date);

        switch (author) {
            case "TheRealAsser":
                holder.authorCircularImageView.setImageResource(R.drawable.asser);
                break;
            case "TheRealCezanne":
                holder.authorCircularImageView.setImageResource(R.drawable.cezanne);
                break;
            case "TheRealJlin":
                holder.authorCircularImageView.setImageResource(R.drawable.jlin);
                break;
            case "TheRealLyla":
                holder.authorCircularImageView.setImageResource(R.drawable.lyla);
                break;
            case "TheRealNikita":
                holder.authorCircularImageView.setImageResource(R.drawable.nikita);
                break;
            default:
                holder.authorCircularImageView.setImageResource(R.drawable.test);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mData = newCursor;
        notifyDataSetChanged();
    }

    public class SquawkViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTextView;
        final TextView messageTextView;
        final TextView dateTextView;
        final CircularImageView authorCircularImageView;

        public SquawkViewHolder(View layoutView) {
            super(layoutView);
            authorTextView = (TextView) layoutView.findViewById(R.id.text_view_author);
            messageTextView = (TextView) layoutView.findViewById(R.id.text_view_message);
            dateTextView = (TextView) layoutView.findViewById(R.id.text_view_date);
            authorCircularImageView = (CircularImageView) layoutView.findViewById(
                    R.id.circular_image_view_author);
        }
    }
}
