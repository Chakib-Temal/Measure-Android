package com.chakibtemal.fr.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chakibtemal.fr.modele.service.ItemSpinner;
import com.chakibtemal.fr.modele.sharedResources.ComplexSensor;
import com.chakibtemal.fr.androidproject.R;

import java.util.ArrayList;
import java.util.List;

public class BasicSpinnerAdapter extends BaseAdapter {

    private List<ItemSpinner> mSpinnerItems = new ArrayList<ItemSpinner>();
    private List<ComplexSensor> mData = new ArrayList<ComplexSensor>();
    private Context mContext;

    public BasicSpinnerAdapter(List<ComplexSensor> mData , List<ItemSpinner> mSpinnerItems , Context mContext) {
        this.mSpinnerItems = mSpinnerItems;
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
        View root = inflater.inflate(R.layout.sensor_item, null);

        TextView nameSensor = (TextView) root.findViewById(R.id.nameSensor);
        final ComplexSensor sensor = (ComplexSensor) getItem(position);
        nameSensor.setText(sensor.getSensor().getName());

        final Spinner spinner = (Spinner) root.findViewById(R.id.spinner1);
        ArrayAdapter<ItemSpinner> adapter = new ArrayAdapter<ItemSpinner>(mContext, android.R.layout.simple_list_item_1, mSpinnerItems);
        spinner.setAdapter(adapter);

        spinner.setFocusable(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ItemSpinner item = (ItemSpinner) spinner.getSelectedItem();
               sensor.getDataOfSensor().setFrequency(item.getFrequency());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        return root;
    }

}
