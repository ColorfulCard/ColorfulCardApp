package org.techtown.db_6;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Intent intent;
    private ArrayList<CardDataItem> cardDataList = null;

    CardListAdapter(ArrayList<CardDataItem> dataList)
    {
        cardDataList = dataList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType== Code.ViewType.mealCard){
            view = inflater.inflate(R.layout.meal_card, parent, false);
            return new CardViewHolder(view);
        }
        else  if(viewType == Code.ViewType.sideMealCard)
        {
            view = inflater.inflate(R.layout.sidemeal_card, parent, false);
            return new CardViewHolder(view);
        }
        else if(viewType== Code.ViewType.eduCard)
        {
            view = inflater.inflate(R.layout.educationcard, parent, false);
            return new CardViewHolder(view);
        }
        else{
            return null;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(viewHolder instanceof CardViewHolder){

            CardDataItem card =cardDataList.get(position);

            ((CardViewHolder) viewHolder).name.setText(card.getCardName());
            ((CardViewHolder) viewHolder).content.setText(card.getBalance());
            ((CardViewHolder) viewHolder).minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("카드를 삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            RetrofitService service1 = retrofit.create(RetrofitService.class);

                            Call<Integer> call = service1.deleteUserCard(card.getCardNum(), card.getUserID(), card.getCardName(), card.getCardType());

                            call.enqueue(new Callback<Integer>() {

                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {

                                    if (response.isSuccessful()) {

                                        Log.d("tag", "회원삭제성공\n");
                                        notifyItemRemoved(position);
                                        cardDataList.remove(position);
                                        notifyItemRangeChanged(position, cardDataList.size());
                                        //등록된 카드만 삭제하니 데이터 불일치로 삭제실패 (반환0) 인 문제는 발생하지 않음
                                    }
                                    else
                                    {
                                        Log.d("tag", "회원삭제실패\n");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                   // Log.d("tag", "서버 delete동작 후 반환이 int 라서 발생한 오류" + t.getMessage());
                                    Log.d("tag", " 네트워크 문제로 회원등록 실패" + t.getMessage());

                                }
                            });

                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    builder.show();
                }
            });

            ((CardViewHolder) viewHolder).button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    String[] balances = cardDataList.get(position).getBalances();

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

    }


    @Override
    public int getItemCount()
    {
        return cardDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return cardDataList.get(position).getViewType();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView name;
        ImageView image;
        Button button;
        ImageView minus;

        CardViewHolder(View itemView)
        {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.tv);
            image = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button);
            minus=itemView.findViewById(R.id.minus);
        }
    }

}