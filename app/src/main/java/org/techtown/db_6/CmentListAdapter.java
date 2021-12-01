package org.techtown.db_6;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DataItem.CommentData> cmentDataList=null;
    private Posting posting= PostingActivity.posting;
    private String userID;
    public CmentListAdapter(ArrayList<DataItem.CommentData> cmentDataList, String userID){
        this.cmentDataList= cmentDataList;
        this.userID=userID;

        Log.d("tag","userID"+userID);
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
            if(posting.getPid().equals(comment.getCid()))
            {
                ((CmentViewHolder)holder).cid.setTextColor(0xFFF9595F);     //글쓴사람 댓글이면 빨간색으로  0xFFF56E4A
            }

            ((CmentViewHolder)holder).cment.setText(comment.getCment());
            ((CmentViewHolder)holder).cdate.setText(comment.getCdate());


            //댓글 꾹 눌렀을 때 대댓글쓰기 , 본인 글이면 댓글 삭제하기
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    Log.d("tag",comment.getCno()+"댓글클릭됨");
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    String[] list;
                    if(comment.getCid().equals(userID)){
                        list= new String[]{"대댓글 달기","삭제하기"};

                    }else {
                        list=new String[]{"대댓글 달기"};

                    }

                    PostingActivity.sendBt_Ccment.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String ccment = PostingActivity.input1.getText().toString();

                            if (ccment.equals("")) {

                                Toast.makeText(v.getContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("tag",comment.getCno()+"cno에 대댓글 DB에 인서트하기 전");
                                //인서트하면됨

                                insertCcmentOnCment(comment.getPno(),comment.getCno(),userID, ccment,position);

                                if(PostingActivity.isHeartPosting||PostingActivity.isHeartPress){
                                    PostingActivity.pinkheartBt.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    PostingActivity. grayheartBt.setVisibility(View.VISIBLE);
                                }

                                PostingActivity.cmentBt.setVisibility(View.VISIBLE);
                                PostingActivity.input1.setVisibility(View.GONE);
                                PostingActivity.input1.setText("");
                                PostingActivity.sendBt_Ccment.setVisibility(View.GONE);
                                InputMethodManager imm= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(PostingActivity.input1.getWindowToken(), 0);
                            }

                        }
                    });


                    builder.setItems(list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(which==0){
                                //대댓글 달기 클릭시
                                //입력칸이랑 키보드 올라와야함
                                PostingActivity.input1.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        PostingActivity.grayheartBt.setVisibility(View.INVISIBLE);
                                        PostingActivity.pinkheartBt.setVisibility(View.INVISIBLE);
                                        PostingActivity.cmentBt.setVisibility(View.INVISIBLE);
                                        PostingActivity.input1.setVisibility(View.VISIBLE);
                                        PostingActivity.sendBt_Ccment.setVisibility(View.VISIBLE);
                                        PostingActivity.input1.setFocusableInTouchMode(true);
                                        PostingActivity.input1.requestFocus();
                                        InputMethodManager imm= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(PostingActivity.input1, 0);

                                    }
                                });

                            }else if(which==1){
                                //댓글 삭제하기 클릭시
                                Log.d("tag","삭제하기 버튼눌림");
                                deleteCmentOnPosting(comment.getPno(),comment.getCno(),comment.getCccnt(),position);

                            }
                        }
                    });

                    AlertDialog alertD= builder.create();
                    alertD.show();

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

                    if(ccomment.getCcid().equals(userID)){


                        String list[]={"삭제하기"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setItems(list, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0){
                                    //대댓글 삭제하기 클릭시
                                    deleteCcmentOnCment(ccomment.getPno(),ccomment.getCno(),ccomment.getCcno(),position);
                                }
                            }
                        });

                        AlertDialog alertD= builder.create();
                        alertD.show();

                    }

                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cmentDataList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return cmentDataList.get(position).getViewType();
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

    void deleteCmentOnPosting(int pno,int cno,int cccnt, int position) {


        if (cccnt > 0) {

            //댓글의 position 뒤에 대댓글 개수만큼 삭제해준다.
            for (int i = 1; i <= cccnt; i++) {

                Log.d("tag", "대댓글발견"+cmentDataList.get(position+1).getCcomment().getCcno()+"ccno삭제함");
                //ccnt값도 내려주기
                updateCmentCnt(pno,"minus");
                cmentDataList.remove(position + 1);

            }
        }

        cmentDataList.remove(position);
        notifyDataSetChanged();

        Server server = new Server();
        RetrofitService service = server.getRetrofitService();
        Call<Integer> call = service.deleteComment(pno, cno);

        call.enqueue(new Callback<Integer>() {

            //Comment DB에서 삭제임 (delete on Cascade라 DB의 대댓글에선 작동으로 삭제됨)
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                if (response.isSuccessful()) {
                    Integer result = response.body();
                    if (result.intValue() == 1) {

                        Log.d("tag", "댓글삭제성공\n");
                        updateCmentCnt(pno,"minus");             //ccnt하나 내려주기
                    }
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("tag", "댓글삭제실패\n");
            }
        });

    }


    void updateCmentCnt(int pno, String sign){

        Server server= new Server();
        RetrofitService service= server.getRetrofitService();
        Call<Integer> call= service.putCommentCnt(pno,sign);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful())
                {
                    if( sign.equals("plus"))
                    {
                        Log.d("tag","ccnt++");
                        //보이는 댓글 수 바꾸기
                        PostingActivity.ccnt.setText(String.valueOf(posting.getCcnt()+1));
                        posting.addCcnt(+1);

                    }
                    else {
                        Log.d("tag","ccnt--");
                        //보이는 댓글 수 바꾸기
                        PostingActivity.ccnt.setText(String.valueOf(posting.getCcnt()-1));
                        Log.d("tag",PostingActivity.ccnt.getText().toString());
                        posting.addCcnt(-1);

                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    void updateCcmentCnt(int pno,int cno, String sign){

        //대댓글수 증가 감소 DB에 업데이트

        Server server= new Server();
        RetrofitService service= server.getRetrofitService();
        Call<Integer> call= service.putCcommentCnt(pno,cno,sign);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful())
                {
                    if( sign.equals("plus"))
                    {
                        Log.d("tag","cccnt++");
                    }
                    else {
                        Log.d("tag","cccnt--");
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }
    void deleteCcmentOnCment(int pno,int cno,int ccno, int position){

        Server server= new Server();
        RetrofitService service= server.getRetrofitService();
        Call<Integer> call= service.deleteCcomment(pno,cno,ccno);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer result= response.body();

                if(result.intValue()==1){

                    Log.d("tag", "대댓글삭제성공\n");

                    //DB쪽
                    updateCmentCnt(pno,"minus");              //ccnt 내리기
                    updateCcmentCnt(pno,cno,"minus");         //cccnt 내리기

                    //DataList 쪽  cccnt내리기
                    for(DataItem.CommentData dataItem: cmentDataList){

                        if(dataItem.getComment()!=null){

                            if(dataItem.getComment().getCno()==cno)
                                dataItem.getComment().addCccnt(-1);
                        }
                    }

                    notifyItemRemoved(position);
                    cmentDataList.remove(position);
                    notifyItemRangeChanged(position,cmentDataList.size());



                }else{
                    Log.d("tag", "대댓글삭제실패\n");
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("tag", "대댓글삭제실패\n");
            }
        });

    }

    void insertCcmentOnCment(int pno,int cno, String ccid, String ccment, int position){

        //cccnt올리기 ccnt올리기
        //DB쪽 - 대댓글테이블에 댓글 올리기, 댓글테이블에 대댓글 수 증가. 포스팅테이블에 댓글 수 증가
        //DataList쪽 - 대댓글 달려고 한 댓글 posotion 뒤에 대댓글 추가해서 리사이클러뷰에 데이터리스트 변화 notify. 이때 기존 대댓글이 있다면 가장 뒤에 추가

        //1) ccno 숫자 만들기
        int cccnt= cmentDataList.get(position).getComment().getCccnt();
        int ccno=1;

        if(cccnt==0){
            ccno=1;  //1로 그대로 둔다
        }
        else
        {
            for (int i = 1; i <= cccnt; i++) {

                Ccomment ccomment = cmentDataList.get(position + i).getCcomment();

                if (ccno < ccomment.getCcno()) {
                    ccno = ccomment.getCcno();
                }
            }
            ccno++;     //최댓값보다 1증가
        }

        //2) 대댓글테이블에 댓글 올리기
        Server server= new Server();
        RetrofitService service= server.getRetrofitService();

        Call<Ccomment> call= service.postCcomment(pno,cno,ccno,ccid,ccment);
        Log.d("tag",pno+" "+cno+" "+ccno+" "+ccid+" "+ccment+"대댓글 인서트하려고함");


        call.enqueue(new Callback<Ccomment>() {
            @Override
            public void onResponse(Call<Ccomment> call, Response<Ccomment> response) {
                if(response.isSuccessful()){
                    Ccomment ccomment= response.body();

                    //2) 데이터리스트에 추가해주기
                    //대댓글 없는 댓글이라면 댓글바로 뒤에 추가        //대댓글 있는 댓글이라면 가장 뒤 대댓글 뒤에 추가
                    cmentDataList.add(position+cccnt+1,new DataItem.CommentData(ccomment, StateSet.ViewType.ccomment));


                    //3) ccnt, cccnt 값 1증가 업데이트 - DB
                    updateCmentCnt(pno,"plus");
                    updateCcmentCnt(pno,cno,"plus");

                    //3) cccnt 값 1증가 업데이트 - dataList
                    cmentDataList.get(position).getComment().addCccnt(+1);

                    //4) 리사이클러뷰 다시 구성
                    notifyItemRangeChanged(position+1,cmentDataList.size());

                }

            }

            @Override
            public void onFailure(Call<Ccomment> call, Throwable t) {
                Log.d("tag", "대댓글달기 실패\n");
            }
        });

    }
}
