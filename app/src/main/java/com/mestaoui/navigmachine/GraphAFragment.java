package com.mestaoui.navigmachine;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphAFragment extends Fragment {
    private static final String TAG = "GraphMFragment";
    private BarChart chart;
    RequestQueue requestQueue;
    String url = "http://10.0.2.2:8090/machines/";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GraphAFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphAFragment newInstance(String param1, String param2) {
        GraphAFragment fragment = new GraphAFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graphannee, container, false);
        chart = v.findViewById(R.id.chart2);
        initializeBarChart();
        loadData();
        return v;
    }

    public void loadData() {
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest (Request.Method.GET,
                url + "byYear/" , null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null) {
                            try {
                                Map<String, String> map = new HashMap<>();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject machine = response.getJSONObject(i);
                                    map.put(machine.getString("annee"), machine.getString("nbr"));
                                }
                                createBarChart(map);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    private void initializeBarChart() {
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(4);
        chart.getXAxis().setDrawGridLines(false);
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(true);
        chart.getXAxis().setDrawGridLines(false);
        chart.animateY(1500);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setDrawLabels(true);
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.invalidate();
    }

    private void createBarChart(Map<String, String> map) {
        ArrayList<BarEntry> values = new ArrayList<>();
        int i = 0;
        for(String key : map.keySet()) {
            values.add(new BarEntry(i, Integer.parseInt(map.get(key))));
            ++i;
        }

        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setDrawValues(true);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            chart.setData(data);
            chart.setVisibleXRange(1,4);
            chart.setFitBars(true);
            XAxis xAxis = chart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(map.keySet()));
            for (IDataSet set : chart.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());
            chart.invalidate();
        }
    }
}
