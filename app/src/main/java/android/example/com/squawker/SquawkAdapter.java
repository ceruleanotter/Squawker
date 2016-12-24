package android.example.com.squawker;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by lyla on 12/23/16.
 */

public class SquawkAdapter extends RecyclerView.Adapter<SquawkAdapter.SquawkViewHolder> {

    private Cursor mData;

    @Override
    public SquawkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SquawkViewHolder holder, int position) {
        mData.moveToPosition(position);

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

        public SquawkViewHolder(View layoutView) {
            super(layoutView);
            authorTextView = (TextView) layoutView.findViewById(R.id.text_view_author);
            messageTextView = (TextView) layoutView.findViewById(R.id.text_view_message);
            dateTextView = (TextView) layoutView.findViewById(R.id.text_view_date);
        }
    }
}
