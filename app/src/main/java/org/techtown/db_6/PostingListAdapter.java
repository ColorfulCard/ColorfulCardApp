package org.techtown.db_6;
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

import java.util.ArrayList;


public class PostingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Posting> postingDataList= null;
    public Posting choicePosting=null;

    public PostingListAdapter(ArrayList<Posting> dataList){
        this.postingDataList=dataList;
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
            ((PostingViewHolder)holder).pdate.setText(posting.getPdate());
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
                    v.getContext().startActivity(intent);
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
