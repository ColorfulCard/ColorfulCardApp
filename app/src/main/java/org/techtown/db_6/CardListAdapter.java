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

public class CardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Intent intent;
    private ArrayList<CardDataItem> myDataList = null;

    CardListAdapter(ArrayList<CardDataItem> dataList)
    {
        myDataList = dataList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType== Code.ViewType.mealCard){
            view = inflater.inflate(R.layout.meal_card, parent, false);
            return new MealCardViewHolder(view);
        }
        else  if(viewType == Code.ViewType.sideMealCard)
        {
            view = inflater.inflate(R.layout.sidemeal_card, parent, false);
            return new SideMealViewHolder(view);
        }
        else if(viewType== Code.ViewType.eduCard)
        {
            view = inflater.inflate(R.layout.educationcard, parent, false);
            return new SideMealViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.plus, parent, false);
            return new PLUSViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(viewHolder instanceof MealCardViewHolder)
        {
            ((MealCardViewHolder) viewHolder).name.setText(myDataList.get(position).getName());
            ((MealCardViewHolder) viewHolder).content.setText(myDataList.get(position).getBalance());
            ((MealCardViewHolder) viewHolder).button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    String[] balances = myDataList.get(position).getBalances();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    AlertDialog dialog = builder.setMessage(
                            "이월 잔여금액: "+balances[0] +"원"
                                    +"\n당월 충전금액: "+ balances[1] +"원"
                                    +"\n당월 사용금액: "+ balances[2] +"원"
                                    +"\n당월 잔여금액: "+ balances[3] +"원"
                                    +"\n금일 한도금액: "+ balances[4] +"원"
                                    +"\n금일 사용금액: "+ balances[5] +"원"
                                    +"\n금일 잔여금액: "+ balances[6]).setPositiveButton("확인", null).create();
                    dialog.show();

                }

            });
        }
        else if(viewHolder instanceof SideMealViewHolder)
        {
            ((SideMealViewHolder) viewHolder).name.setText(myDataList.get(position).getName());
            ((SideMealViewHolder) viewHolder).balance.setText(myDataList.get(position).getBalance());
            ((SideMealViewHolder) viewHolder).button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    String[] balances = myDataList.get(position).getBalances();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    AlertDialog dialog = builder.setMessage(
                            "이월 잔여금액: "+balances[0] +"원"
                            +"\n당월 충전금액: "+ balances[1] +"원"
                            +"\n당월 사용금액: "+ balances[2] +"원"
                            +"\n당월 잔여금액: "+ balances[3] +"원"
                            +"\n금일 한도금액: "+ balances[4] +"원"
                            +"\n금일 사용금액: "+ balances[5] +"원"
                            +"\n금일 잔여금액: "+ balances[6]).setPositiveButton("확인", null).create();
                    dialog.show();

                }

            });
        }
        else if(viewHolder instanceof EduCardViewHolder){

            ((EduCardViewHolder) viewHolder).name.setText(myDataList.get(position).getName());
            ((EduCardViewHolder) viewHolder).content.setText(myDataList.get(position).getBalance());
            ((EduCardViewHolder) viewHolder).button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    String[] balances = myDataList.get(position).getBalances();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    AlertDialog dialog = builder.setMessage(
                            "이월 잔여금액: "+balances[0] +"원"
                                    +"\n당월 충전금액: "+ balances[1] +"원"
                                    +"\n당월 사용금액: "+ balances[2] +"원"
                                    +"\n당월 잔여금액: "+ balances[3] +"원"
                                    +"\n금일 한도금액: "+ balances[4] +"원"
                                    +"\n금일 사용금액: "+ balances[5] +"원"
                                    +"\n금일 잔여금액: "+ balances[6]).setPositiveButton("확인", null).create();
                    dialog.show();

                }

            });

        }
        else if(viewHolder instanceof PLUSViewHolder)
        {
            ((PLUSViewHolder) viewHolder).name.setText(myDataList.get(position).getName());
           // ((PLUSViewHolder) viewHolder).balance.setText(myDataList.get(position).getBalance());
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


    public class SideMealViewHolder extends RecyclerView.ViewHolder{
        TextView balance;
        TextView name;
        ImageView image;
        Button button;

        SideMealViewHolder(View itemView)
        {
            super(itemView);

            balance = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.tv);
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
            name = itemView.findViewById(R.id.tv);
            image = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button);
        }
    }

    public class MealCardViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView name;
        ImageView image;
        Button button;
        TextView tv;
        MealCardViewHolder(View itemView)
        {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.tv);
            image = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button);
        }
    }

    public class EduCardViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView name;
        ImageView image;
        Button button;
        TextView tv;
        EduCardViewHolder(View itemView)
        {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.tv);
            image = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button);
        }
    }


}