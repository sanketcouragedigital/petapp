package com.couragedigital.petapp.Connectivity;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetRefreshFetchList {
    private static final String TAG = PetFetchList.class.getSimpleName();

    public static List petRefreshFetchList(List<PetList> petLists, RecyclerView.Adapter adapter, String url, SwipeRefreshLayout petListSwipeRefreshLayout) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    PetList petList = new PetList();
                                    petList.setPetBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                    petList.setPetPostOwner(replaceSpecialChars(obj.getString("name")));
                                    petList.setImage_path(obj.getString("image_path"));
                                    if(!obj.getString("pet_adoption").equals("")) {
                                        petList.setListingType(replaceSpecialChars(obj.getString("pet_adoption")));
                                    }
                                    else if(!obj.getString("pet_price").equals("")) {
                                        petList.setListingType("Rs. :- " + replaceSpecialChars(obj.getString("pet_price")));
                                    }
                                    petList.setPetCategory(replaceSpecialChars(obj.getString("pet_category")));
                                    petList.setPetAge(replaceSpecialChars(obj.getString("pet_age")));
                                    petList.setPetGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    petList.setPetDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    petList.setPetPostDate(replaceSpecialChars(obj.getString("post_date")));
                                    petList.setPetPostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                    petList.setPetPostOwnerMobileNo(obj.getString("mobileno"));

                                    // adding pet to pets array
                                    petLists.add(0, petList);
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data


                        if(petListSwipeRefreshLayout.isRefreshing()) {
                            petListSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(petListSwipeRefreshLayout.isRefreshing()) {
                    petListSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(petListReq);
        return petLists;
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
