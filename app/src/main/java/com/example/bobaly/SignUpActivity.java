//sign up UI
package com.example.bobaly;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText inputEmail,inputPw,inputConfirmPw,inputUser;
    private Button btnRegister,btnLogIn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //get firebase auth instance
        auth=FirebaseAuth.getInstance();

        inputEmail=findViewById(R.id.signup_email);
        inputPw=findViewById(R.id.signup_pw);
        inputConfirmPw=findViewById(R.id.signup_confirmpw);
        inputUser=findViewById(R.id.signup_username);
        btnRegister=findViewById(R.id.sign_up_button);
        btnLogIn=findViewById(R.id.log_in_button);
        progressBar=findViewById(R.id.progressBar_signup);

        //go to log in interface
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        //end of go to log in interface

        //register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString().trim();
                String password = inputPw.getText().toString().trim();
                String confirm_pass = inputConfirmPw.getText().toString().trim();
                String username = inputUser.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    inputUser.setError("Enter user name");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter email address");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError("Invalid email format");
                    return;

                }

                if (TextUtils.isEmpty(password)) {
                    inputPw.setError("Enter password");
                    return;
                }

                if (password.length() < 6) {
                    inputPw.setError("Password too short");
                    return;
                }

                if (TextUtils.isEmpty(confirm_pass)) {
                    inputConfirmPw.setError("Enter confirm password");
                    return;
                }

                if (!password.equals(confirm_pass)) {
                    inputConfirmPw.setError("Confirm password not same with password");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //if all fields okay, create user to firebase
                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.INVISIBLE);

                                if(!task.isSuccessful()){//if account cannot register
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{// if account register success ,store user's username
                                    final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder()
                                            .setDisplayName(inputUser.getText().toString().trim())
                                            .build();
                                    user.updateProfile(profileChangeRequest)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SignUpActivity.this, "Welcome! "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
        //end of register button


    }
}
