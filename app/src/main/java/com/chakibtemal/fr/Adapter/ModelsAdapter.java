package com.chakibtemal.fr.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chakibtemal.fr.androidproject.R;
import com.chakibtemal.fr.modele.sharedResources.AllDataForRunActivity;

import java.util.ArrayList;
import java.util.List;

public class ModelsAdapter extends BaseAdapter {

    private List<AllDataForRunActivity> mData = new ArrayList<AllDataForRunActivity>();
    private Context mContext;

    public ModelsAdapter(List<AllDataForRunActivity> mData , Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return  mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.models_item, null);

        TextView nameSensor = (TextView) root.findViewById(R.id.nameSensor);
        AllDataForRunActivity allDataForRunActivity = (AllDataForRunActivity) getItem(position);
        nameSensor.setText(allDataForRunActivity.getRunMode().getNameMode());


        return root;
    }
}
