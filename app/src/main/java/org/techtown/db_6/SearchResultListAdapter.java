package org.techtown.db_6;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class SearchResultListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MapDataItem> mapDataList =null;
    SearchResultListAdapter(ArrayList<MapDataItem> dataList)
    {
        mapDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType==Code.ViewType.searchResult){
            view = inflater.inflate(R.layout.search_result, parent, false);
            return new SearchResultViewHolder(view);
        }
        else {
            return  null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SearchResultViewHolder)
        {
            ((SearchResultViewHolder) holder).store_name.setText(mapDataList.get(position).getStore_name());
            ((SearchResultViewHolder) holder).store_address.setText(mapDataList.get(position).getStore_address());
            ((SearchResultViewHolder) holder).store_type.setText(mapDataList.get(position).getStore_type());

        }
    }

    @Override
    public int getItemCount() {
        return mapDataList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return mapDataList.get(position).getViewType();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder{

        TextView store_name;
        TextView store_address;
        TextView store_type;
        ImageView map_marker;

        SearchResultViewHolder(View itemView)
        {
            super(itemView);

            store_name = itemView.findViewById(R.id.store_name);
            store_address = itemView.findViewById(R.id.store_address);
            store_type = itemView.findViewById(R.id.store_type);
            map_marker =itemView.findViewById(R.id.map_marker);

        }

    }

}