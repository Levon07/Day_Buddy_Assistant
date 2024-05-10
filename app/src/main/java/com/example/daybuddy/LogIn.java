package com.example.daybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    EditText Email;
    EditText Password;
    ConstraintLayout LogIn;
    FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        LogIn = findViewById(R.id.LogIn);

        Email = findViewById(R.id.Email);
        Password  = findViewById(R.id.Password);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(mUser != null){
            Intent intent = new Intent(LogIn.this, calendar_activity.class);
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


    public void setLogIn(View view){
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        boolean isValidated = validateData(email, password);
        if(!isValidated){
            return;
        }else {
            loginAccountInFirebase(email, password);
        }
    }

    boolean validateData(String email, String password){
        // validate the data that are input by user

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6){
            Password.setError("Password length is invalid");
            return false;
        }
        return true;
    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // login is success
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to mainActivity
                        startActivity(new Intent(LogIn.this, calendar_activity.class));
                        finish();
                    } else{
                        Utility.showToast(LogIn.this, "Email not verified, Please verify your email.");
                    }
                } else{
                    // login failed
                    Utility.showToast(LogIn.this, task.getException().getLocalizedMessage());

                }
            }
        });
    }




    public void NextActivity(){
        Intent intent = new Intent(LogIn.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("auth", mAuth.getCurrentUser());
        startActivity(intent);
    }

    public void SignUp(View view) {
        Intent intent = new Intent(LogIn.this,register.class);
        startActivity(intent);
        finish();
    }
}