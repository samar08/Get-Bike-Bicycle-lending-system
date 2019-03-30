package com.example.samar.getbike;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class CustomAdapter extends ArrayAdapter<mycycledetails> {

  private final Context context;
  private final ArrayList<mycycledetails> values;
  public CustomAdapter(Context context,ArrayList<mycycledetails> values){
      super(context,R.layout.cyclesdisplay,values);
      this.context=context;
      this.values=values;

  }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
final int redcolor=0xffff0000;
final int yellowcolor=0xffCCCC00;
final int greencolor=0xff008000;
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview=inflater.inflate(R.layout.cyclesdisplay,parent,false);
        TextView name=rowview.findViewById(R.id.cycnamedisp);
        final TextView id=rowview.findViewById(R.id.cyciddisp);
        Button deletebtn,viewreqbtn;
        final TextView status=rowview.findViewById(R.id.status);
        deletebtn=rowview.findViewById(R.id.deletelistbtn);
        viewreqbtn=rowview.findViewById(R.id.viewreqbtn);
        final ImageView cycpic=rowview.findViewById(R.id.mycycdisp);
        name.setText(values.get(position).getName());
        id.setText(values.get(position).getCycleid());
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("bikes");
        query.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("cycid",id.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    for(ParseObject obj:objects){
                        ParseFile file=(ParseFile)obj.get("cycpic");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null && data.length>0){
                                    Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                    cycpic.setImageBitmap(bitmap);
                                }
                                else{
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }
                else{
                  //  Toast.makeText(context,"You don't have any bikes/c",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ParseQuery<ParseObject> chk=new ParseQuery<ParseObject>("availbikes");
        chk.whereEqualTo("cycid",values.get(position).getCycleid());
        chk.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
        chk.setLimit(1);
        chk.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects!=null && e==null){
                    for(ParseObject pl:objects){
                        if((pl.get("onride")).equals(false)){
                            status.setTextColor(yellowcolor);
                            status.setText("Available");
                        }
                        else if((pl.get("onride")).equals(true)){
                            status.setTextColor(greencolor);
                            status.setText("On ride");
                        }
                    }

                }
                else if(objects.get(0)==null){
                    status.setText("Unavailable");
                    status.setTextColor(redcolor);
                    //
                }
                else if(e!=null){
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                if(status.getText().toString()==""){
                    status.setText("Unavailable");
                    status.setTextColor(redcolor);
                }
            }
        });


        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.getText().toString().equals("On ride")) {
                    Toast.makeText(context, "You cannot delete this bike now!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(status.getText().toString().equals("Available")) {
                        ParseQuery<ParseObject> ut = new ParseQuery<ParseObject>("availbikes");
                        ut.whereEqualTo("cycid", id.getText().toString());
                        ut.whereEqualTo("ownerid", ParseUser.getCurrentUser().getUsername());
                        ut.setLimit(1);
                        ut.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                for (ParseObject op : objects) {
                                    op.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    ParseQuery<ParseObject> pm=new ParseQuery<ParseObject>("requests");
                    pm.whereEqualTo("bikeid",id.getText().toString());
                    pm.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
                        try {
                            if(pm.count()>0) {
                                pm.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null && objects != null) {
                                            for (ParseObject pl : objects) {
                                                pl.deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e != null) {
                                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                values.remove(position);
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("bikes");
                query.whereEqualTo("ownerid", ParseUser.getCurrentUser().getUsername());
                query.whereEqualTo("cycid", id.getText().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for (ParseObject obj6 : objects) {
                            obj6.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Unable to delete", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        notifyDataSetChanged();

                    }
                });

            }
            }
        });
viewreqbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
Intent intent=new Intent(context,requests.class);
intent.putExtra("bikeid",id.getText().toString());
intent.putExtra("ownerid",ParseUser.getCurrentUser().getUsername());
startActivity(context,intent,Bundle.EMPTY);
    }
});


        return rowview;
    }
}
