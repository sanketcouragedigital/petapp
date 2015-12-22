package com.couragedigital.petapp.Connectivity;


import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.ClinicListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetFetchClinicList {

    private static final String TAG = PetFetchClinicList.class.getSimpleName();

    public static List petFetchClinicList(List<ClinicListItems> clinicList, RecyclerView.Adapter adapter, String url, ProgressDialog progressDialog) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showClinicDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    ClinicListItems clinicListItems = new ClinicListItems();
                                    clinicListItems.setClinicName(obj.getString("clinic_name"));
                                    clinicListItems.setClinicAdress(obj.getString("clinic_address"));
                                    clinicListItems.setDoctorName(obj.getString("doctor_name"));
                                    clinicListItems.setContact(obj.getString("contact"));
                                    clinicListItems.setEmail(obj.getString("email"));
                                    clinicListItems.setClinicImage_path(obj.getString("clinic_image"));

                                    // adding pet to pets array
                                    clinicList.add(clinicListItems);
                                    adapter.notifyDataSetChanged();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();
                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(petListReq);
        return clinicList;
    }
}
