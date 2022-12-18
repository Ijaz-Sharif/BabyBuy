package com.example.babybuy.Fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.babybuy.Database.DatabaseService;
import com.example.babybuy.Model.User;
import com.example.babybuy.R;
import com.example.babybuy.Screen.AccountActivity;


public class SignUpFragment extends Fragment {


    private EditText etRegisterEmail,et_user_name, etRegisterPassword, etRegisterConfirmPassword;

    TextView tv_login;
    Button btnRegister;
    private Dialog loadingDialog;
    RadioButton volunteerChecked,donorChecked;
    DatabaseService databaseService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        databaseService =new DatabaseService(getContext());
        /////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_login=view.findViewById(R.id.tv_login);
        etRegisterEmail = view.findViewById(R.id.et_register_email);
        etRegisterPassword = view.findViewById(R.id.et_register_password);
        etRegisterConfirmPassword = view.findViewById(R.id.et_register_confirm_password);
        et_user_name = view.findViewById(R.id.et_user_name);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AccountActivity)getActivity()).showLoginScreen();
            }
        });

        btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = etRegisterEmail.getText().toString();
                String name = et_user_name.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String confirm_password = etRegisterConfirmPassword.getText().toString();
                if (validate(email,name, password, confirm_password)) requestRegister(email,name, confirm_password);
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String name, String password, String confirm_password) {
        if (email.isEmpty()) etRegisterEmail.setError("Enter email!");
        else if (name.isEmpty()) et_user_name.setError("Enter name!");
        else if (!email.contains("@")||!email.contains(".")) etRegisterEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etRegisterPassword.setError("Enter password!");
        else if (password.length()<6) etRegisterPassword.setError("Password must be at least 6 characters!");
        else if (confirm_password.isEmpty()) etRegisterConfirmPassword.setError("Enter password!");
        else if (!password.equals(confirm_password)) etRegisterConfirmPassword.setError("Password not matched!");
        else return true;
        return false;
    }
    private void requestRegister(String email, String name,  String confirm_password) {
        loadingDialog.show();
        User user=   new User(name,email,confirm_password);
        databaseService.addUser(user);
        loadingDialog.dismiss();
        ((AccountActivity)getActivity()).showLoginScreen();



    }
}