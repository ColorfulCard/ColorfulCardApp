package org.techtown.db_6;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DataItem.CommentData> cmentDataList=null;

    public CmentListAdapter(ArrayList<DataItem.CommentData> cmentDataList){
    this.cmentDataList= cmentDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType==StateSet.ViewType.comment){
            view = inflater.inflate(R.layout.comment, parent, false);
            return new CmentViewHolder(view);

        }else if(viewType==StateSet.ViewType.ccomment){
            view = inflater.inflate(R.layout.ccomment, parent, false);
            return new CcmentViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof CmentViewHolder){
            Comment comment= cmentDataList.get(position).getComment();
            ((CmentViewHolder)holder).cid.setText(comment.getCid());
            ((CmentViewHolder)holder).cment.setText(comment.getCment());
            ((CmentViewHolder)holder).cdate.setText(comment.getCdate());

            //댓글 꾹 눌렀을 때 대댓글쓰기 , 본인 글이면 댓글 삭제하기
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("tag",comment.getCno()+"댓글클릭됨");

                    return false;
                }
            });
        }
        else if(holder instanceof  CcmentViewHolder){
            Ccomment ccomment= cmentDataList.get(position).getCcomment();
            ((CcmentViewHolder)holder).ccid.setText(ccomment.getCcid());
            ((CcmentViewHolder)holder).ccment.setText(ccomment.getCcment());
            ((CcmentViewHolder)holder).ccdate.setText(ccomment.getCcdate());

            //대댓글 꾹 눌렀을 때 본인 글이면 대댓글 삭제하기
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("tag",ccomment.getCno()+"-"+ccomment.getCcno()+"대댓글클릭됨");

                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CmentViewHolder extends RecyclerView.ViewHolder {

       TextView cid, cment, cdate ;

        public CmentViewHolder(View itemView) {
            super(itemView);

            cid= itemView.findViewById(R.id.cid);
            cment= itemView.findViewById(R.id.cment);
            cdate= itemView.findViewById(R.id.cdate);
        }
    }
    public class CcmentViewHolder extends RecyclerView.ViewHolder{

        TextView ccid, ccment,ccdate;

        public CcmentViewHolder(View itemView){
            super(itemView);
            ccid= itemView.findViewById(R.id.ccid);
            ccment= itemView.findViewById(R.id.ccment);
            ccdate= itemView.findViewById(R.id.ccdate);

        }
    }
}
