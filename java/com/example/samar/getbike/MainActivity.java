package com.example.samar.getbike;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ActivityChooserView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

RadioGroup radioGroup;RadioButton radioButton;
ImageButton editdate;
    DatePickerDialog.OnDateSetListener setListener1 ;
    DatePickerDialog datePickerDialog;
    TextView dateview;
    int fyear,fmonth,fday;
final ArrayList<String> userid=new ArrayList<>();
    Button applybtn;
    ProgressBar spinner;
    int radiobtnid;
ParseUser user;
public void checkbtn(View view){
     radiobtnid=radioGroup.getCheckedRadioButtonId();
    radioButton=findViewById(radiobtnid);
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Calendar cal=Calendar.getInstance();
        user=ParseUser.getCurrentUser();

radioGroup=findViewById(R.id.radio);
dateview=findViewById(R.id.dateview);
editdate=findViewById(R.id.editdatebtn);
        radiobtnid=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radiobtnid);
        final ArrayList<Availbikes> list=new ArrayList<Availbikes>();
        final ListView avaibikes=findViewById(R.id.availbikeslist);
        final Availbikesadapter availbikesadapter=new Availbikesadapter(MainActivity.this,list);
applybtn=findViewById(R.id.applybtn);
applybtn.setVisibility(View.VISIBLE);
        fyear=cal.get(Calendar.YEAR);fmonth=cal.get(Calendar.MONTH);fday=cal.get(Calendar.DAY_OF_MONTH);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        spinner=(ProgressBar)findViewById(R.id.progressbar);
        spinner.setVisibility(View.INVISIBLE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        TextView usernameheader=headerview.findViewById(R.id.usernameheader);
        final ImageView image_head=headerview.findViewById(R.id.imageView);
        try {
            usernameheader.setText(ParseUser.getCurrentUser().getUsername());
        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        ParseQuery<ParseObject> pic=new ParseQuery<ParseObject>("profilepic");
        pic.whereEqualTo("username",user.getUsername());
        pic.addDescendingOrder("createdAt");
        pic.setLimit(1);
        pic.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects!=null && e==null){
                    for(ParseObject pico:objects){
                        ParseFile pic=pico.getParseFile("profilepic");
                        pic.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data!=null && e==null){
                                    Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                    image_head.setImageBitmap(bitmap);
                                }
                                else if(e==null){
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else if(e!=null){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog=new DatePickerDialog(MainActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener1,fyear,fmonth,fday);
                datePickerDialog.show();
            }
        });

        setListener1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fyear=year;fmonth=month+1;fday=dayOfMonth;
                dateview.setText(fday+"/"+fmonth+"/"+fyear);

                }
        };

      applybtn.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
              spinner.setVisibility(View.VISIBLE);
              applybtn.setVisibility(View.INVISIBLE);
              list.clear();
              ParseQuery<ParseUser> que=ParseUser.getQuery();
              //ParseQuery<ParseObject> que=new ParseQuery<ParseObject>("User");
              que.whereEqualTo("collegename",user.get("collegename").toString());

              que.findInBackground(new FindCallback<ParseUser>() {
                  @Override
                  public void done(List<ParseUser> objects, ParseException e) {
                      if(e==null && objects!=null){
                          for(ParseUser obj:objects) {
                              ParseQuery<ParseObject> que1=new ParseQuery<ParseObject>("availbikes");
                              try{
                                  que1.whereEqualTo("ownerid",obj.get("username").toString());
                                 // Toast.makeText(MainActivity.this,obj.get("username").toString(),Toast.LENGTH_SHORT).show();
                                  que1.whereEqualTo("onride",false);

                              }catch (Exception e2){
                                  Toast.makeText(MainActivity.this, e2.getMessage(), Toast.LENGTH_SHORT).show();
                              }


                              if(radiobtnid==R.id.rentoption){
                                  que1.whereEqualTo("rent",true);
                                  que1.whereEqualTo("sell",false);
                                  que1.whereEqualTo("fromdate",dateview.getText().toString());

                              }
                              else if(radiobtnid==R.id.buyoption){
                                  que1.whereEqualTo("rent",false);
                                  que1.whereEqualTo("sell",true);

                              }
                              que1.findInBackground(new FindCallback<ParseObject>() {
                                  @Override
                                  public void done(List<ParseObject> objects, ParseException e) {

                                      if(e==null && objects!=null){

                                          for(ParseObject object:objects){

                                              Availbikes bike=new Availbikes(object.get("bikename").toString(),object.get("cycid").toString());
                                              list.add(bike);

                                          }
                                          avaibikes.setAdapter(availbikesadapter);
                                          availbikesadapter.notifyDataSetChanged();
                                          spinner.setVisibility(View.INVISIBLE);
                                          applybtn.setVisibility(View.VISIBLE);
                                      }
                                      else{
                                          Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                      }



                                  }
                              });


                          }

                      }
                      else{
                          Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }
          });
 avaibikes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     @Override
     public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        // Toast.makeText(MainActivity.this,list.get(position).getName(),Toast.LENGTH_SHORT).show();
         ParseQuery<ParseObject> quy=new ParseQuery<ParseObject>("availbikes");
         quy.whereEqualTo("bikename",list.get(position).getName());
         quy.whereEqualTo("cycid",list.get(position).getId());
         quy.setLimit(1);
         quy.findInBackground(new FindCallback<ParseObject>() {
             @Override
             public void done(List<ParseObject> objects, ParseException e) {
                 for(ParseObject obt:objects){
                     if(obt.get("ownerid").equals(user.getUsername())){
                         Toast.makeText(MainActivity.this,"You cannot buy your own cycle",Toast.LENGTH_SHORT).show();
                     }
                     else{
                           Intent bikeint=new Intent(MainActivity.this,bikeowner.class);
                           bikeint.putExtra("bikeid",list.get(position).getId());
                     startActivity(bikeint);
                     }
                 }
             }
         });

     }
 });

}




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.logoutid){

                if(ParseUser.getCurrentUser()!=null){
                    Toast.makeText(this,"Logged out successfully",Toast.LENGTH_SHORT).show();
                    ParseUser.logOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(this,"log out clicked not null",Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            finish();
            }
            else if(id==R.id.putbikebtn){
            startActivity(new Intent(MainActivity.this,UploadBike.class));
            finish();
        }
        else if(id==R.id.mybikebtn){
            startActivity(new Intent(MainActivity.this,Mybikes.class));

        }
        else if(id==R.id.ridesbtn){
            startActivity(new Intent(MainActivity.this,Myridesactivity.class));
        }
        else if(id==R.id.bikehistory){
            startActivity(new Intent(MainActivity.this,bikeshistoryActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
