package com.example.samar.getbike;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    ProgressBar spinner;
    public void signupclicked(View view){
        Intent signintent=new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(signintent);
        // finish();
    }
    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!error) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    public void loginclicked(View view){
        spinner.setVisibility(View.VISIBLE);

        password.setError(null);
        if(email.getText().toString()=="" || password.getText().toString()==""){
            Toast.makeText(LoginActivity.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
            spinner.setVisibility(View.INVISIBLE);
        }
        else{
            ParseUser.logInInBackground(email.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null) {
                        if(parseUser.getBoolean("emailVerified")) {
                            spinner.setVisibility(View.INVISIBLE);
                           // alertDisplayer("Login Sucessful", "Welcome, " + ParseUser.getCurrentUser().getUsername() + "!", false);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            spinner.setVisibility(View.INVISIBLE);
                            ParseUser.logOut();
                            alertDisplayer("Login Fail", "Please Verify Your Email first", true);
                        }
                    } else {
                        spinner.setVisibility(View.INVISIBLE);
                        ParseUser.logOut();
                        alertDisplayer("Login Fail", e.getMessage() + " Please re-try", true);
                    }
                }
            });

        }

// Login with Parse



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.emaillogin);
        password=findViewById(R.id.passwordlogin);
        spinner=(ProgressBar)findViewById(R.id.progressbar);
        spinner.setVisibility(View.INVISIBLE);
    }
}
