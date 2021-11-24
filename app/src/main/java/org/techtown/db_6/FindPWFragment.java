package org.techtown.db_6;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPWFragment extends Fragment {

    private EditText edit_name, edit_id, edit_email;
    private Button btn_send, btn_login;
    private TextView checkResult;
    private MainHandler handler;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.find_pw, container, false);

        edit_name = view.findViewById(R.id.edit_name);
        edit_id = view.findViewById(R.id.edit_id);
        edit_email = view.findViewById(R.id.edit_email);
        btn_send = view.findViewById(R.id.btn_send);
        btn_login = view.findViewById(R.id.btn_login);
        checkResult = view.findViewById(R.id.text_checkResult);

        checkResult.setText("아이디와 가입시 입력한 이메일을 통해\n회원님의 비밀번호를 찾을 수 있습니다.");
        handler = new MainHandler();

        //로그인 버튼 클릭 시
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //전송하기 버튼 클릭 시
        btn_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = edit_email.getText().toString();
                String name = edit_name.getText().toString();
                String id = edit_id.getText().toString();

                if (email.equals("") || name.equals("") || id.equals("")) {
                    checkResult.setTextColor(0xAAef484a); //경고 빨간색
                    checkResult.setText("이름과 아이디,이메일을 모두 입력하십시오.");
                    return;
                }

                Server server = new Server();
                RetrofitService service1 = server.getRetrofitService();
                Call<UserProfile> call = service1.getUserProfile(id);

                call.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        UserProfile result = response.body();

                        if (result.getName().equals(name) && result.getEmail().equals(email)) {

                            checkResult.setTextColor(0xFF6E6E6E);
                            checkResult.setText("가입하신 이메일주소로 아이디를 전송합니다.");
                            SendPWbyMailThread thread= new SendPWbyMailThread(email,result.getPwd(),name);
                            thread.start();

                        } else {
                            checkResult.setTextColor(0xAAef484a);
                            checkResult.setText("해당 아이디로 가입된 회원과 이름\n혹은 이메일이 일치하지 않습니다.");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                        checkResult.setTextColor(0xAAef484a);
                        checkResult.setText("해당 아이디로 가입된 회원이 없습니다.\n아이디를 다시 확인하십시오.");

                    }
                });
            }
        });
        return view;
    }


    class SendPWbyMailThread extends Thread {

        String receptEmail;
        String content;

        public SendPWbyMailThread(String email, String pw, String name) {
            this.receptEmail = email;
            this.content = name + "님, 안녕하세요.\n회원님의 가입된 컬러풀 카드앱의 비밀번호는 아래와 같습니다.\n\n비밀번호: " + pw;
        }

        @Override
        public void run() {
            Message message = handler.obtainMessage();

            try {
                GmailSender gMailSender = new GmailSender();
                Log.d("tag", "sender Make");
                gMailSender.sendMail("[컬러풀 카드앱] 비밀번호 안내",
                        content, receptEmail);

                message.what = StateSet.MailMsg.MSG_SUCCESS;
                handler.sendMessage(message);

            } catch (SendFailedException e) {

                message.what = StateSet.MailMsg.MSG_FAIL;
                handler.sendMessage(message);

            } catch (MessagingException e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message){
            switch(message.what)
            {

                case StateSet.MailMsg.MSG_SUCCESS:
                    checkResult.setText("가입하신 이메일주소로 비밀번호를 전송했습니다.\n해당 이메일에서 비밀번호를 확인하십시오.");
                    break;
                case StateSet.MailMsg.MSG_FAIL: //나올리가 없다
                    checkResult.setText("이메일 형식이 잘못되었습니다");
                    break;
            }
        }
    }
}