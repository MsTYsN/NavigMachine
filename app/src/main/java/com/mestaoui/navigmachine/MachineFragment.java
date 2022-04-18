package com.mestaoui.navigmachine;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mestaoui.navigmachine.adapter.MachineAdapter;
import com.mestaoui.navigmachine.beans.Machine;
import com.mestaoui.navigmachine.beans.Marque;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineFragment extends Fragment {
    private static final String TAG = "MachineFragment";
    private RecyclerView recyclerView;
    private MachineAdapter machineAdapter = null;
    RequestQueue requestQueue;
    String url = "http://10.0.2.2:8090/machines/";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MachineFragment() {
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
    public static MachineFragment newInstance(String param1, String param2) {
        MachineFragment fragment = new MachineFragment();
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
        View v = inflater.inflate(R.layout.fragment_machine, container, false);
        recyclerView = v.findViewById(R.id.recycle);
        setHasOptionsMenu(true);
        loadData();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (machineAdapter != null){
                    machineAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void loadData() {
        List<Machine> machineList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest (Request.Method.GET,
                url + "all/" , null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject machine = response.getJSONObject(i);
                                    machineList.add(new Machine(machine.getInt("id"), machine.getString("reference"), machine.getString("dateAchat"), machine.getDouble("prix"), new Marque(machine.getJSONObject("marque").getInt("id"), machine.getJSONObject("marque").getString("code"), machine.getJSONObject("marque").getString("libelle"))));
                                }
                                machineAdapter = new MachineAdapter(getActivity().getApplicationContext(), machineList);
                                recyclerView.setAdapter(machineAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
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
}
