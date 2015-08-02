package nrv.com.ncastronomy.astromind;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
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
    private  ListView postlists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, NetLink.class));
        IntentFilter movementFilter;
        movementFilter = new IntentFilter("Get.Store.Intent");
        NetResponse netResponse = new NetResponse();
        registerReceiver(netResponse, movementFilter);

        setContentView(R.layout.homepage);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
//        dataset=getResources().getStringArray(R.array.testar);
        custAdapter=new CustAdapter(this);
        listView=(ListView)findViewById(R.id.drawList);
        listView.setAdapter(custAdapter);
        postlists=(ListView)findViewById(R.id.postlistviews);
//        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataset));

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
postlists.setOnScrollListener(new AbsListView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int curfst=postlists.getFirstVisiblePosition();
        int lstfst= postlists.getLastVisiblePosition();
        for (int i=curfst;i<lstfst;i++){
            SingleRow tmp=(SingleRow)postlists.getAdapter().getItem(i);

            Intent in=new Intent("Req.Store.Intent");
            in.putExtra("request","getimages");
            in.putExtra("imglink",tmp.imageid);
            in.putExtra("id",tmp.id);
            sendBroadcast(in);

        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
});

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplication(),position+" Selected ",Toast.LENGTH_LONG).show();
                listView.setItemChecked(position, true);
                drawerLayout.closeDrawers();
                if(position==1){//fetch
                    Intent in=new Intent("Req.Store.Intent");
                    in.putExtra("request", "allpostids");
                    sendBroadcast(in);
                }
                else if(position==2){
                    postlists.setVisibility(View.INVISIBLE);
                    // listView.setAdapter(null);

                }
                else if(position==3){

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
                    postlists.setAdapter(new PostViewAdapter(HomeActivity.this, res_obj));
                    postlists.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if (intent.getStringExtra("resfor").equals("imagesdata")){

            }
        }
    }
    }

class CustAdapter extends  BaseAdapter{
String menuItems[];
    int images[]={R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon};

    Context context;
    public CustAdapter(Context context_inp){
        context=context_inp;
        menuItems=context.getResources().getStringArray(R.array.testar);
    }
    @Override
    public int getCount() {
        return menuItems.length;
    }

    @Override
    public Object getItem(int position) {
        return menuItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=null;
        if(position!=0) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.custom_row, parent, false);
            } else {
                row = convertView;
            }
            TextView tv = (TextView) row.findViewById(R.id.textView);
            ImageView imtv = (ImageView) row.findViewById(R.id.imageView2);
            tv.setText(menuItems[position]);
            imtv.setImageResource(images[position]);

            return row;
        }
        else{
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_first, parent, false);
            } else {
                row = convertView;
            }
            //TextView tv = (TextView) row.findViewById(R.id.textView);
            ImageView imtv = (ImageView) row.findViewById(R.id.profilepic);
            //tv.setText(menuItems[position]);
            imtv.setImageResource(images[position]);
            imtv.setMaxWidth(200);

            return row;
        }
    }
}
class SingleRow{
    int typeofpost;//0-message 1-story
    String id;
    String title;
    int likes;
    String description;
    String imageid;
    int postimgid;
    String pubdate;

    public SingleRow(String title, String description,String img,int iconimg,String pid,int type,String dat_pub) {

        this.title = title;
        this.description = description;
        this.imageid=img;
        this.postimgid=iconimg;
        this.id=pid;
        this.typeofpost=type;
        this.pubdate=dat_pub;
    }
}

class PostViewAdapter extends BaseAdapter{
    String[] titles;
Context cont;
    ArrayList<SingleRow> datalist;
PostViewAdapter(Context con,JSONObject jsonObject){
    cont=con;
    datalist=new ArrayList<SingleRow>();
    Resources res=con.getResources();
   // int images[]={R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon};
    int images_icon[]={R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon};
    if(jsonObject!=null){
        try {
            JSONArray data=jsonObject.getJSONArray("data");
            for (int i=0;i<data.length();i++){
                JSONObject post=data.getJSONObject(i);
                String tit;
                String desc;
                String pubdt;
                int tp;
                if(post.has("message")){
                    tit="message";
                    desc=post.getString("message");
                    pubdt=post.getString("created_time");
                    tp=0;
                }
                else if(post.has("story")){//
                    tit="story";
                    //Log.d("post no",""+i);
                    desc=post.getString("story");
                    tp=1;
                    pubdt=post.getString("created_time");
                }

                else{
                    Log.d("all keys",post.keys().toString());

                    tit="ganna ba";
                    desc="ganna ba oi  "+ i;
                    tp=i;
                    pubdt="danne na";
                }
                SingleRow s;
                if(post.has("picture")){
                     s=new SingleRow(tit,desc,post.getString("picture"),images_icon[i],post.getString("id"),tp,pubdt);
                }
                else{
                     s=new SingleRow(tit,desc,null,images_icon[i],post.getString("id"),tp,pubdt);
                }

                datalist.add(s);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }/*
    String[] titles=con.getResources().getStringArray(R.array.test2);
    String[] desc=res.getStringArray(R.array.test2);

    for (int i=0;i<5;i++){
        SingleRow s=new SingleRow(titles[i],desc[i],images[i],images_icon[i]);
        datalist.add(s);
    }*/

}
    String[] title;
    String[] desc;
    int[] imgs;
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.post_row, parent, false);
        ImageView imgicon= (ImageView) row.findViewById(R.id.posticon_image);
        ImageView imgphoto= (ImageView) row.findViewById(R.id.post_image);
        TextView titletext= (TextView) row.findViewById(R.id.post_desc_textview);
        TextView nolikes= (TextView) row.findViewById(R.id.post_no_likes);
        TextView post_pubdate= (TextView) row.findViewById(R.id.post_pubdate);
        SingleRow temp=datalist.get(position);
        imgicon.setImageResource(temp.postimgid);
        //imgphoto.setImageResource(temp.imageid);
        titletext.setText(temp.description);
        nolikes.setText(temp.title);
        post_pubdate.setText(temp.pubdate);
        return row;
    }


}


