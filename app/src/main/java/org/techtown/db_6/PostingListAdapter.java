package org.techtown.db_6;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;


public class PostingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Posting> postingDataList= null;
    private Posting choicePosting=null;
    private String userID;
    private String attachedActivity;

    public PostingListAdapter(ArrayList<Posting> dataList, String userID, String attachedActivity){
        this.postingDataList=dataList;
        this.userID=userID;
        this.attachedActivity=attachedActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        Context context= parent.getContext();

        if(viewType==StateSet.ViewType.posting) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.posting, parent, false);
            return new PostingViewHolder(view);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.board_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        // return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof PostingViewHolder){

            Posting posting= postingDataList.get(position);

            ((PostingViewHolder)holder).pid.setText(posting.getPid());
            ((PostingViewHolder)holder).pcontent.setText(posting.getPcontent());

            String pdate=posting.getPdate();
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
            String nowTime = sdf.format(System.currentTimeMillis());

            Boolean sameDay=true;
            for(int i=0;i<11;i++){
                if(pdate.charAt(i)!=nowTime.charAt(i))
                {
                    sameDay=false;
                }
            }
            if(sameDay){
                //여전히 true 라면 같은 날, 같은 시간대이다.
                int beforeMinute =  Integer.parseInt(nowTime.substring(12)) - Integer.parseInt(pdate.substring(12));
                if (beforeMinute==0) {
                    ((PostingViewHolder)holder).pdate.setText("방금 전");
                }else {
                    ((PostingViewHolder)holder).pdate.setText(beforeMinute + "분 전");
                }
            }
            else{
                ((PostingViewHolder)holder).pdate.setText(pdate);
            }

            ((PostingViewHolder)holder).hcnt.setText(String.valueOf(posting.getHcnt()));
            ((PostingViewHolder)holder).ccnt.setText(String.valueOf(posting.getCcnt()));
            ((PostingViewHolder)holder).vcnt.setText(String.valueOf(posting.getVcnt()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("tag","클릭된pno:"+posting.getPno());
                    choicePosting= posting;

                    Intent intent = new Intent(v.getContext(),PostingActivity.class);
                    intent.putExtra("choicePosting",choicePosting);
                    intent.putExtra("userID",userID);
                    intent.putExtra("prevActivity",attachedActivity);
                    v.getContext().startActivity(intent);

                    //  오 ->왼 슬라이딩 하면서 전환됨
                    ((Activity)v.getContext()).overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit );

                    /*바로 게시판에서 조회된 포스팅액티비티로 넘어갈경우 이전액티비티는 없애준다.
                     그때는 포스팅 액티비티에서 이전 화면(게시판)으로 넘어올 때 DB에서도 재조회해서 UI에 보여지는 정보의 싱크를 맞춰주기 위함 */
                    if(attachedActivity.equals("BoardActivity")) {
                        Log.d("tag", "지금 게시판액티비티 삭제되나요?");
                        ((Activity) v.getContext()).finish();
                    }
                }
            });
        }
        else if(holder instanceof LoadingViewHolder)
        {
            //로딩 바를 보여준다.
        }
    }

    @Override
    public int getItemViewType(int position) {
        return postingDataList.get(position)==null? StateSet.ViewType.loading: StateSet.ViewType.posting;
    }

    @Override
    public int getItemCount() {
        return postingDataList.isEmpty()? 0: postingDataList.size();
    }

    public class PostingViewHolder extends RecyclerView.ViewHolder{

        TextView pid,pcontent,pdate,hcnt,ccnt,vcnt;

        public PostingViewHolder( View itemView) {

            super(itemView);
            pid=itemView.findViewById(R.id.pid);
            pcontent= itemView.findViewById(R.id.pcontent);
            pdate=itemView.findViewById(R.id.pdate);
            hcnt=itemView.findViewById(R.id.hcnt);
            ccnt=itemView.findViewById(R.id.ccnt);
            vcnt=itemView.findViewById(R.id.vcnt);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{

        private ProgressBar progressBar;
        public LoadingViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            progressBar= itemView.findViewById(R.id.progressbar);
        }
    }

}
