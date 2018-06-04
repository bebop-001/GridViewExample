package com.kana_tutor.gridviewexample;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static List<String> stringIntList = new ArrayList<>(1024);
    static {
        for (int i = 1; i < 1024; i++)
            stringIntList.add(String.format("%03x", i));
    }

    GridView gridView;
    StringGridAdapter stringGridAdapter;
    private static Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.grid_view);
        stringGridAdapter = new StringGridAdapter();
        gridView.setAdapter(stringGridAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        if (state != null)
            gridView.onRestoreInstanceState(state);
            */
        // gridView.scrollTo(0, 1200);
        gridView.setSelection(55);
        gridView.requestLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = gridView.onSaveInstanceState();
    }

    private class ViewHolder {
        long itemId = -1;
    }

    public void nextRowOnClick(View view) {
        Log.d(TAG, "nextRowOnClick");
    }
    private static Toast onClickToast;
    public void stringIntOnClick(View v) {
        ViewHolder vh = (ViewHolder)v.getTag();
        if (vh != null) {
            if (onClickToast != null)
                onClickToast.cancel();
            onClickToast = Toast.makeText(this, "Button ID:" + vh.itemId
                    + " Text:" + ((Button)v).getText(), Toast.LENGTH_SHORT);
            onClickToast.show();
        }
    }

    private class StringGridAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button button = (Button)convertView;
            Log.d(TAG,"getView:position:" + position);
            if (button == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                button = (Button)inflater.inflate(R.layout.string_int_textview, null, false);
            }
            ViewHolder vh = (ViewHolder)button.getTag();
            if (vh == null) {
                vh = new ViewHolder();
                button.setTag(vh);
            }
            if (vh.itemId != getItemId(position)) {
                vh.itemId = getItemId(position);
                button.setText((String)getItem(position));
            }
            return button;
        }
        @Override
        public int getCount() {
            return stringIntList.size();
        }
        @Override
        public Object getItem(int position) {
            return stringIntList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return Integer.parseInt(stringIntList.get(position), 16);
        }
    }
}
