package com.couragedigital.petapp.Adapter;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.Connectivity.MyListingPetMateDelete;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.MyListing;
import com.couragedigital.petapp.MyListingPetMateListDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.MyListingPetMateListItem;
import com.couragedigital.petapp.model.PetMateListItems;

import java.util.ArrayList;
import java.util.List;

public class MyListingPetMateListAdapter extends RecyclerView.Adapter
        <MyListingPetMateListAdapter.ViewHolder> {

    public  List<MyListingPetMateListItem> myListingPetMateArrayList;
    public ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public MyListingPetMateListAdapter() {
    }

    public MyListingPetMateListAdapter(List<MyListingPetMateListItem> modelData) {
        myListingPetMateArrayList = modelData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mylistingpetmateitem, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MyListingPetMateListItem myListingPetMateListItem = myListingPetMateArrayList.get(i);
        viewHolder.bindPetList(myListingPetMateListItem);
    }

    @Override
    public int getItemCount() {
        return myListingPetMateArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedNetworkImageView mlPetMateImage;
        public TextView mlPetMateBreed;
        public Button seeMoreButton;
        public Button deletebutton;
        public View mlDividerLinePetMate;

        int statusOfFavourite = 0;
        private MyListingPetMateListItem myListingPetMateListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            mlPetMateImage = (RoundedNetworkImageView) itemView.findViewById(R.id.myListingPetMateImage);
            mlPetMateBreed = (TextView) itemView.findViewById(R.id.myListingPetMateBreed);
            seeMoreButton = (Button) itemView.findViewById(R.id.myListingPetMateSeeMore);
            mlDividerLinePetMate = itemView.findViewById(R.id.myListingPetMateDividerLine);
            deletebutton = (Button) itemView.findViewById(R.id.myListingPetMateDelete);
            seeMoreButton.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }

        public void bindPetList(MyListingPetMateListItem myListingPetMateListItem) {
            this.myListingPetMateListItem = myListingPetMateListItem;

            mlPetMateImage.setImageUrl(myListingPetMateListItem.getFirstImagePath(), imageLoader);
            mlPetMateBreed.setText(myListingPetMateListItem.getPetMateBreed());
            seeMoreButton.setText("See More");
            deletebutton.setText("Delete");
            //petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
//            mlPetMateFavourite.setVisibility(View.GONE);
            mlDividerLinePetMate.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.myListingPetMateSeeMore) {
                if (this.myListingPetMateListItem != null) {
                    Intent petFullInformation = new Intent(v.getContext(), MyListingPetMateListDetails.class);
                    petFullInformation.putExtra("PET_FIRST_IMAGE", myListingPetMateListItem.getFirstImagePath());
                    petFullInformation.putExtra("PET_SECOND_IMAGE", myListingPetMateListItem.getSecondImagePath());
                    petFullInformation.putExtra("PET_THIRD_IMAGE", myListingPetMateListItem.getThirdImagePath());
                    petFullInformation.putExtra("PET_MATE_BREED", myListingPetMateListItem.getPetMateBreed());
                    petFullInformation.putExtra("PET_MATE_AGE", myListingPetMateListItem.getPetMateAge());
                    petFullInformation.putExtra("PET_MATE_GENDER", myListingPetMateListItem.getPetMateGender());
                    petFullInformation.putExtra("PET_MATE_DESCRIPTION", myListingPetMateListItem.getPetMateDescription());

                    v.getContext().startActivity(petFullInformation);
                }
            }
            else if(v.getId() == R.id.myListingPetMateDelete) {
                if(this.myListingPetMateListItem != null) {
                    String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
                    int id = myListingPetMateListItem.getId();
                    String email = myListingPetMateListItem.getPetMatePostOwnerEmail();
                    url = url + "?method=deleteMyListingPetMateList&format=json&id="+ id +"&email="+ email +"";
                    new DeletePetMateFromServer().execute(url);
                    deletebutton.setText("Deleted");
                    deletebutton.setEnabled(false);
                    seeMoreButton.setEnabled(false);
                }
            }
        }

        public class DeletePetMateFromServer extends AsyncTask<String, String, String> {

            String urlForFetch;
            @Override
            protected String doInBackground(String... url) {
                try {
                    urlForFetch = url[0];
                    MyListingPetMateDelete.deleteFromRemoteServer(urlForFetch, v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}