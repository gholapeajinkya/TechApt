package com.example.admin.techapt;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SolvedTestsAdapter extends BaseAdapter {

    ArrayList<String> teststringarr,datestringarr;
    TextView test,data;
    Context context;
    LayoutInflater layoutInflater;
    //
    SparseBooleanArray mSelectedItemsIds;
    public SolvedTestsAdapter(ArrayList<String> teststringarr, ArrayList<String> datestringarr,Context context) {
        this.teststringarr = teststringarr;
        this.datestringarr = datestringarr;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() { return teststringarr.size(); }

    @Override
    public Object getItem(int i) { return this.teststringarr.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi=view;
        Log.e("Test name: ","IN SOLVEDTESTSADPTER");
        vi = layoutInflater.inflate(R.layout.solvedtestslayout, null);
        test = (TextView) vi.findViewById(R.id.solvedtestname);
        data = (TextView) vi.findViewById(R.id.solvedtestdata);

        String testname = (String)getItem(i);
        String datestr = datestringarr.get(i);
        Log.e("Test name: ",testname);
        test.setText(testname);
        data.setText(datestr);

        return vi;
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void remove(SolvedTests object) {
        teststringarr.remove(object);
        notifyDataSetChanged();
    }
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
}
