package org.techtown.db_6;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.math.BigDecimal;
import java.util.ArrayList;

public class SearchStoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>

{

    private ArrayList<DataItem.MapData> mapDataList =null;
    public MemberStore choiceStore=null;
    SearchStoreListAdapter(ArrayList<DataItem.MapData> dataList)
    {
        mapDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType== StateSet.ViewType.searchResult){
            view = inflater.inflate(R.layout.search_result, parent, false);
            return new SearchStoreViewHolder(view);
        }
        else {
            return  null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SearchStoreViewHolder)
        {
             MemberStore store= mapDataList.get(position).getStore();
            ((SearchStoreViewHolder) holder).store_name.setText(store.getStore_name());
            ((SearchStoreViewHolder) holder).store_address.setText(store.getStore_address());
            ((SearchStoreViewHolder) holder).store_type.setText(store.getStore_type());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    choiceStore = new MemberStore(store.getStore_num(),store.getStore_name(),store.getStore_type(),store.getStore_address(), BigDecimal.valueOf(store.getLatitude()),BigDecimal.valueOf(store.getStore_Longitude()));
                    Intent intent = new Intent(v.getContext(),Map2Activity.class);

                    intent.putExtra("choiceStore",choiceStore);
                    v.getContext().startActivity(intent);

                }
            });
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

    public class SearchStoreViewHolder extends RecyclerView.ViewHolder {

        TextView store_name;
        TextView store_address;
        TextView store_type;
        ImageView map_marker;

        SearchStoreViewHolder(View itemView)
        {
            super(itemView);

            store_name = itemView.findViewById(R.id.store_name);
            store_address = itemView.findViewById(R.id.store_address);
            store_type = itemView.findViewById(R.id.store_type);
            map_marker =itemView.findViewById(R.id.map_marker);

        }

    }

}