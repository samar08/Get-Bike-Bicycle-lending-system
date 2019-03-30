package com.example.samar.getbike;

import android.content.Intent;
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

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class lended_customer extends AppCompatActivity {
    TextView bikename,bikeidview,customernameview,customeridview,fdateview,ftimeview,tdateview,ttimeview;
    TextView costdisp,ratedisp,dop,oprice;
    TextView mobnum,onridemsg;
    ImageView cycimg,userimg;
RelativeLayout datelayout,buylayout;
    ImageButton btn;
    Button startbtn,stopbtn,cancelbtn,sellbtn;

    public void onBackPressed(){
    Intent intent=new Intent(lended_customer.this,MainActivity.class);
    startActivity(intent);
    finishAffinity();
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lended_customer);
        Intent intent=getIntent();
        final String bikeid=intent.getStringExtra("bikeid");
        String userid=intent.getStringExtra("userid");
        btn=findViewById(R.id.whatbtn);
        datelayout=findViewById(R.id.relativeLayout5);
        bikename=findViewById(R.id.bikenamedisp);
        buylayout=findViewById(R.id.buylayout);
bikeidview=findViewById(R.id.bikeiddisp);
customernameview=findViewById(R.id.customername);
cancelbtn=findViewById(R.id.cancelbtn);
sellbtn=findViewById(R.id.sellbtn);
customernameview.setText(userid);
customeridview=findViewById(R.id.userid);
        dop=findViewById(R.id.dop);
        oprice=findViewById(R.id.oprice);
        fdateview=findViewById(R.id.datefrom);
        ftimeview=findViewById(R.id.fromtimedisp);
        tdateview=findViewById(R.id.todate);
        ttimeview=findViewById(R.id.totime);
        costdisp=findViewById(R.id.costdisp);
        ratedisp=findViewById(R.id.rating);
        mobnum=findViewById(R.id.mobnum);
onridemsg=findViewById(R.id.onridedisp);
cycimg=findViewById(R.id.cycimg);
userimg=findViewById(R.id.ownerimg);
startbtn=findViewById(R.id.startbtn);
stopbtn=findViewById(R.id.stopbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toNumber="91"+mobnum.getText().toString();
                // Toast.makeText(bikeowner.this,toNumber,Toast.LENGTH_SHORT).show();
                try {
                    String text = "Hi, I accepted your request.You can collect the bicycle key from me";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        final ParseQuery<ParseObject> req=new ParseQuery<ParseObject>("rides");
        req.whereEqualTo("bikeid",bikeid);
        req.whereEqualTo("userid",userid);
        req.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
        req.whereEqualTo("ride_finish",false);
        req.addDescendingOrder("createdAt");
        req.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    if(objects.get(0).get("ride_confirm").equals(false) && objects.get(0).get("bought").equals(false)){
                        onridemsg.setVisibility(View.INVISIBLE);
                        startbtn.setVisibility(View.VISIBLE);
                        startbtn.setClickable(true);
                        cancelbtn.setClickable(false);
                        sellbtn.setClickable(false);
                        stopbtn.setVisibility(View.INVISIBLE);
                        stopbtn.setClickable(false);
                    }
                    else if(objects.get(0).get("ride_confirm").equals(false) && objects.get(0).get("bought").equals(true)){
                        onridemsg.setVisibility(View.INVISIBLE);
                        startbtn.setVisibility(View.INVISIBLE);
                        stopbtn.setVisibility(View.INVISIBLE);
                        startbtn.setClickable(false);
                        stopbtn.setClickable(false);
                        datelayout.setVisibility(View.INVISIBLE);
                        buylayout.setVisibility(View.VISIBLE);
                        cancelbtn.setClickable(true);
                        sellbtn.setClickable(true);
                    }
                    else if(objects.get(0).get("ride_confirm").equals(true) && objects.get(0).get("bought").equals(false)){
                        onridemsg.setVisibility(View.VISIBLE);
                        startbtn.setVisibility(View.INVISIBLE);
                        datelayout.setVisibility(View.VISIBLE);
                        startbtn.setClickable(false);
                        stopbtn.setVisibility(View.VISIBLE);
                        stopbtn.setClickable(true);
                    }
                }
                else if(e!=null){
                    Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
ParseQuery<ParseUser> cust=ParseUser.getQuery();
cust.whereEqualTo("username",userid);
cust.findInBackground(new FindCallback<ParseUser>() {
    @Override
    public void done(List<ParseUser> objects, ParseException e) {
        if(e==null && objects!=null){
            mobnum.setText(objects.get(0).get("mobilenumber").toString());
        customeridview.setText(objects.get(0).get("collegeid").toString());
        }
        else if(e==null){
            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
});
ParseQuery<ParseObject> picquery=new ParseQuery<ParseObject>("profilepic");
picquery.whereEqualTo("username",userid);
picquery.findInBackground(new FindCallback<ParseObject>() {
    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(e==null && objects!=null){
            ParseFile file=objects.get(0).getParseFile("profilepic");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if(e==null && data.length>0){
                        Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                        userimg.setImageBitmap(bitmap);
                    }
                    else if(e!=null){
                        Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(e==null){
            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
});
ParseQuery<ParseObject> que=new ParseQuery<ParseObject>("availbikes");
que.whereEqualTo("cycid",bikeid);
que.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
que.findInBackground(new FindCallback<ParseObject>() {
    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(objects!=null && e==null){
            for(ParseObject obj:objects){
                bikename.setText(obj.get("bikename").toString());
                bikeidview.setText(obj.get("cycid").toString());
                fdateview.setText(obj.get("fromdate").toString());
                ftimeview.setText(obj.get("fromtime").toString());
                tdateview.setText(obj.get("todate").toString());
                ttimeview.setText(obj.get("totime").toString());
                costdisp.setText(obj.get("price").toString());
            }
        }
        else if(e!=null){
            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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
                                Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
req.findInBackground(new FindCallback<ParseObject>() {
    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(e==null && objects!=null)
        {
         objects.get(0).put("ride_confirm",true);
         objects.get(0).saveInBackground();
        }
        else if(e!=null){
            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
});
                onridemsg.setVisibility(View.VISIBLE);
                startbtn.setVisibility(View.INVISIBLE);
                startbtn.setClickable(false);
                stopbtn.setVisibility(View.VISIBLE);
                stopbtn.setClickable(true);
            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null && objects!=null){
                          objects.get(0).put("ride_finish",true);
                          objects.get(0).saveInBackground();

                        }
                        else if(e!=null){
                            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ParseQuery<ParseObject> qt=new ParseQuery<ParseObject>("availbikes");
                qt.whereEqualTo("cycid",bikeid);
                qt.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
                qt.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            for(ParseObject og:objects){
                                og.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e!=null){
                                            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                        else if(e==null){
                                            Intent intent=new Intent(lended_customer.this,MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                    }
                                });
                            }
                        }
                        else if(e!=null){
                            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        sellbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            objects.get(0).put("ride_confirm",true);
                            objects.get(0).put("ride_finish",true);
                            objects.get(0).saveInBackground();
                        }
                        else if(e!=null){
                            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ParseQuery<ParseObject> qt=new ParseQuery<ParseObject>("availbikes");
                qt.whereEqualTo("cycid",bikeid);
                qt.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
                qt.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                       if(e==null){
                           for(ParseObject og:objects){
                               og.deleteInBackground(new DeleteCallback() {
                                   @Override
                                   public void done(ParseException e) {
                                       if(e!=null){
                                           Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                           }
                       }
                       else if(e!=null){
                           Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                       }
                    }
                });

                ParseQuery<ParseObject> qm=new ParseQuery<ParseObject>("bikes");
                qm.whereEqualTo("cycid",bikeid);
                qm.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
                qm.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null ){
                           objects.get(0).deleteInBackground(new DeleteCallback() {
                               @Override
                               public void done(ParseException e) {
                                   if(e!=null){
                                       Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                   }
                                   else if(e==null){
                                       Intent intent=new Intent(lended_customer.this,MainActivity.class);
                                       startActivity(intent);
                                       finishAffinity();
                                   }
                               }
                           });
                        }
                        else if(e!=null){
                            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> ut=new ParseQuery<ParseObject>("availbikes");
                ut.whereEqualTo("cycid",bikeid);
                ut.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
                ut.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            objects.get(0).put("onride",false);
                            objects.get(0).saveInBackground();
                        }
                        else if(e!=null){
                            Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            req.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e==null){
                        objects.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null)
                                {
                                    Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent intent=new Intent(lended_customer.this,MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }
                        });
                    }
                    else if(e!=null){
                        Toast.makeText(lended_customer.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


            }
        });

    }
}
