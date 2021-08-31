package org.techtown.db_6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<DataItem> myDataList = null;

    MyAdapter(ArrayList<DataItem> dataList)
    {
        myDataList = dataList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Code.ViewType.Notmeal)
        {
            view = inflater.inflate(R.layout.notmeal, parent, false);
            return new NotmealViewHolder(view);
        }
        else if(viewType == Code.ViewType.PLUS)
        {
            view = inflater.inflate(R.layout.plus, parent, false);
            return new PLUSViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.meal_card, parent, false);
            return new mealCardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {

        if(viewHolder instanceof NotmealViewHolder)
        {
            ((NotmealViewHolder) viewHolder).name.setText(myDataList.get(position).getName());
            ((NotmealViewHolder) viewHolder).balance.setText(myDataList.get(position).getBalance());
            ((NotmealViewHolder) viewHolder).button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    String[] balances = myDataList.get(position).getBalances();

                    Log.d("tag","click");
                }

            });
        }
        else if(viewHolder instanceof PLUSViewHolder)
        {
            ((PLUSViewHolder) viewHolder).name.setText(myDataList.get(position).getName());
            ((PLUSViewHolder) viewHolder).balance.setText(myDataList.get(position).getBalance());
            // ((PLUSViewHolder) viewHolder).button.setText(myDataList.get(position).getButton());
        }
        else
        {
            ((mealCardViewHolder) viewHolder).name.setText(myDataList.get(position).getName());
            ((mealCardViewHolder) viewHolder).content.setText(myDataList.get(position).getBalance());
            ((mealCardViewHolder) viewHolder).button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    String[] balances = myDataList.get(position).getBalances();

                    Log.d("tag",balances[0]+" "+balances[1]+" "+balances[2]+" "+balances[3]+" "+balances[4]+" "+balances[5]+" "+balances[6]);
                }

            });
        }
    }

    @Override
    public int getItemCount()
    {
        return myDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return myDataList.get(position).getViewType();
    }


    public class NotmealViewHolder extends RecyclerView.ViewHolder{
        TextView balance;
        TextView name;
        ImageView image;
        Button button;

        NotmealViewHolder(View itemView)
        {
            super(itemView);

            balance = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button);

        }

    }

    public class PLUSViewHolder extends RecyclerView.ViewHolder{
        TextView balance;
        TextView name;
        ImageView image;
        Button button;

        PLUSViewHolder(View itemView)
        {
            super(itemView);
            balance = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button);
        }
    }

    public class mealCardViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView name;
        ImageView image;
        Button button;
        mealCardViewHolder(View itemView)
        {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button);
        }
    }


}