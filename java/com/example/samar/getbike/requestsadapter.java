package com.example.samar.getbike;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class requestsadapter extends ArrayAdapter<requests_disp> {
    private final Context context;
    private final ArrayList<requests_disp> values;

    public requestsadapter(Context context, ArrayList<requests_disp> values) {
        super(context,R.layout.request_rowlayout,values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Boolean bought;
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview=inflater.inflate(R.layout.request_rowlayout,parent,false);
        final ImageView custpic=rowview.findViewById(R.id.custpic);
        TextView custname=rowview.findViewById(R.id.cust_name);
        final Button acceptbtn=rowview.findViewById(R.id.accept_reqbtn),rejectbtn=rowview.findViewById(R.id.rejectbtn);
        ParseQuery<ParseObject> pic=new ParseQuery<ParseObject>("profilepic");
        custname.setText(values.get(position).getCustomer_name());
        //custid.setText(values.get(position).getCustomer_id());
        pic.whereEqualTo("username",values.get(position).getCustomer_name());
        pic.addDescendingOrder("createdAt");
        pic.setLimit(1);
        pic.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    ParseFile file= objects.get(0).getParseFile("profilepic");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if(e==null && data.length>0){
                                Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                custpic.setImageBitmap(bitmap);
                            }
                            else{
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if(e!=null){
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptbtn.setClickable(false);
                acceptbtn.setVisibility(View.INVISIBLE);
                rejectbtn.setVisibility(View.INVISIBLE);
                ParseQuery<ParseObject> sr=new ParseQuery<ParseObject>("availbikes");
                sr.whereEqualTo("cycid",values.get(position).getBikeid());
                sr.setLimit(1);
                sr.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(objects!=null && e==null){
                            ParseObject rd=objects.get(0);

                            //Toast.makeText(context,bought+"",Toast.LENGTH_SHORT).show();
                            rd.put("onride",true);
                           rd.saveInBackground();
                        }
                        else if(e!=null){
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final ParseObject rides = new ParseObject("rides");
                rides.put("bikeid",values.get(position).getBikeid());
                rides.put("ownerid",values.get(position).getOwnerid());
                rides.put("userid",values.get(position).getCustomer_name());
                rides.put("ride_confirm",false);
                rides.put("rate_given",false);
                rides.put("ride_finish",false);

                rides.put("review_given",false);
                ParseQuery<ParseObject> qrt=new ParseQuery<ParseObject>("availbikes");
                qrt.whereEqualTo("cycid",values.get(position).getBikeid());
                qrt.whereEqualTo("ownerid",values.get(position).getOwnerid());
                qrt.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null && objects!=null){
                            for(ParseObject pk:objects){
                                rides.put("fromdate",pk.get("fromdate").toString());
                                rides.put("fromtime",pk.get("fromtime").toString());
                                rides.put("todate",pk.get("todate").toString());
                                rides.put("totime",pk.get("totime").toString());
                                rides.put("price",pk.get("price").toString());
                                rides.put("bought",pk.get("sell"));
                                rides.saveInBackground();
                            }
                        }
                        else if(objects==null || e!=null){
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ParseQuery<ParseObject> bike=new ParseQuery<ParseObject>("bikes");
                bike.whereEqualTo("cycid",values.get(position).getBikeid());
                bike.whereEqualTo("ownerid",values.get(position).getOwnerid());
                bike.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for(ParseObject pot:objects){
                            rides.put("oprice",pot.get("originalprice").toString());
                            rides.put("dop",pot.get("dopday").toString()+"/"+pot.get("dopmonth").toString()+"/"+pot.get("dopyear").toString());
                         rides.put("cycpic",pot.getParseFile("cycpic"));
                         rides.put("bikename",pot.get("name").toString());
                         rides.saveInBackground(new SaveCallback() {
                             @Override
                             public void done(ParseException e) {
                                 if(e!=null){
                                     Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                 }
                             }
                         });
                        }
                    }
                });
                rides.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                           Toast.makeText(context,"Request accepted",Toast.LENGTH_SHORT).show();
                        }
                        else if(e!=null){
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

ParseQuery<ParseObject> qt=new ParseQuery<ParseObject>("requests");
qt.whereEqualTo("bikeid",values.get(position).getBikeid());
qt.whereEqualTo("ownerid",values.get(position).getOwnerid());
qt.findInBackground(new FindCallback<ParseObject>() {
    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(objects!=null && e==null){
            for(ParseObject d:objects){
                d.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                         if(e!=null) {
                             Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                         else{
                             Intent kint=new Intent(context,lended_customer.class);
                             kint.putExtra("bikeid",values.get(position).getBikeid());
                             kint.putExtra("userid",values.get(position).getCustomer_name());
                             startActivity(context,kint,Bundle.EMPTY);
                             ((Activity)context).finish();
                         }
                    }
                });
            }
            notifyDataSetChanged();
        }
        else if(e!=null){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
});
//values.remove(position);

            }
        });
        rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(context,values.get(position).getCustomer_name(),Toast.LENGTH_SHORT).show();
                ParseQuery<ParseObject> findreq=new ParseQuery<ParseObject>("requests");
                findreq.whereEqualTo("bikeid",values.get(position).getBikeid());
                findreq.whereEqualTo("ownerid",values.get(position).getOwnerid());
                findreq.whereEqualTo("customerid",values.get(position).getCustomer_name());
                findreq.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null && objects!=null){
                           objects.get(0).deleteInBackground(new DeleteCallback() {
                               @Override
                               public void done(ParseException e) {
                                   if(e!=null){
                                       Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                        }
                        else if(e!=null || objects==null){
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                values.remove(position);
                notifyDataSetChanged();
            }
        });
    return rowview;
    }
}
