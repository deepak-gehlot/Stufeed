package com.stufeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stufeed.R;
import com.stufeed.pojo.Board;

import java.util.List;

/**
 * Created by sowmitras on 16/12/17.
 */

public class BoardListAdapter  extends ArrayAdapter<Board> {

    public BoardListAdapter(Context context, List<Board> boards) {
        super(context, 0, boards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.multi_text_item, parent, false);
        }

        Board board = getItem(position);
        TextView top = (TextView) convertView.findViewById(R.id.top_text);
        top.setText(board.getBoard_name());
        TextView bottom = (TextView) convertView.findViewById(R.id.bottom_text);
        bottom.setText(board.getBoard_key());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getBoard_name().hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}