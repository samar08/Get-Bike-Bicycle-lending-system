package com.example.samar.getbike;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    TextView roomno,blockname;
ImageView profilepic;
LinearLayout editblocklayout,editroomlayout;
ImageButton editblock,editroom;
EditText newblock,newroom;
ParseUser user;
Toolbar toolbar;
ImageButton backbtn;
public void editpicclicked(View view){

   if(ContextCompat.checkSelfPermission(ProfileActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
       getimage();
   }
   else{
       ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
   }
}
public void getimage(){
    Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent,1);
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
if(requestCode==0 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

    getimage();
}

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if(requestCode==1 && data!=null && resultCode==RESULT_OK){
        Uri image=data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), image);
            profilepic.setImageBitmap(bitmap);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] bytearray=stream.toByteArray();
            ParseFile file=new ParseFile("profilepic",bytearray);
            ParseObject object=new ParseObject("profilepic");
            object.put("username",user.getUsername());
            object.put("profilepic",file);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){

                    }
                    else{
                        Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    else if(data==null){
        Toast.makeText(this,"No image selected",Toast.LENGTH_SHORT).show();
    }
}

    public void editblockclicked(View view){
    editblocklayout.setVisibility(View.VISIBLE);
    editblock.setVisibility(View.INVISIBLE);
editroom.setVisibility(View.INVISIBLE);
roomno.setVisibility(View.INVISIBLE);
blockname.setVisibility(View.INVISIBLE);
}
public void saveblockclicked(View view){
    editblocklayout.setVisibility(View.INVISIBLE);
    editblock.setVisibility(View.VISIBLE);
    editroom.setVisibility(View.VISIBLE);
    roomno.setVisibility(View.VISIBLE);
    user.put("blockname",newblock.getText().toString());
    user.saveInBackground();
    blockname.setText(user.get("blockname").toString());
    blockname.setVisibility(View.VISIBLE);
}
public void cancelblockclicked(View view){
    editblocklayout.setVisibility(View.INVISIBLE);
    editblock.setVisibility(View.VISIBLE);
    editroom.setVisibility(View.VISIBLE);
    roomno.setVisibility(View.VISIBLE);
    blockname.setVisibility(View.VISIBLE);

}
public void editroomclicked(View view){
   editroomlayout.setVisibility(View.VISIBLE);
   editroom.setVisibility(View.INVISIBLE);
   editblock.setVisibility(View.INVISIBLE);
   roomno.setVisibility(View.INVISIBLE);
   blockname.setVisibility(View.INVISIBLE);

}
public void saveroomclicked(View view){
    user.put("roomnumber",newroom.getText().toString());
    user.saveInBackground();
    roomno.setText(user.get("roomnumber").toString());
    editroomlayout.setVisibility(View.INVISIBLE);
    editroom.setVisibility(View.VISIBLE);
    editblock.setVisibility(View.VISIBLE);
    roomno.setVisibility(View.VISIBLE);
    blockname.setVisibility(View.VISIBLE);

}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
startActivity(new Intent(ProfileActivity.this,MainActivity.class));
finish();

}

    public void cancelroomclicked(View view){
    editroomlayout.setVisibility(View.INVISIBLE);
    editroom.setVisibility(View.VISIBLE);
    editblock.setVisibility(View.VISIBLE);
    roomno.setVisibility(View.VISIBLE);
    blockname.setVisibility(View.VISIBLE);

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView username=findViewById(R.id.usernamedisp);
        TextView regno=findViewById(R.id.regdisp);
        TextView email=findViewById(R.id.emaildisp);
        TextView mobileno=findViewById(R.id.mobiledisp);
        TextView college=findViewById(R.id.collegenamedisp);
        toolbar=(Toolbar)findViewById(R.id.profiletoolbar);
        setSupportActionBar(toolbar);
        backbtn=findViewById(R.id.backbtn);

        newblock=findViewById(R.id.newblock);
        user=ParseUser.getCurrentUser();
        newroom=findViewById(R.id.newroom);
        roomno=findViewById(R.id.roomnumberdisp);
        profilepic=(ImageView)findViewById(R.id.profilepic);
        blockname=findViewById(R.id.blockdisp);
        editblocklayout=(LinearLayout)findViewById(R.id.editblocklayout);
        editroomlayout=findViewById(R.id.editroomlayout);
       editblock=findViewById(R.id.editblock);
       editroom=findViewById(R.id.editroomnumber);
        username.setText(ParseUser.getCurrentUser().getUsername());
        email.setText(ParseUser.getCurrentUser().getEmail());
        mobileno.setText(ParseUser.getCurrentUser().get("mobilenumber").toString());
        college.setText(ParseUser.getCurrentUser().get("collegename").toString());
        regno.setText(ParseUser.getCurrentUser().get("collegeid").toString());
        blockname.setText(ParseUser.getCurrentUser().get("blockname").toString());
        roomno.setText(ParseUser.getCurrentUser().get("roomnumber").toString());
editroomlayout.setVisibility(View.INVISIBLE);
editblocklayout.setVisibility(View.INVISIBLE);
backbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finish();
    }
});
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("profilepic");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addDescendingOrder("createdAt");
        query.setLimit(1);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){


                    for(ParseObject obj:objects) {

                        ParseFile file=(ParseFile)obj.get("profilepic");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null && data.length>0){
                                    Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                    profilepic.setImageBitmap(bitmap);
                                }
                                else{
                                    Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }

            }
        });
    }
}
