package com.example.samar.getbike;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

public class UploadBike extends AppCompatActivity {

    ImageView cycimage,backbtn;
    Button upload;

    ProgressBar spinner;
TextView cycid,dop;
    ImageButton editcyc,dopbtn;
    DatePickerDialog.OnDateSetListener setListener3;
    DatePickerDialog datePickerDialog;
    int dday,dmonth,dyear;
    ParseUser user;
    ParseFile file;String cycleid;int count;
    ParseObject object;

    EditText bikename,oprice;

    int opt;

    public void editcycclicked(View view){
        if(ContextCompat.checkSelfPermission(UploadBike.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(UploadBike.this.getContentResolver(), image);
                cycimage.setImageBitmap(bitmap);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bytearray=stream.toByteArray();
                file=new ParseFile("cycpic",bytearray);

            }
            catch(Exception e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else if(data==null){
            Toast.makeText(this,"No image selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UploadBike.this,MainActivity.class));
        finish();
    }
public void addclicked(View view){
    spinner.setVisibility(View.VISIBLE);
ParseObject object =new ParseObject("bikes");

object.put("cycpic",file);
object.put("originalprice",Integer.parseInt(oprice.getText().toString()));
object.put("name",bikename.getText().toString());
    object.put("cycid",cycleid);
    object.put("ownerid",user.getUsername());
    object.put("dopday",dday);object.put("dopmonth",dmonth);object.put("dopyear",dyear);
object.put("rating",0);
object.put("popularity",0);
object.saveInBackground(new SaveCallback() {
    @Override
    public void done(ParseException e) {
        if(e==null){
            Toast.makeText(UploadBike.this,"Bike added successfully",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadBike.this,MainActivity.class));
            finishAffinity();
        }
        else{
            Toast.makeText(UploadBike.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
});
}

public int chkcycid(String cycleid){

       ParseQuery<ParseObject> list=new ParseQuery<ParseObject>("bikes");
       list.whereEqualTo("cycid",cycleid);
    try {
        opt=list.count();
    } catch (ParseException e) {
        e.printStackTrace();
    }

    if(opt>0){
       return 0;
    }else{
        return 1;
    }


}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bike);
        bikename=findViewById(R.id.cycname);
        Calendar cal=Calendar.getInstance();
user=ParseUser.getCurrentUser();
oprice=findViewById(R.id.cycprice);
cycid=findViewById(R.id.cycleid2);
cycimage=(ImageView)findViewById(R.id.cycleimgid);
editcyc=(ImageButton)findViewById(R.id.editcycbtn);
backbtn=findViewById(R.id.backbtn);
        dopbtn=findViewById(R.id.editdop);
        dop=findViewById(R.id.dopdisp);
        spinner=(ProgressBar)findViewById(R.id.progressbar);
        spinner.setVisibility(View.INVISIBLE);
        dyear=cal.get(Calendar.YEAR);dmonth=cal.get(Calendar.MONTH);dday=cal.get(Calendar.DAY_OF_MONTH);
ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("bikes");
try{
 count=query.count();
}
catch(Exception e) {
    Toast.makeText(UploadBike.this,e.getMessage(),Toast.LENGTH_SHORT).show();
}
        cycleid=user.get("collegeid").toString()+count;
        //Toast.makeText(UploadBike.this,cycleid,Toast.LENGTH_SHORT).show();
while(chkcycid(cycleid)!=1){
//    Toast.makeText(UploadBike.this,chkcycid(cycleid),Toast.LENGTH_SHORT).show();
    count++;
    cycleid=user.get("collegeid").toString()+count;
}
       // Toast.makeText(UploadBike.this,cycleid,Toast.LENGTH_SHORT).show();
cycid.setText(cycleid);
 dopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog=new DatePickerDialog(
                        UploadBike.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener3,dyear,dmonth,dday);
                datePickerDialog.show();
            }
        });
        setListener3=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dyear=year;dmonth=month+1;dday=dayOfMonth;
                dop.setText(dday+"/"+dmonth+"/"+dyear);

            }
        };

    backbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(UploadBike.this,MainActivity.class));
            finish();
        }
    });

    }
}
