package com.couragedigital.petapp;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.Adapter.DrawerAdapter;
import com.couragedigital.petapp.model.DrawerItems;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {


    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    TextView txtlogout;
    SessionManager sessionManager;
    TextView lblName;
    TextView lblEmail;
    RecyclerView listItems;                           // Declaring RecyclerView
    RecyclerView.Adapter adapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager layoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout drawer;
    FrameLayout frameLayout;
    LinearLayout linearLayout;
    public ArrayList<DrawerItems> itemDatastArrayList;

    @Override
    public void setContentView(int layoutResID) {
        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer, null);
        frameLayout = (FrameLayout) drawer.findViewById(R.id.contentFrame);
        linearLayout = (LinearLayout) drawer.findViewById(R.id.drawerlinearlayout);
        listItems = (RecyclerView) drawer.findViewById(R.id.drawerListItem);

        final String[] title = new String[]{"Home", "Profile","MyListings" ,"Account Setting" ,"FeedBack" , "Share","LogOut"};
        final int[] icons = {R.drawable.home, R.drawable.profile,R.drawable.mylisting,R.drawable.setting,0,0,0};
        itemDatastArrayList = new ArrayList<DrawerItems>();
        for (int i = 0; i < title.length; i++) {
            DrawerItems drawerItems = new DrawerItems();
            drawerItems.setTittle(title[i]);
            drawerItems.setIcons(icons[i]);
            itemDatastArrayList.add(drawerItems);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listItems.setLayoutManager(linearLayoutManager);
        DrawerAdapter mAdapter = new DrawerAdapter(itemDatastArrayList, drawer);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        getLayoutInflater().inflate(layoutResID, linearLayout, true);
        drawer.setClickable(true);
        listItems.setAdapter(mAdapter);

        toolbar = (Toolbar) drawer.findViewById(R.id.app_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        super.setContentView(drawer);
        sessionManager = new SessionManager(getApplicationContext());
        lblName = (TextView) findViewById(R.id.lblname);
        lblEmail = (TextView) findViewById(R.id.lblemail);

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME); // get name
        String email = user.get(SessionManager.KEY_EMAIL);  // get email
     //   lblName.setText(name);   // Show user data on activity
        lblEmail.setText(email);
    }
}