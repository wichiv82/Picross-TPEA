package com.example.picross;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LevelsScreen extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    private LevelsScreenActivity activity;

    public LevelsScreen(ArrayList<String> list, LevelsScreenActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_layout, null);
        }

        TextView level = view.findViewById(R.id.level);
        level.setText(list.get(position).split("\\.")[0]);

        Button continueGameButton= view.findViewById(R.id.continueGame);
        Button newGameButton= view.findViewById(R.id.newGame);

        continueGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String level_name = list.get(position);
                Intent intent = new Intent(activity, GameActivity.class);
                intent.putExtra("level", level_name);
                intent.putExtra("launch", "CONTINUE");
                activity.startActivity(intent);
                activity.finish();
            }
        });

        newGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String level_name = list.get(position);
                Intent intent = new Intent(activity, GameActivity.class);
                intent.putExtra("level", level_name);
                intent.putExtra("launch", "NEW");
                activity.startActivity(intent);
                activity.finish();
            }
        });

        return view;
    }
}
