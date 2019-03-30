package com.example.samar.getbike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class bikeshistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikeshistory);

        final ListView listView=findViewById(R.id.bikeshistorylist);
        final ArrayList<String> histarray=new ArrayList<String>();
        final bikeshistoryadapter adapter=new bikeshistoryadapter(bikeshistoryActivity.this,histarray);
        ParseQuery<ParseObject> rt=new ParseQuery<ParseObject>("rides");
        rt.whereEqualTo("ownerid",ParseUser.getCurrentUser().getUsername());
        rt.whereEqualTo("ride_finish",true);
        rt.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    for(ParseObject og:objects){
                        histarray.add(og.getObjectId());
                        //Toast.makeText(bikeshistoryActivity.this,histarray.get(0),Toast.LENGTH_SHORT).show();
                    }
                    listView.setAdapter(adapter);
                  adapter.notifyDataSetChanged();
                }
                else if(e!=null){
                    Toast.makeText(bikeshistoryActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
