package com.example.samar.getbike;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Mybikes extends AppCompatActivity {
    ParseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybikes);
        user=ParseUser.getCurrentUser();

        final ListView mycycles=findViewById(R.id.mycyclelist);
       final ArrayList<mycycledetails> list=new ArrayList<mycycledetails>();
        final CustomAdapter adapter=new CustomAdapter(Mybikes.this,list);

        ParseQuery<ParseObject> query2=new ParseQuery<ParseObject>("bikes");
        query2.whereEqualTo("ownerid",user.getUsername());
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects !=null) {
                    for (ParseObject obj3:objects) {
                      mycycledetails cycle=new mycycledetails(obj3.get("name").toString(),obj3.get("cycid").toString());
                      list.add(cycle);
                    }
mycycles.setAdapter(adapter);
                    mycycles.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            ParseQuery<ParseObject> fi=new ParseQuery<ParseObject>("availbikes");
                            fi.whereEqualTo("cycid",list.get(position).getCycleid());
                            fi.whereEqualTo("ownerid",user.getUsername());
                            fi.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if(e==null && objects!=null){
                                        for(final ParseObject toy:objects){
                                            if(toy.get("onride").equals(true)){
                                                ParseQuery<ParseObject> qw=new ParseQuery<ParseObject>("rides");
                                                qw.whereEqualTo("bikeid",list.get(position).getCycleid());
                                                qw.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> objects, ParseException e) {
                                                        for(ParseObject pt:objects){
                                                            Intent iny=new Intent(Mybikes.this,lended_customer.class);
                                                            iny.putExtra("bikeid",pt.get("bikeid").toString());
                                                            iny.putExtra("userid",pt.get("userid").toString());
                                                            startActivity(iny);
                                                        }
                                                    }
                                                });

                                            }
                                            else{
                                                AlertDialog.Builder builder=new AlertDialog.Builder(Mybikes.this);
                                                View aview=getLayoutInflater().inflate(R.layout.viewtimings,null,false);
                                                Button delavailbtn=aview.findViewById(R.id.delavail);
                                                RelativeLayout datelayout=aview.findViewById(R.id.dateslayout);
                                                TextView fromdate=aview.findViewById(R.id.fromdate5),fromtime=aview.findViewById(R.id.fromtime5),todate=aview.findViewById(R.id.todate5),totime=aview.findViewById(R.id.totime5);
                                                TextView lendtype=aview.findViewById(R.id.lendtype),price=aview.findViewById(R.id.pricedisp5);

                                                if(toy.get("rent").equals(true)){
                                                    datelayout.setVisibility(View.VISIBLE);
                                                    fromdate.setText(toy.get("fromdate").toString());
                                                    fromtime.setText(toy.get("fromtime").toString());
                                                    todate.setText(toy.get("todate").toString());
                                                    totime.setText(toy.get("totime").toString());
                                                    lendtype.setText("Lend");
                                                }
                                                else{
                                                    lendtype.setText("Sell");
                                                    datelayout.setVisibility(View.INVISIBLE);
                                                }
                                                price.setText(toy.get("price").toString());
                                                AlertDialog dialog=builder.create();
                                                dialog.setView(aview);
                                                dialog.show();
                                                delavailbtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ParseQuery<ParseObject> qg=new ParseQuery<ParseObject>("requests");
                                                        qg.whereEqualTo("bikeid",toy.get("cycid").toString());
                                                        qg.whereEqualTo("ownerid",toy.get("ownerid").toString());
                                                        qg.findInBackground(new FindCallback<ParseObject>() {
                                                            @Override
                                                            public void done(List<ParseObject> objects, ParseException e) {
                                                                if (e==null){
                                                                    for(ParseObject om:objects){
                                                                        om.deleteInBackground(new DeleteCallback() {
                                                                            @Override
                                                                            public void done(ParseException e) {
                                                                                if(e!=null){
                                                                                    Toast.makeText(Mybikes.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                                else if(e!=null){
                                                                    Toast.makeText(Mybikes.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        toy.deleteInBackground(new DeleteCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                if(e!=null ){
                                                                    Toast.makeText(Mybikes.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    Intent jint=new Intent(Mybikes.this,MainActivity.class);
                                                    startActivity(jint);
                                                    finishAffinity();
                                                    }
                                                });
                                                }
                                        }
                                    }
                                    else if(e!=null){
                                        Toast.makeText(Mybikes.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            try {
                                if(fi.count()==0){
                                    Intent intent=new Intent(Mybikes.this,LendActivity.class);
                                    intent.putExtra("cycid",list.get(position).getCycleid());
                                    startActivity(intent);
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }



                        }
                    });
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(Mybikes.this,"You don't have any bikes",Toast.LENGTH_SHORT).show();
                }
            }

        });

        }
}
