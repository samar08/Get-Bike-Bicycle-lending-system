package com.example.samar.getbike;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    EditText username;
    EditText mobileno;
    EditText email;
    EditText password;
    EditText confirmpassword;
EditText Blockname,roomnumber,collegename,collegeid;
    TextView t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.confirmpassword);
        email=findViewById(R.id.emailsignup);
        mobileno=findViewById(R.id.mobilenumber);
        Blockname=findViewById(R.id.blockname);
        roomnumber=findViewById(R.id.roomnumber);
        collegename=findViewById(R.id.collegename);
        collegeid=findViewById(R.id.collegeid);
        t2 = (TextView) findViewById(R.id.websiteurl);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!error) {
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    public void registerclicked(View view){
        if(password.getText().toString().length()==0 || confirmpassword.getText().toString().length()==0 || username.getText().toString().length()==0 || email.getText().toString().length()==0 || mobileno.getText().toString().length()==0 || collegename.getText().toString().length()==0 || roomnumber.getText().toString().length()==0 || Blockname.getText().toString().length()==0 || collegeid.getText().toString().length()==0){
            Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show();
        }
        else{
            if(password.getText().toString().equals(confirmpassword.getText().toString())) {
                Toast.makeText(this,"register reached",Toast.LENGTH_SHORT).show();
                try {
                    // Reset errors
    email.setError(null);
    password.setError(null);


                    ParseUser user=new ParseUser();
                    user.setUsername(username.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.put("collegeid",collegeid.getText().toString());
                    user.put("collegename",collegename.getText().toString());
                    user.put("blockname",Blockname.getText().toString());
                    user.put("roomnumber",roomnumber.getText().toString());
                    user.put("mobilenumber",mobileno.getText().toString());

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseUser.logOut();
                                alertDisplayer("Account Created Successfully!", "Please verify your email before Login", false);
                            } else {
                                ParseUser.logOut();
                                alertDisplayer("Error Account Creation failed", "Account could not be created" + " :" + e.getMessage(), true);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }






            }
            else{
                Toast.makeText(this.getApplicationContext(),"Password is not matching with Confirm Password",Toast.LENGTH_SHORT).show();
            }
        }

    }

}
