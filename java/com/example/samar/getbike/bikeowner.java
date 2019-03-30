package com.example.samar.getbike;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.EventListener;
import java.util.List;

public class bikeowner extends AppCompatActivity {
    TextView bikename,bikeidview,usernameview,useridview,fdateview,ftimeview,tdateview,ttimeview;
    TextView costdisp,ratedisp,dop,oprice;
TextView mobnum,email,reqmessage;
ImageView cycimg,ownerimg;
RelativeLayout datedisp;
ImageButton btn;
Button reqbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikeowner);
        Intent bikeint=getIntent();
        final String bikeid=bikeint.getStringExtra("bikeid");
        //Toast.makeText(bikeowner.this,bikeid,Toast.LENGTH_SHORT).show();
        bikename=findViewById(R.id.bikenamedisp);
        final String[] userperu = new String[1];
        btn=findViewById(R.id.whatbtn);
        bikeidview=findViewById(R.id.bikeiddisp);
        usernameview=findViewById(R.id.ownername);
        useridview=findViewById(R.id.userid);
        reqmessage=findViewById(R.id.req_message);
        datedisp=findViewById(R.id.relativeLayout5);
dop=findViewById(R.id.dop);
oprice=findViewById(R.id.oprice);
fdateview=findViewById(R.id.datefrom);
ftimeview=findViewById(R.id.fromtimedisp);
tdateview=findViewById(R.id.todate);
ttimeview=findViewById(R.id.totime);
costdisp=findViewById(R.id.costdisp);
ratedisp=findViewById(R.id.rating);
mobnum=findViewById(R.id.mobnum);
//email=findViewById(R.id.gmaildisp);
cycimg=findViewById(R.id.cycimg);
reqbtn=findViewById(R.id.reqbtn);
ownerimg=findViewById(R.id.ownerimg);
        ParseQuery<ParseObject> qur=new ParseQuery<ParseObject>("availbikes");
        qur.whereEqualTo("cycid",bikeid);
        qur.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject obj=objects.get(0);
                userperu[0] =obj.get("ownerid").toString();
               // Toast.makeText(bikeowner.this,userperu[0],Toast.LENGTH_SHORT).show();
                usernameview.setText(userperu[0]);
                bikename.setText(obj.get("bikename").toString());
                bikeidview.setText(obj.get("cycid").toString());
                if(obj.get("rent").equals(true) && obj.get("sell").equals(false)) {
                    fdateview.setText(obj.get("fromdate").toString());
                    ftimeview.setText(obj.get("fromtime").toString());
                    tdateview.setText(obj.get("todate").toString());
                    ttimeview.setText(obj.get("totime").toString());
                }
                else{
                    datedisp.setVisibility(View.INVISIBLE);
                }
costdisp.setText(obj.get("price").toString());
ParseQuery<ParseObject> findreq=new ParseQuery<ParseObject>("requests");
findreq.whereEqualTo("customerid",ParseUser.getCurrentUser().getUsername().toString());
findreq.whereEqualTo("bikeid",bikeid);
findreq.whereEqualTo("ownerid",userperu[0]);
findreq.findInBackground(new FindCallback<ParseObject>() {
    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(e==null && objects.size()>0){
            reqbtn.setVisibility(View.INVISIBLE);
            reqmessage.setVisibility(View.VISIBLE);
            reqbtn.setClickable(false);
        }
        else if(objects.size()==0){
            reqbtn.setVisibility(View.VISIBLE);
            reqmessage.setVisibility(View.INVISIBLE);
            reqbtn.setClickable(true);
        }
    }
});

                ParseQuery<ParseUser> qb=ParseUser.getQuery();
                qb.whereEqualTo("username", userperu[0]);
               //Toast.makeText(bikeowner.this,userperu[0],Toast.LENGTH_SHORT).show();
                qb.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(objects.size()>0 && e==null) {
                            for (ParseUser ur : objects) {
                                useridview.setText(ur.get("collegeid").toString());
                                //Toast.makeText(bikeowner.this,ur.get("mobilenumber").toString(),Toast.LENGTH_SHORT).show();
                                mobnum.setText(ur.get("mobilenumber").toString());
                               /* Toast.makeText(bikeowner.this,ur.getEmail().toString(),Toast.LENGTH_SHORT).show();
                                email.setText(ur.getEmail().toString());*/

                            ParseQuery<ParseObject> imk=new ParseQuery<ParseObject>("profilepic");
                            imk.whereEqualTo("username",userperu[0]);
                            imk.addDescendingOrder("createdAt");
                            imk.setLimit(1);
                            imk.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    ParseObject io=objects.get(0);
                                    ParseFile ifile=io.getParseFile("profilepic");
                                    ifile.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if(e==null && data.length>0){
                                                Bitmap bit=BitmapFactory.decodeByteArray(data,0,data.length);
                                                ownerimg.setImageBitmap(bit);
                                            }
                                            else{
                                                Toast.makeText(bikeowner.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                            }
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String toNumber="91"+mobnum.getText().toString();
                                    // Toast.makeText(bikeowner.this,toNumber,Toast.LENGTH_SHORT).show();
                                    try {
                                        String text = "Hi,I want to buy your bike: "+bikename.getText().toString()+", id: "+bikeid;// Replace with your message.
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                                        startActivity(intent);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                           reqbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ParseObject rj=new ParseObject("requests");
                                    rj.put("customerid",ParseUser.getCurrentUser().getUsername());
                                    rj.put("ownerid",userperu[0]);
                                    rj.put("bikeid",bikeid);
                                    rj.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e==null){
                                                reqbtn.setVisibility(View.INVISIBLE);
                                                reqmessage.setVisibility(View.VISIBLE);
                                                reqbtn.setClickable(false);
                                                Toast.makeText(bikeowner.this, "Request sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    }
                            });
                        }
                        else if(e!=null){
                            Toast.makeText(bikeowner.this,objects.size(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        ParseQuery<ParseObject> ut=new ParseQuery<ParseObject>("bikes");
        ut.whereEqualTo("cycid",bikeid);
        ut.setLimit(1);
        ut.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject ol:objects){
                    ratedisp.setText(ol.get("rating").toString());
                    oprice.setText(ol.get("originalprice").toString());
                    dop.setText(ol.get("dopday").toString()+"/"+ol.get("dopmonth").toString()+"/"+ol.get("dopyear").toString());
ParseFile file= (ParseFile) ol.getParseFile("cycpic");
file.getDataInBackground(new GetDataCallback() {
    @Override
    public void done(byte[] data, ParseException e) {
        if(e==null && data.length>0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            cycimg.setImageBitmap(bitmap);
        }
        else{
            Toast.makeText(bikeowner.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
});
                }
            }
        });

    }
}
