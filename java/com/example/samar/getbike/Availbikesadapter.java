package com.example.samar.getbike;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class Availbikesadapter extends ArrayAdapter<Availbikes> {
private final Context context;
private final ArrayList<Availbikes> values;


    public Availbikesadapter(Context context, ArrayList<Availbikes> values) {
         super(context,R.layout.rowlayout,values);
         this.context = context;
         this.values = values;

     }
     static ViewGroup holder;{
        TextView bikename;
        TextView bikeid;
        ImageView bikeimg;
        TextView fdate,tdate,ftime,ttime;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//Toast.makeText(context,"get view",Toast.LENGTH_SHORT).show();
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View rowview=inflater.inflate(R.layout.rowlayout,parent,false);

        //Toast.makeText(context,values.get(position).getName(),Toast.LENGTH_SHORT).show();
        TextView bikename=rowview.findViewById(R.id.bikename);
        final TextView bikeid=rowview.findViewById(R.id.bikeid);
        final ImageView bikeimg=rowview.findViewById(R.id.bikeimg);
      final TextView fdate=rowview.findViewById(R.id.fdatedisp);
        final TextView tdate=rowview.findViewById(R.id.tdatedisp);
        final TextView ftime=rowview.findViewById(R.id.ftimedisp);
        final TextView ttime=rowview.findViewById(R.id.ttimedisp);
        final RelativeLayout fromdisp=rowview.findViewById(R.id.fromdisplayout),todisp=rowview.findViewById(R.id.todisplayout);
        bikename.setText(values.get(position).getName());
        bikeid.setText(values.get(position).getId());
        ParseQuery<ParseObject> qy=new ParseQuery<ParseObject>("availbikes");
        qy.whereEqualTo("cycid",bikeid.getText().toString());
        qy.whereEqualTo("bikename",bikename.getText().toString());
        qy.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null) {
                    for (ParseObject obj : objects) {
if(obj.get("rent").equals(true) &&  obj.get("sell").equals(false)) {
    fdate.setText(obj.get("fromdate").toString());
    tdate.setText(obj.get("todate").toString());
    ftime.setText(obj.get("fromtime").toString());
    ttime.setText(obj.get("totime").toString());
}
else{
    fromdisp.setVisibility(View.INVISIBLE);
    todisp.setVisibility(View.INVISIBLE);
}
                      //  Toast.makeText(context,fdate.getText().toString(),Toast.LENGTH_SHORT).show();*/
                        ParseQuery<ParseObject> q2=new ParseQuery<ParseObject>("bikes");
                        q2.whereEqualTo("cycid",bikeid.getText().toString());
                        q2.setLimit(1);
                        q2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null && objects!=null){

                                    ParseFile file=(ParseFile)objects.get(0).get("cycpic");
                                    file.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if(e==null && data!=null){
                                              Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                              bikeimg.setImageBitmap(bitmap);
                                            }
                                            else{
                                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else {
                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rowview;
    }
}
