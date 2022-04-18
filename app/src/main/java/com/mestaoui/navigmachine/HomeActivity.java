package com.mestaoui.navigmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            MachineFragment machineFragment = new MachineFragment();
            FragmentManager fm1 = getFragmentManager();
            FragmentTransaction ft1 = fm1.beginTransaction();
            ft1.replace(R.id.fragment_container, machineFragment);
            ft1.commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_machine:
                        MachineFragment machineFragment = new MachineFragment();
                        FragmentManager fm1 = getFragmentManager();
                        FragmentTransaction ft1 = fm1.beginTransaction();
                        ft1.replace(R.id.fragment_container, machineFragment);
                        ft1.commit();
                        break;
                    case R.id.nav_graph1:
                        GraphMFragment graphMFragment = new GraphMFragment();
                        FragmentManager fm2 = getFragmentManager();
                        FragmentTransaction ft2 = fm2.beginTransaction();
                        ft2.replace(R.id.fragment_container, graphMFragment);
                        ft2.commit();
                        break;
                    case R.id.nav_graph2:
                        GraphAFragment graphAFragment = new GraphAFragment();
                        FragmentManager fm3 = getFragmentManager();
                        FragmentTransaction ft3 = fm3.beginTransaction();
                        ft3.replace(R.id.fragment_container, graphAFragment);
                        ft3.commit();
                        break;
                    case R.id.nav_logout:
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}