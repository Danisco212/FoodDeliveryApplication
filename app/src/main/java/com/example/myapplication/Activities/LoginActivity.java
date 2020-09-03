package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Helpers.CheckUserInput;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText email, password;
    private TextView error;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize(){
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        error = findViewById(R.id.errorMessage);
        loading = findViewById(R.id.loading);
    }

    // logging in the user
    public void loginUser(View view){
        loading.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        CheckUserInput checkUserInput = new CheckUserInput();
        if (checkUserInput.correctInputs(email, password)){ // fields are filled
            if (checkUserInput.validEmail(email)){ // email is valid
                if (checkUserInput.passwordLength(password)){ // password is ore than 6 characters
                    mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){ // user signed in
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    }else { // user not signed in
                                        showErrorMessages(task.getException().getMessage());
                                    }
                                }
                            });
                }else{// password less than 6 characters
                    showErrorMessages("Password must be at least 6 characters long");
                }
            }else{// email is not valid
                showErrorMessages("Invalid email address");
            }
        }else{// fields are empty
            showErrorMessages("Fill all fields");
        }
    }

    // showing the error messages to the user
    private void showErrorMessages(String errorMsg){
        loading.setVisibility(View.GONE);
        error.setText(errorMsg);
        error.setVisibility(View.VISIBLE);
    }

    // registration activity - called in the xml file
    public void register(View view){
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}