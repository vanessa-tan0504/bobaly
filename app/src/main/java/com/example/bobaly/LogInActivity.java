// log in UI
package com.example.bobaly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText inputEmail,inputPass;
    private ProgressBar progressBar;
    private Button btnSignup,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get firebase auth instance
        auth=FirebaseAuth.getInstance();

        //user session if user already login
        if(auth.getCurrentUser()!=null){
            //Toast.makeText(this, "session get", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }
        //end of user session if user already login

        setContentView(R.layout.log_in);
        inputEmail=findViewById(R.id.login_email);
        inputPass=findViewById(R.id.login_pw);
        progressBar=findViewById(R.id.progressBar_login);
        btnLogin=findViewById(R.id.login_button);
        btnSignup=findViewById(R.id.signup_button);

        //get firebase auth instance
        auth=FirebaseAuth.getInstance();

        //go to sign up interface
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this,SignUpActivity.class));
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        //end of go to sign up interface


        //log in button settings
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=inputEmail.getText().toString().trim();
                final String password=inputPass.getText().toString();

                if(TextUtils.isEmpty(email)){
                    inputEmail.setError("Enter email address");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError("Invalid email format");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    inputPass.setError("Enter password");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                if(!task.isSuccessful()){//if account cannot login
                                    Toast.makeText(LogInActivity.this, "Authentication failed. " + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{ //if account can login
                                    Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LogInActivity.this, "Welcome back! "+auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //end of log in button settings

    }
}
