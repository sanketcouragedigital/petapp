package com.couragedigital.peto.Connectivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Adapter.SpinnerItemsAdapter;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetBreedsSpinnerList {

    private static final String TAG = PetBreedsSpinnerList.class.getSimpleName();
    public static String url = URLInstance.getUrl();

    public static List fetchPetBreeds(List petBreedsList, String petCategoryName, SpinnerItemsAdapter adapter) {
        String urlToFetch = url + "?petCategory="+petCategoryName+"&method=showPetBreedsAsPerPetCategory&format=json";
        JsonObjectRequest petCategoryReq = new JsonObjectRequest(Request.Method.GET, urlToFetch, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        petBreedsList.removeAll(petBreedsList);
                        adapter.notifyDataSetChanged();
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetBreedsResponse");
                            petBreedsList.add("Select Pet Breed");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    petBreedsList.add(obj.optString("pet_breed"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(petCategoryReq);
        return petBreedsList;
    }
}