package com.example.daybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final String TAG = "EmailPassword";


    FirebaseUser mUser;

    String emailPattern = "^(.+)@(\\\\S+)$";


    EditText Email;
    EditText Password;
    EditText ConfPassword;

    ProgressDialog progressDialog;


    ConstraintLayout SignUp;
    ConstraintLayout SendVerifyAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        SignUp = findViewById(R.id.SignUp);

        Email = findViewById(R.id.EmailAddress);
        Password  = findViewById(R.id.Password);
        ConfPassword  = findViewById(R.id.PasswordConf);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        SendVerifyAgain = findViewById(R.id.verifyEmail);

        progressDialog = new ProgressDialog(this);

        if(mUser != null){
            Intent intent = new Intent(register.this, calendar_activity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void register(View view){



        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confpassword = ConfPassword.getText().toString();

        createAccount();

    }

    void createAccount(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmPassword = ConfPassword.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if(!isValidated){
            return;
        }else {
            createAccountInFirebase(email, password);
        }
    }

    void createAccountInFirebase(String email, String password) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(register.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // creating acc is done
                            Utility.showToast(register.this, "Successfully create account, Check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();

                            Handler handler = new Handler();

                            Runnable vis = new Runnable() {
                                @Override
                                public void run() {
                                    SendVerifyAgain.setVisibility(View.VISIBLE);
                                    SendVerifyAgain.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                }
                            };

                            handler.postDelayed(vis,10000);

                        } else{
                            //failure
                            Utility.showToast(register.this, task.getException().getLocalizedMessage());

                            Handler handler = new Handler();

                            Runnable vis = new Runnable() {
                                @Override
                                public void run() {
                                    SendVerifyAgain.setVisibility(View.VISIBLE);
                                    SendVerifyAgain.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                }
                            };

                            handler.postDelayed(vis,10000);

                        }
                    }
                });
    }

    boolean validateData(String email, String password, String confirmPassword){
        // validate the data that are input by user

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6){
            Password.setError("Password length is invalid");
            return false;
        }
        if (!password.equals(confirmPassword)){
            ConfPassword.setError("Password not matched");
            return false;
        }
        return true;
    }





    public void LogIn(View view){

        Intent intent = new Intent(register.this, LogIn.class);

        startActivity(intent);

        finish();
    }




}
