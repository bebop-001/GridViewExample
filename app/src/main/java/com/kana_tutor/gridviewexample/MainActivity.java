package com.kana_tutor.gridviewexample;

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
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static List<String> stringIntList = new ArrayList<>(1024);
    static {
        for (int i = 1; i < 1024; i++)
            stringIntList.add(String.format("%03x", i));
    }

    GridView gridView;
    StringGridAdapter stringGridAdapter;
    private int selectedItemPosition = 0;

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
        Log.d(TAG, "onResume: selection = " + gridView.getSelectedItemPosition());
        gridView.setSelection(selectedItemPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    private static int vhId;
    private class ViewHolder {
        int id = vhId++;
        long itemId = -1;
        int position = -1;
    }

    public void changeRowOnClick(View view) {
        int columns = gridView.getNumColumns();
        int id = view.getId();
        if (id == R.id.next_row) {
            selectedItemPosition += columns;
            if (selectedItemPosition >= stringIntList.size())
                selectedItemPosition = stringIntList.size() - 1;
        }
        else if (id == R.id.previous_row) {
            selectedItemPosition -= columns;
            if (selectedItemPosition < 0)
                selectedItemPosition = 0;
        }
        Log.d(TAG, "nextRowOnClick");
        // position from setSelection isn't necessarily related
        // to getSelectedItemPosition.  Depends on touch mode, etc..??
        // int position = gridView.getSelectedItemPosition();
        gridView.setSelection(selectedItemPosition);
        gridView.invalidate();
    }
    private static Toast onClickToast;
    public void stringIntOnClick(View v) {
        ViewHolder vh = (ViewHolder)v.getTag();
        selectedItemPosition = vh.position;
        gridView.setSelection(selectedItemPosition);
        if (onClickToast != null)
            onClickToast.cancel();
        onClickToast = Toast.makeText(this, "Button ID:" + vh.itemId
                + " Text:" + ((Button)v).getText(), Toast.LENGTH_SHORT);
        onClickToast.show();
    }

    private static int buttonCounter;
    private class StringGridAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button button = (Button)convertView;
            Log.d(TAG,"getView:position:" + position);
            if (button == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                button = (Button)inflater.inflate(R.layout.string_int_textview, null, false);
                Log.d(TAG, "getView:button:" + buttonCounter++);
            }
            ViewHolder vh = (ViewHolder)button.getTag();
            if (vh == null) {
                vh = new ViewHolder();
                button.setTag(vh);
            }
            if (vh.itemId != getItemId(position)) {
                vh.itemId = getItemId(position);
                vh.position = position;
                button.setText((String)getItem(position));
            }
            Log.d(TAG, String.format(
                "getView: position:%d, text:\"%s\", vh.id:%d"
                , position, getItem(position), vh.id));
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
