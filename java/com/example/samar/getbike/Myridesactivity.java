package com.example.samar.getbike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Myridesactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myridesactivity);
        final ListView list=findViewById(R.id.myrideslist);
        final ArrayList<String> objectid=new ArrayList<String>();
        final Myridesadapter myridesadapter=new Myridesadapter(Myridesactivity.this,objectid);
        ParseQuery<ParseObject> oq=new ParseQuery<ParseObject>("rides");
        oq.whereEqualTo("userid",ParseUser.getCurrentUser().getUsername());
        oq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    for(ParseObject ride:objects){

                        objectid.add(ride.getObjectId());
                    }
                list.setAdapter(myridesadapter);
                    myridesadapter.notifyDataSetChanged();
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(Myridesactivity.this,"hrlo",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(e!=null){
                    Toast.makeText(Myridesactivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
