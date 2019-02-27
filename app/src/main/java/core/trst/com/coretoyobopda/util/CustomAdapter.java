package core.trst.com.coretoyobopda.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.TreeMap;

import core.trst.com.coretoyobopda.R;

/**
 * Created by user on 9/2/2017.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    TreeMap<Integer, String> pData;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, TreeMap<Integer, String> pData) {
        this.context = applicationContext;
        this.pData = pData;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return pData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list_scan, null);
        TextView no = view.findViewById(R.id.tvNo);
        TextView data = view.findViewById(R.id.tvData);

        no.setText((getCount()-i) +" | ");
        data.setText(pData.get(i));
        return view;
    }
}