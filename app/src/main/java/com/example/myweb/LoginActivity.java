package com.example.myweb;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
//import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    DatabaseHelper db;
//    ViewGroup progressView;
//    protected boolean isProgressShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
//        View v = this.getLayoutInflater().inflate(R.layout. progress_bar,null);
//        dialog.setContentView(v);
//        dialog.show();

        db = new DatabaseHelper(this);
        mTextUsername = (EditText)findViewById(R.id.edittext_username);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mButtonLogin = (Button)findViewById(R.id.button_login);
        mTextViewRegister = (TextView)findViewById(R.id.textview_register);
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                Boolean res = db.checkUser(user, pwd);
                if(res == true)
                {
//                  Intent HomePage = new Intent(LoginActivity.this,HomeActivity.class);
                    int id = db.getUserId(user,pwd);
                    SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                    sessionManagement.saveSession(id);

                    Intent MainPage = new Intent(LoginActivity.this,MainActivity.class);
                    MainPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(MainPage);


                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        int userId = sessionManagement.getSession();

        if(userId != -1){
            Intent MainPage = new Intent(LoginActivity.this,MainActivity.class);
            MainPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(MainPage);
        }
    }

//    public void showProgressingView() {
//
//        if (!isProgressShowing) {
//            View view=findViewById(R.id.progressBar1);
//            view.bringToFront();
//        }
//    }
//
//    public void hideProgressingView() {
//        View v = this.findViewById(android.R.id.content).getRootView();
//        ViewGroup viewGroup = (ViewGroup) v;
//        viewGroup.removeView(progressView);
//        isProgressShowing = false;
//    }
}
