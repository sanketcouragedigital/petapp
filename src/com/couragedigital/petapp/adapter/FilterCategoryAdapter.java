package com.couragedigital.petapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.model.FilterCategoryList;

import java.util.List;

public class FilterCategoryAdapter extends RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder> {

    List<FilterCategoryList> filterCategoryLists;
    View v;
    ViewHolder viewHolder;

    public FilterCategoryAdapter(List<FilterCategoryList> filterCategoryLists) {
        this.filterCategoryLists = filterCategoryLists;
    }

    @Override
    public FilterCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petlistfiltercategory, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterCategoryAdapter.ViewHolder viewHolder, int i) {
        FilterCategoryList filterCategoryList = filterCategoryLists.get(i);
        viewHolder.bindFilterCategoryList(filterCategoryList);
    }

    @Override
    public int getItemCount() {
        return filterCategoryLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout filterCategoryLayout;
        TextView filterCategoryText;
        CheckBox filterCategoryCheckBox;

        private FilterCategoryList filterCategoryList;

        public ViewHolder(View itemView) {
            super(itemView);
            filterCategoryLayout = (RelativeLayout) itemView.findViewById(R.id.filterCategoryLayout);
            filterCategoryText = (TextView) itemView.findViewById(R.id.filterCategoryText);
            filterCategoryCheckBox = (CheckBox) itemView.findViewById(R.id.filterCategoryCheckBox);

            filterCategoryLayout.setOnClickListener(this);
        }

        public void bindFilterCategoryList(FilterCategoryList filterCategoryList) {
            this.filterCategoryList = filterCategoryList;
            filterCategoryText.setText(filterCategoryList.getCategoryText());
            filterCategoryCheckBox.setChecked(false);
        }

        @Override
        public void onClick(View v) {

        }
    }
}