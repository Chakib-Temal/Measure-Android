package com.chakibtemal.fr.modele.drawingGraphs;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.chakibtemal.fr.androidproject.R;
import com.chakibtemal.fr.modele.valuesSensorModel.ValueOfSensor;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;

public class DrawGraphHelper {
    private Context context;

    public DrawGraphHelper(Context context){
        this.context = context;
    }

    public void prepareGraphe(int CompterIndex, ValueOfSensor[] sensorValue, String title, int size, List<View> listGraphView) {
        if (size == 3) {
            GraphView graph = new GraphView(context);
            List<LineGraphSeries<DataPoint>> list = new ArrayList<LineGraphSeries<DataPoint>>();

            DataPoint [] d = new DataPoint[CompterIndex];

            long starTime = sensorValue[0].getTime();
            getDataSerie(d,CompterIndex, sensorValue, starTime, 0);
            list.add(this.getNewSerie(d, Color.GREEN, "X"));

            getDataSerie(d,CompterIndex, sensorValue, starTime, 1);
            list.add(this.getNewSerie(d, Color.BLACK, "Y"));

            getDataSerie(d,CompterIndex, sensorValue, starTime, 2);
            list.add(this.getNewSerie(d, Color.RED, "Z"));

            pushSeries(list, graph);
            scaleGraphX(sensorValue, CompterIndex, starTime, graph);

            graph.setTitle(title);
            listGraphView.add(graph);


            LinearLayout parent = new LinearLayout(context);
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            lp.weight = 1;

            GraphView graphX = new GraphView(context);
            getDataSerie(d, CompterIndex, sensorValue, starTime, 0);
            scaleGraphX(sensorValue, CompterIndex, starTime, graphX);
            graphX.addSeries(this.getNewSerie(d, Color.GREEN, "X"));
            graphX.setTitle(title + " -> X");
            parent.addView(graphX, lp);


            GraphView graphY = new GraphView(context);
            getDataSerie(d, CompterIndex, sensorValue, starTime, 1);
            scaleGraphX(sensorValue, CompterIndex, starTime, graphY);
            graphY.addSeries(this.getNewSerie(d, Color.BLACK, "Y"));
            graphY.setTitle(title + " -> Y");
            parent.addView(graphY, lp);

            GraphView graphZ = new GraphView(context);
            getDataSerie(d, CompterIndex, sensorValue, starTime, 2);
            scaleGraphX(sensorValue, CompterIndex, starTime, graphZ);
            graphZ.addSeries(this.getNewSerie(d, Color.RED, "Z"));
            graphZ.setTitle(title + " -> Z");
            parent.addView(graphZ, lp);

            listGraphView.add(parent);

        }else if (size == 1){
            try{
                GraphView graph = new GraphView(context);
                DataPoint [] d = new DataPoint[CompterIndex];
                List<LineGraphSeries<DataPoint>> list = new ArrayList<LineGraphSeries<DataPoint>>();
                long starTime = sensorValue[0].getTime();

                getDataSerie(d, CompterIndex, sensorValue, starTime, 0);
                scaleGraphX(sensorValue, CompterIndex, starTime, graph);
                graph.addSeries(this.getNewSerie(d, Color.GREEN, "X"));
                graph.setTitle(title);
                listGraphView.add(graph);
            }catch (Exception e){
                e.getStackTrace();
            }
        }
    }

    public void getDataSerie(DataPoint [] d, int CompterIndex,ValueOfSensor[] sensorValue, long starTime, int codeValue){
        for (int i=0 ; i < CompterIndex ; i++){
            d[i] = new DataPoint((sensorValue[i].getTime() - starTime)/1000000, sensorValue[i].getValues()[codeValue]);
        }
    }

    public  LineGraphSeries<DataPoint> getNewSerie(DataPoint [] d , int mcolor, String title){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(d);
        series.setColor(mcolor);
        series.setTitle(title);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(context, "Point : "+ dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
        return series;
    }

    public void putViewChildFlliper(List<View> listGraphView, ViewFlipper viewFlipperGraphs){
        for (int i=0; i < listGraphView.size(); i++){
            if( listGraphView.get(i) instanceof GraphView ) {
                GraphView graphView = (GraphView) listGraphView.get(i);
                graphView.getLegendRenderer().setVisible(true);
                graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                GridLabelRenderer gridLabel = graphView.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle(context.getResources().getString(R.string.TIMES));
                viewFlipperGraphs.addView(graphView, i);

            }else if (listGraphView.get(i) instanceof LinearLayout){
                LinearLayout parent = (LinearLayout) listGraphView.get(i);
                for (int j = 0; j< parent.getChildCount(); j++){
                    GraphView graphView = (GraphView) parent.getChildAt(j);
                    graphView.getLegendRenderer().setVisible(true);
                    graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                    GridLabelRenderer gridLabel = graphView.getGridLabelRenderer();
                    gridLabel.setHorizontalAxisTitle(context.getResources().getString(R.string.TIMES));
                }
                viewFlipperGraphs.addView(parent, i);
            }
        }
    }
    public void pushSeries(List<LineGraphSeries<DataPoint>> list, GraphView graph  ){
        for (LineGraphSeries<DataPoint> actualList : list){
            graph.addSeries(actualList);
        }
    }



    public void scaleGraphX(ValueOfSensor[] sensorValue, int CompterIndex, long starTime, GraphView graph){
        try {
            long milSecondeAllTime = (sensorValue[CompterIndex - 1].getTime() - starTime) / 1000000;
            long percentage = (CompterIndex * 10 ) / 100;
            long milSecondesForSample = (milSecondeAllTime / CompterIndex);
            graph.getViewport().setMaxX(milSecondeAllTime + milSecondesForSample * percentage);
            graph.getViewport().setXAxisBoundsManual(true);
        }catch (Exception e){
            e.getStackTrace();
        }
    }

}
