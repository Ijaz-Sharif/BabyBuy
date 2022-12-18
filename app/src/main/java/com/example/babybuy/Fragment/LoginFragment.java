package com.example.babybuy.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babybuy.Database.DatabaseService;
import com.example.babybuy.MainActivity;
import com.example.babybuy.Model.User;
import com.example.babybuy.R;
import com.example.babybuy.Screen.AccountActivity;
import com.example.babybuy.Utils.Constant;

public class LoginFragment extends Fragment {
    private EditText etLoginEmail, etLoginPassword;
    TextView tv_new_register;
    private Dialog loadingDialog;
    DatabaseService databaseService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        databaseService =new DatabaseService(getContext());
        /////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        etLoginEmail =view.findViewById(R.id.et_login_email);
        etLoginPassword = view.findViewById(R.id.et_login_password);
        tv_new_register=view.findViewById(R.id.tv_new_register);
        Button btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();
                // call the validate function and then request
                if (validate(email, password)) requestLogin(email, password);
            }
        });


        tv_new_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AccountActivity)getActivity()).showSignUpScreen();
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String password) {
        if (email.isEmpty()) etLoginEmail.setError("Enter email!");
        else if (!email.contains("@")||!email.contains(".")) etLoginEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etLoginPassword.setError("Enter password!");
        else if (password.length()<6) etLoginPassword.setError("Password must be at least 6 characters!");
        else return true;
        return false;
    }
    private void requestLogin(String email, String password) {
        loadingDialog.show();

        // get donor record from database on the base of email and password
        User user =databaseService.getUserData(email,password);
        if(user!=null){
            // save record in the shared preferance
            Constant.setUsername(getContext(),user.getName());
            Constant.setUserId(getContext(),user.getUserId());
            Constant.setUserLoginStatus(getContext(),true);

            loadingDialog.dismiss();
            startActivity(new Intent(getContext(), MainActivity.class));

        }
        else {
            loadingDialog.dismiss();
            Toast.makeText(getContext(), "wrong mail or password", Toast.LENGTH_LONG).show();
        }






    }
}