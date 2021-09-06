package org.techtown.db_6;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Intent intent;
    private ArrayList<MemberStore> MemberStoreDataList = null;

    mapAdapter(ArrayList<MemberStore> MemberStoredataList)
    {
        MemberStoreDataList = MemberStoredataList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.infowindowmap, parent, false);
        return new MemberStoreViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {

       if(viewHolder instanceof MemberStoreViewHolder)
        {
            ((MemberStoreViewHolder) viewHolder).tv.setText(MemberStoreDataList.get(position).getStore_name());
            // ((PLUSViewHolder) viewHolder).button.setText(myDataList.get(position).getButton());
        }


    }


    @Override
    public int getItemCount()
    {
        return MemberStoreDataList.size();
    }


    public class MemberStoreViewHolder extends RecyclerView.ViewHolder{
        TextView tv;

        MemberStoreViewHolder(View itemView)
        {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }


}
