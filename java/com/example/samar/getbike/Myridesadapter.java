package com.example.samar.getbike;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class Myridesadapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    public Myridesadapter(Context context,ArrayList<String> values){
        super(context,R.layout.myrides_rowlayout,values);
        this.context=context;
        this.values=values;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final int redcolor=0xffff0000;
        final int yellowcolor=0xffCCCC00;
        final int greencolor=0xff008000;
        final int orangecolor=0xffFF4500;
        final LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview=inflater.inflate(R.layout.myrides_rowlayout,parent,false);
        final RatingBar ratebar=rowview.findViewById(R.id.ratebar);
        final TextView fromdate,fromtime,todate,totime,username,userid,bikename,bikeid,dop,oprice,rating,mobnum,price,status;
        final Button submitbtn=rowview.findViewById(R.id.submitratebtn),ratebikebtn=rowview.findViewById(R.id.ratethebikebtn),submitreview=rowview.findViewById(R.id.submitreview);
        final RelativeLayout ratinglayout=rowview.findViewById(R.id.ratinglayout),submitratelayout=rowview.findViewById(R.id.giveratinglayout),date_layout=rowview.findViewById(R.id.date_layout);
        final ImageView bikepic=rowview.findViewById(R.id.bikepic),ownerpic=rowview.findViewById(R.id.ownerpic);
        final ImageButton whatbtn=rowview.findViewById(R.id.whatbtn),reviewbtn=rowview.findViewById(R.id.reviewbtn);
        final RelativeLayout mobilelayout=rowview.findViewById(R.id.mobilelayout),reviewlayout=rowview.findViewById(R.id.review_layout);
        final EditText reviewtext=rowview.findViewById(R.id.reviewtext);
        status=rowview.findViewById(R.id.statusview);
        mobnum=rowview.findViewById(R.id.mobnum);
        fromdate=rowview.findViewById(R.id.fromdateview);fromtime=rowview.findViewById(R.id.fromtimeview);
        todate=rowview.findViewById(R.id.todateview);totime=rowview.findViewById(R.id.totimeview);
        username=rowview.findViewById(R.id.ownername);userid=rowview.findViewById(R.id.ownerid);
        bikename=rowview.findViewById(R.id.bikename2);bikeid=rowview.findViewById(R.id.bikeid2);
        dop=rowview.findViewById(R.id.dop2); price=rowview.findViewById(R.id.price2); oprice=rowview.findViewById(R.id.opricetext);
    rating=rowview.findViewById(R.id.ratedisp);
        final ParseQuery<ParseObject> rideq=new ParseQuery<ParseObject>("rides");
        rideq.whereEqualTo("objectId",values.get(position));
        rideq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    for(ParseObject ride:objects){
                        username.setText(ride.get("ownerid").toString());
                        bikeid.setText(ride.get("bikeid").toString());
                        fromdate.setText(ride.get("fromdate").toString());
                        fromtime.setText(ride.get("fromtime").toString());
                        todate.setText(ride.get("todate").toString());
                        totime.setText(ride.get("totime").toString());
                        bikename.setText(ride.get("bikename").toString());
                        dop.setText(ride.get("dop").toString());
                        oprice.setText(ride.get("oprice").toString());
                        price.setText(ride.get("price").toString());
                        if(ride.get("bought").equals(true) && ride.get("ride_finish").equals(true)){
                            date_layout.setVisibility(View.INVISIBLE);
                        }
                        if(ride.get("ride_confirm").equals(true) && ride.get("ride_finish").equals(false) ){
                            status.setText("On ride");
                            status.setTextColor(greencolor);

                        }
                        else if(ride.get("ride_confirm").equals(false)){
                            status.setTextColor(yellowcolor);
                            status.setText("Request accepted");
                        }
                        else if(ride.get("ride_confirm").equals(true) && ride.get("ride_finish").equals(true) && ride.get("bought").equals(false)){
                            status.setText("Ride finished");
                            status.setTextColor(redcolor);

                        }
                        else if(ride.get("ride_confirm").equals(true) && ride.get("ride_finish").equals(true) && ride.get("bought").equals(true)){
                            status.setText("Bought");
                            status.setTextColor(orangecolor);
                        }
                        if(ride.get("ride_finish").equals(true) && ride.get("rate_given").equals(false) && ride.get("bought").equals(false)){
                            submitratelayout.setVisibility(View.VISIBLE);
                            ratebikebtn.setClickable(true);

                        }
                        if(ride.get("ride_finish").equals(true) && ride.get("review_given").equals(false)){
                            reviewbtn.setVisibility(View.VISIBLE);
                            reviewbtn.setClickable(true);
                        }

                        ParseFile file=ride.getParseFile("cycpic");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null && data.length>0){
                                    Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                    bikepic.setImageBitmap(bitmap);
                                }
                                else if(e!=null){
                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        ParseQuery<ParseObject> userp=new ParseQuery<ParseObject>("profilepic");
                        userp.whereEqualTo("username",ride.get("ownerid").toString());
                        userp.addDescendingOrder("createdAt");
                        userp.setLimit(1);
                        userp.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null) {
                                    for (ParseObject ob:objects) {
                                      ParseFile file2=ob.getParseFile("profilepic");
                                      file2.getDataInBackground(new GetDataCallback() {
                                          @Override
                                          public void done(byte[] data, ParseException e) {
                                              if(e==null && data.length>0){
                                                Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                                ownerpic.setImageBitmap(bitmap);
                                              }
                                              else if(e!=null){
                                                  Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      });

                                    }
                                }
                                else if (e!=null){
                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        ParseQuery<ParseUser> qr=ParseUser.getQuery();
                        qr.whereEqualTo("username",ride.get("ownerid").toString());
                        qr.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                if(e==null && objects!=null){
                                    for(ParseObject id:objects){
                                        userid.setText(id.get("collegeid").toString());
                                        mobnum.setText(id.get("mobilenumber").toString());
                                    }
                                }else if(e!=null){
                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
/*ParseQuery<ParseObject> bike=new ParseQuery<ParseObject>("bikes");
bike.whereEqualTo("cycid",ride.get("bikeid").toString());
bike.whereEqualTo("ownerid",ride.get("ownerid").toString());
bike.findInBackground(new FindCallback<ParseObject>() {
    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(objects!=null && e==null){
            rating.setText(objects.get(0).get("rating").toString());

        }
        else if(e!=null){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
});*/
                    }
                }
                else if(e!=null){
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        ratebikebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ratinglayout.setVisibility(View.VISIBLE);
               submitratelayout.setVisibility(View.INVISIBLE);
               ratebikebtn.setClickable(false);
               ratebar.setClickable(true);
               submitbtn.setClickable(true);

            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> rt=new ParseQuery<ParseObject>("bikes");
                rt.whereEqualTo("cycid",bikeid.getText().toString());
                rt.whereEqualTo("ownerid",username.getText().toString());
                rt.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null && objects!=null){
                            for(ParseObject mk:objects){
                                DecimalFormat df2 = new DecimalFormat(".#");
                              float avgrate=Float.parseFloat(mk.get("rating").toString());
                              int pop=(int)mk.get("popularity");
                              float prrate=ratebar.getRating();
                            double res=((pop*avgrate)+ prrate)/(pop+1);
                            res=Double.parseDouble(df2.format(res));
                            mk.put("rating",res);
                            mk.put("popularity",pop+1);
                            mk.saveInBackground();
                            }
                        }
                        else if(e!=null){
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
               ParseQuery<ParseObject> ms=new ParseQuery<ParseObject>("rides");
                ms.whereEqualTo("objectId",values.get(position));
                ms.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(objects!=null && e==null){
                            for(ParseObject ot:objects){
                                ot.put("rate_given",true);
                                ot.saveInBackground();
                            }
                        }
                        else if(e!=null){
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ratinglayout.setVisibility(View.INVISIBLE);
                submitratelayout.setVisibility(View.INVISIBLE);
                ratebikebtn.setClickable(false);
                ratebar.setClickable(false);
                submitbtn.setClickable(false);
            }
        });
        whatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toNumber = "91" + mobnum.getText().toString();
                // Toast.makeText(bikeowner.this,toNumber,Toast.LENGTH_SHORT).show();
                try {
                    String text = "";// Replace with your message.
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
                    startActivity(context,intent,Bundle.EMPTY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobilelayout.setVisibility(View.VISIBLE);
                whatbtn.setClickable(true);
                reviewbtn.setClickable(false);
reviewlayout.setVisibility(View.INVISIBLE);
submitreview.setClickable(false);
reviewtext.setClickable(false);

final AlertDialog.Builder dialog=new AlertDialog.Builder(context);
View mview=inflater.inflate(R.layout.reviewlayout,null,false);
final EditText rev=mview.findViewById(R.id.editreview);
Button submit=mview.findViewById(R.id.submitrebtn);
Button cancel=mview.findViewById(R.id.cancelrebtn);
                dialog.setView(mview);
                final AlertDialog alertDialog=dialog.create();
                alertDialog.show();
submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        rideq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    objects.get(0).put("review_given",true);
                    objects.get(0).put("review",rev.getText().toString());
                    objects.get(0).saveInBackground();
                    reviewbtn.setVisibility(View.INVISIBLE);
                    reviewbtn.setClickable(false);
                    alertDialog.dismiss();
                }
                else if(e!=null){
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
});
cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        reviewbtn.setClickable(true);
        reviewbtn.setVisibility(View.VISIBLE);
    alertDialog.dismiss();
    }
});

            }
        });
        submitreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> pl=new ParseQuery<ParseObject>("rides");
                pl.whereEqualTo("objectId",values.get(position));
                pl.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null && objects!=null){
                            for(ParseObject oc:objects){
                                oc.put("review",reviewtext.getText().toString());
                                oc.put("review_given",true);
                                oc.saveInBackground();
                            }
                        }
                        else if(e!=null){
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mobilelayout.setVisibility(View.VISIBLE);
                whatbtn.setClickable(true);
                reviewbtn.setClickable(false);
                reviewlayout.setVisibility(View.INVISIBLE);
                submitreview.setClickable(false);
                reviewtext.setClickable(false);

            }

        });

return rowview;
    }
}
