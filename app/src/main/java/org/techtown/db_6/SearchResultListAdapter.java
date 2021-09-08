package org.techtown.db_6;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class SearchResultListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>

{

    private ArrayList<MapDataItem> mapDataList =null;
    public MemberStore choiceStore=null;
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
           // ((SearchResultViewHolder) holder).store_name.setText(mapDataList.get(position).getStore_name());
           // ((SearchResultViewHolder) holder).store_address.setText(mapDataList.get(position).getStore_address());
            //((SearchResultViewHolder) holder).store_type.setText(mapDataList.get(position).getStore_type());
            MemberStore store= mapDataList.get(position).getStore();
            ((SearchResultViewHolder) holder).store_name.setText(store.getStore_name());
            ((SearchResultViewHolder) holder).store_address.setText(store.getStore_address());
            ((SearchResultViewHolder) holder).store_type.setText(store.getStore_type());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    choiceStore = new MemberStore(store.getStore_num(),store.getStore_name(),store.getStore_type(),store.getStore_address(), BigDecimal.valueOf(store.getLatitude()),BigDecimal.valueOf(store.getLongitude()));
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

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {

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