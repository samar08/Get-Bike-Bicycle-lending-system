package com.example.samar.getbike;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LendActivity extends AppCompatActivity {
    ImageButton fromdatebtn,todatebtn,fromtimebtn,totimebtn;
    DatePickerDialog.OnDateSetListener setListener1 ,setListener2;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    ImageView cycimg;
    String bikename;
    Button lendbtn;
    float oprice;
    ProgressBar spinner;
    TimePickerDialog.OnTimeSetListener timeSetListener1,timeSetListener2;
    RelativeLayout fromlayout,tolayout;
    int fyear,fmonth,fday,tyear,tmonth,tday,fhour,fmin,thour,tmin,count;
    TextView fromdate,todate,fromtime,totime,cycid,pricedisp;
    Boolean rent=Boolean.FALSE,sell=Boolean.FALSE;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH);
    SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
    int op;
String dopstring;
int dopday,dopmonth,dopyear;
    public void rentclicked(View view){
        if(view.getId()==R.id.rent){
            fromlayout.setVisibility(View.VISIBLE);
            tolayout.setVisibility(View.VISIBLE);
            rent=true;
            sell=false;
        }
        else if(view.getId()==R.id.sell) {
            fromlayout.setVisibility(View.INVISIBLE);
            tolayout.setVisibility(View.INVISIBLE);
            sell=true;rent=false;
        }
    }
    public void uploadclicked(View view){
        lendbtn.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.VISIBLE);
        ParseObject obj=new ParseObject("availbikes");
        obj.put("ownerid",ParseUser.getCurrentUser().getUsername());
        obj.put("cycid",cycid.getText().toString());
        obj.put("fromdate",fromdate.getText().toString());
        obj.put("todate",todate.getText().toString());
    obj.put("onride",false);
    obj.put("fromtime",fromtime.getText().toString());
    obj.put("totime",totime.getText().toString());
    obj.put("bikename",bikename);
    if(sell==true && rent==false){
        obj.put("sell",true);obj.put("rent",false);
    }
    else if(rent==true && sell==false){
        obj.put("rent",true);obj.put("sell",false);
    }
    obj.put("price",pricedisp.getText().toString());
    obj.saveInBackground(new SaveCallback() {
        @Override
        public void done(ParseException e) {
            if(e==null){
                Toast.makeText(LendActivity.this,"Uploaded successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LendActivity.this,MainActivity.class));
                finish();
                lendbtn.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.INVISIBLE);
            }
            else{
                Toast.makeText(LendActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);
Intent inte=getIntent();
String cycidstring=inte.getStringExtra("cycid");
lendbtn=findViewById(R.id.lendbtn);
        lendbtn.setVisibility(View.VISIBLE);

        Calendar cal=Calendar.getInstance();
        fromlayout=findViewById(R.id.fromlayout);
cycimg=findViewById(R.id.imageView2);
        tolayout=findViewById(R.id.tolayout);
        fromlayout.setVisibility(View.INVISIBLE);
        tolayout.setVisibility(View.INVISIBLE);
        fyear=cal.get(Calendar.YEAR);fmonth=cal.get(Calendar.MONTH);fday=cal.get(Calendar.DAY_OF_MONTH);
        tyear=cal.get(Calendar.YEAR);tmonth=cal.get(Calendar.MONTH);tday=cal.get(Calendar.DAY_OF_MONTH);
        spinner=(ProgressBar)findViewById(R.id.progressbar);
        spinner.setVisibility(View.INVISIBLE);
Button calprice=findViewById(R.id.calpricebtn);
        fhour=cal.get(Calendar.HOUR_OF_DAY);fmin=cal.get(Calendar.MINUTE);
        thour=cal.get(Calendar.HOUR_OF_DAY); tmin=cal.get(Calendar.MINUTE);
        todate=findViewById(R.id.datedisp2);
        fromdate=findViewById(R.id.datedisp);
pricedisp=findViewById(R.id.pricedisp);
        totime=findViewById(R.id.timedisp2);
        fromtime=findViewById(R.id.timedisp);
        fromdatebtn=findViewById(R.id.fromdatebtn);
        fromtimebtn=findViewById(R.id.fromtimebtn);
        todatebtn=findViewById(R.id.todatebtn);
        totimebtn=findViewById(R.id.totimebtn);
        cycid=findViewById(R.id.cid);
        cycid.setText(cycidstring);
        ImageButton backbtn= findViewById(R.id.backbtn);
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("bikes");
        query.whereEqualTo("cycid",cycidstring);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null) {
                    for (ParseObject obj4 : objects) {
                        dopyear=(int)obj4.get("dopyear");dopday=(int)obj4.get("dopday");dopmonth=(int)obj4.get("dopmonth");
                        dopstring=dopday+"/"+dopmonth+"/"+dopyear;
                        op=(int)obj4.get("originalprice");
                       bikename= (String) obj4.get("name");
                       oprice=Float.parseFloat(obj4.get("originalprice").toString());
                      ParseFile file=(ParseFile)obj4.get("cycpic");
                      file.getDataInBackground(new GetDataCallback() {
                          @Override
                          public void done(byte[] data, ParseException e) {
                              Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                              cycimg.setImageBitmap(bitmap);
                          }
                      });
                    }
                }
                else{
                    Toast.makeText(LendActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        fromtimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog=new TimePickerDialog(LendActivity.this,timeSetListener1,fhour,fmin,false);
                timePickerDialog.show();
            }
        });
        totimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog=new TimePickerDialog(LendActivity.this,timeSetListener2,thour,tmin,false);
                timePickerDialog.show();
            }
        });

        fromdatebtn.setOnClickListener (new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                datePickerDialog=new DatePickerDialog(
                                                        LendActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener1,fyear,fmonth,fday);
                                                datePickerDialog.show();
                                            }
                                        }
        );
        todatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog=new DatePickerDialog(
                        LendActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener2,tyear,tmonth,tday);
                datePickerDialog.show();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LendActivity.this,Mybikes.class));
                finish();
            }
        });
        setListener1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fyear=year;fmonth=month+1;fday=dayOfMonth;
                fromdate.setText(fday+"/"+fmonth+"/"+fyear);

            }
        };
        setListener2=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tyear=year;tmonth=month+1;tday=dayOfMonth;
                todate.setText(tday+"/"+tmonth+"/"+tyear);

            }
        };

        timeSetListener1=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                fhour=hourOfDay;fmin=minute;
                fromtime.setText(fhour+":"+fmin);
            }
        };
        timeSetListener2=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                thour=hourOfDay;tmin=minute;
                totime.setText(thour+":"+tmin);
            }
        };
        calprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(rent==true) {
                  try {
                      Date date1 = simpleDateFormat.parse(fromdate.getText().toString() + " " + fromtime.getText().toString());
                      Date date2 = simpleDateFormat.parse(todate.getText().toString() + " " + totime.getText().toString());
                      long diff = date2.getTime() - date1.getTime();

                      long seconds = diff / 1000;

                      if (seconds < 3600.0 ) {
                          Toast.makeText(LendActivity.this, "Minimum time 60 minutes", Toast.LENGTH_SHORT).show();
                      }
                      else if(seconds>86400){
                          Toast.makeText(LendActivity.this, "Maximum time 24 hours", Toast.LENGTH_SHORT).show();
                      }
                      else {
                          int resp=(int) ((double) seconds * 0.002778);
                          int resop=(int)((double)oprice/(1000));
                          float dec=(float)seconds/3600;
                          int res= (int) ((resop+resp)*(1-dec*0.01));
                          pricedisp.setText(res+"");
                      }

                  } catch (java.text.ParseException e) {
                      Toast.makeText(LendActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                  }
              }
              else if(sell==true){
                       int sr= (int) (op*(0.66));
                 // Toast.makeText(LendActivity.this,op+"",Toast.LENGTH_SHORT).show();
                  pricedisp.setText(sr+"");
                      }
            }
        });
        }
}
