package com.couragedigital.petapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.couragedigital.petapp.Connectivity.FilterFetchPetList;
import com.couragedigital.petapp.Connectivity.PetFetchList;
import com.couragedigital.petapp.Connectivity.PetRefreshFetchList;
import com.couragedigital.petapp.Listeners.PetFetchListScrollListener;
import com.couragedigital.petapp.Adapter.PetListAdapter;
import com.couragedigital.petapp.Singleton.FilterPetListInstance;
import com.couragedigital.petapp.model.PetListItems;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;

public class PetList extends BaseActivity {

    private static final String TAG = PetList.class.getSimpleName();

    // http://c/dev/api/petappapi.php?method=showPetDetails&format=json
    // "http://storage.couragedigital.com/dev/api/petappapi.php"
    private static String url = "http://192.168.0.5/PetAppAPI/api/petappapi.php";
    private ProgressDialog progressDialog;
    public List<PetListItems> petLists = new ArrayList<PetListItems>();

    public List<PetListItems> petListsForFilter = new ArrayList<PetListItems>();
    public PetListAdapter adapterForFilter;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    SwipeRefreshLayout petListSwipeRefreshLayout;

    public List<PetListItems> originalpetLists = new ArrayList<PetListItems>();

    static String urlForFetch;

    private int current_page = 1;

    int filterState;

    int FILTER_STATE_RESULT = 1;

    public List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
    public List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
    public List<String> filterSelectedInstanceGenderList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAdoptionAndPriceList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petlist);

        /*petlistView = (ListView) findViewById(R.id.petList);
        Adapter = new PetListAdapter(this, petLists);
        petlistView.setAdapter(Adapter);*/

        recyclerView = (RecyclerView) findViewById(R.id.petList);
        petListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.petListSwipeRefreshLayout);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        url = url+"?method=showPetDetails&format=json&currentPage="+current_page+"";

        recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {
                url = url+"?method=showPetDetails&format=json&currentPage="+current_page+"";
                grabURL(url);
            }
        });

        recyclerView.smoothScrollToPosition(0);

        //recyclerView.fling(0,1);

        adapter = new PetListAdapter(petLists);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Pets...");
        progressDialog.show();

        petListSwipeRefreshLayout.setOnRefreshListener(petListSwipeRefreshListener);
        petListSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3,
                R.color.refresh_progress_4);

        grabURL(url);
    }

    public void grabURL(String url) {
        new FetchListFromServer().execute(url);
    }

    public class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetFetchList.petFetchList(petLists, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }

    private SwipeRefreshLayout.OnRefreshListener petListSwipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            PetListItems petListItems = petLists.get(0);
            String date = petListItems.getPetPostDate();
            //date = date.replace(" ", "+");
            try {
                url = url+"?method=showPetSwipeRefreshList&format=json&date="+ URLEncoder.encode(date, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            new FetchRefreshListFromServer().execute(url);
        }
    };

    public class FetchRefreshListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetRefreshFetchList.petRefreshFetchList(petLists, adapter, urlForFetch, petListSwipeRefreshLayout);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.petlistmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent filterClassIntent = new Intent(PetList.this, PetListFilter.class);
            startActivityForResult(filterClassIntent, FILTER_STATE_RESULT);
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle res = data.getExtras();
            filterState = res.getInt("Filter_State");
            if(filterState == 0) {
                petLists.clear();
                adapter.notifyDataSetChanged();
                url = url+"?method=showPetDetails&format=json&currentPage="+current_page+"";
                recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page){

                    @Override
                    public void onLoadMore(int current_page) {
                        url = url+"?method=showPetDetails&format=json&currentPage="+current_page+"";
                        grabURL(url);
                    }
                });

                petListSwipeRefreshLayout.setOnRefreshListener(petListSwipeRefreshListener);
                petListSwipeRefreshLayout.setColorSchemeResources(
                        R.color.refresh_progress_1,
                        R.color.refresh_progress_2,
                        R.color.refresh_progress_3,
                        R.color.refresh_progress_4);

                grabURL(url);
            }
            else if(filterState == 1) {
                FilterPetListInstance filterPetListInstance = new FilterPetListInstance();
                filterSelectedInstanceCategoryList = filterPetListInstance.getFilterCategoryListInstance();
                filterSelectedInstanceBreedList = filterPetListInstance.getFilterBreedListInstance();
                filterSelectedInstanceAgeList = filterPetListInstance.getFilterAgeListInstance();
                filterSelectedInstanceGenderList = filterPetListInstance.getFilterGenderListInstance();
                filterSelectedInstanceAdoptionAndPriceList = filterPetListInstance.getFilterAdoptionAndPriceListInstance();
                new FetchFilterPetListFromServer().execute();
            }
        }
    }

    public class FetchFilterPetListFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FilterFetchPetList.filterFetchPetList(petLists, adapter, filterSelectedInstanceCategoryList, filterSelectedInstanceBreedList, filterSelectedInstanceAgeList, filterSelectedInstanceGenderList, filterSelectedInstanceAdoptionAndPriceList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*@Override
    public void onRestart() {
        super.onRestart();
        /*if(PetListInstance.getListInstance() != originalpetLists) {
            petLists = PetListInstance.getListInstance();
            Adapter = new PetListAdapter(petLists);
            recyclerView.setAdapter(Adapter);
            Adapter.notifyDataSetChanged();
        }
        Bundle savedInstanceState = null;
        onCreate(savedInstanceState);

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}