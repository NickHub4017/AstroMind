package nrv.com.ncastronomy.astromind;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


/**
 * Created by NRV on 7/31/2015.
 */
public class HomeActivity extends ActionBarActivity {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] dataset;
    private ActionBarDrawerToggle drawerToggle;
    private CustAdapter custAdapter;
    private  ListView postlists,EventList;
    NetResponse netResponse;
    TextView tst;
    Button b;

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(netResponse);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        IntentFilter movementFilter;
        movementFilter = new IntentFilter("Get.Store.Intent");
        netResponse = new NetResponse();
        registerReceiver(netResponse, movementFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(netResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, NetLink.class));
        IntentFilter movementFilter;
        movementFilter = new IntentFilter("Get.Store.Intent");
        netResponse = new NetResponse();
        registerReceiver(netResponse, movementFilter);

        setContentView(R.layout.homepage);




        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
//        dataset=getResources().getStringArray(R.array.testar);
        custAdapter=new CustAdapter(this);
        listView=(ListView)findViewById(R.id.drawList);
        listView.setAdapter(custAdapter);

        EventList=(ListView)findViewById(R.id.eventlistviews);
        EventList.setAdapter(custAdapter);


        postlists=(ListView)findViewById(R.id.postlistviews);
//        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataset));
        postlists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SingleRow t = (SingleRow) parent.getAdapter().getItem(position);
                if (t.link != null) {
                    if (!t.link.split(".com")[0].contains("face")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(t.link)));
                    } else {

                    }
                }
                return true;
            }
        });

        postlists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleRow t = (SingleRow) parent.getAdapter().getItem(position);
                TextView desctv=(TextView)view.findViewById(R.id.post_desc_textview);


            }
        });


        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Toast.makeText(getApplicationContext(),"Open",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //Toast.makeText(getApplicationContext(),"Closed",Toast.LENGTH_LONG).show();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplication(), position + " Selected ", Toast.LENGTH_LONG).show();
                listView.setItemChecked(position, true);
                drawerLayout.closeDrawers();
                if (position == 1) {//fetch
                    EventList.setVisibility(View.INVISIBLE);
                    Intent in = new Intent("Req.Store.Intent");
                    in.putExtra("request", "allpostids");
                    sendBroadcast(in);
                } else if (position == 2) {
                    postlists.setVisibility(View.INVISIBLE);
                    Intent in = new Intent("Req.Store.Intent");
                    in.putExtra("request", "allevents");
                    sendBroadcast(in);


                } else if (position == 3) {

                }

            }
        });



    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    public class NetResponse extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("netres","recive "+intent.getStringExtra("resfor"));
            if(intent.getStringExtra("resfor").equals("postids")){
                //
                // Toast.makeText(getApplication(),intent.getStringExtra("result"),Toast.LENGTH_LONG).show();
                String result="";

                    result=intent.getStringExtra("result");
                Log.d("service_transmit", ""+result.length());

                try {
                    JSONObject res_obj=new JSONObject(result);
                    CustomListAdapter adapter=new CustomListAdapter(HomeActivity.this, res_obj,HomeActivity.this);
                    postlists.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    postlists.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if (intent.getStringExtra("resfor").equals("events")){

                EventList.setVisibility(View.VISIBLE);
                String result=intent.getStringExtra("result");
                try {
                    JSONObject res_obj=new JSONObject(result);
                    EventAdapter evad=new EventAdapter(res_obj.getJSONArray("data"),HomeActivity.this);
                    EventList.setAdapter(evad);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    }










