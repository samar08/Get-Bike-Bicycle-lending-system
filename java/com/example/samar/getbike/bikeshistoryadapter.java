package com.example.samar.getbike;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class bikeshistoryadapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    public bikeshistoryadapter(Context context,ArrayList<String> values){
        super(context,R.layout.myrides_rowlayout,values);
        this.context=context;
        this.values=values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview=inflater.inflate(R.layout.bikeshist_rowlayout,parent,false);
        final TextView fromdate,fromtime,todate,totime,username,userid,bikename,bikeid,mobnum,price;
        final ImageView bikepic=rowview.findViewById(R.id.bikepic),ownerpic=rowview.findViewById(R.id.ownerpic);
        final ImageButton whatbtn=rowview.findViewById(R.id.whatbtn);
        final RelativeLayout date_layout=rowview.findViewById(R.id.date_layout);
        mobnum=rowview.findViewById(R.id.mobnum);
        fromdate=rowview.findViewById(R.id.fromdateview);fromtime=rowview.findViewById(R.id.fromtimeview);
        todate=rowview.findViewById(R.id.todateview);totime=rowview.findViewById(R.id.totimeview);
        username=rowview.findViewById(R.id.ownername);userid=rowview.findViewById(R.id.ownerid);
        bikename=rowview.findViewById(R.id.bikename2);bikeid=rowview.findViewById(R.id.bikeid2);
        price=rowview.findViewById(R.id.price2);
        ParseQuery<ParseObject> qrt=new ParseQuery<ParseObject>("rides");
        qrt.whereEqualTo("objectId",values.get(position));
        qrt.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for(ParseObject ride:objects){
                        username.setText(ride.get("userid").toString());
                        bikeid.setText(ride.get("bikeid").toString());
                        if(ride.get("bought").equals(false)) {
                            fromdate.setText(ride.get("fromdate").toString());
                            fromtime.setText(ride.get("fromtime").toString());
                            todate.setText(ride.get("todate").toString());
                            totime.setText(ride.get("totime").toString());
                        }
                        else{
                            date_layout.setVisibility(View.INVISIBLE);
                        }
                        bikename.setText(ride.get("bikename").toString());
                        price.setText(ride.get("price").toString());

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
                        userp.whereEqualTo("username",ride.get("userid").toString());
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
                        qr.whereEqualTo("username",ride.get("userid").toString());
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




                    }
                }
                else if(e!=null){
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
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
return rowview;
    }
}
