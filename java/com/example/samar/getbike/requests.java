package com.example.samar.getbike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class requests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Intent intent=getIntent();
        final String bikeid=intent.getStringExtra("bikeid");
        final String ownerid=intent.getStringExtra("ownerid");
       final ArrayList<requests_disp> arrayList=new ArrayList<requests_disp>();
        final ListView listView=findViewById(R.id.requestlist);
        final requestsadapter reqadapter=new requestsadapter(requests.this,arrayList);
        ParseQuery<ParseObject> req=new ParseQuery<ParseObject>("requests");
        req.whereEqualTo("bikeid",bikeid);
        req.whereEqualTo("ownerid",ownerid);
        req.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    for(ParseObject ok:objects){
                        requests_disp reqclass=new requests_disp(ok.get("customerid").toString(),bikeid,ownerid);
                        arrayList.add(reqclass);
                    }
                    listView.setAdapter(reqadapter);
                    reqadapter.notifyDataSetChanged();
                }
                else if(e!=null || objects==null){
                    Toast.makeText(requests.this,"You don't have any requests",Toast.LENGTH_SHORT).show();
                    Toast.makeText(requests.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
