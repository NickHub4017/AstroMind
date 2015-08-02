package nrv.com.ncastronomy.astromind;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NRV on 8/2/2015.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;

    Context cont;
    ArrayList<SingleRow> datalist;
    ImageLoader imageLoader = AstroApp.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, JSONObject jsonObject,Context con) {
        this.activity = activity;
        cont=con;
        datalist=new ArrayList<SingleRow>();
        Resources res=con.getResources();
        int images_icon[]={R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.posticon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon};

        try {
            JSONArray data=jsonObject.getJSONArray("data");
            for (int i=0;i<data.length();i++){
                JSONObject post=data.getJSONObject(i);
                String tit;
                String desc;
                String pubdt;
                int tp;
                int likes;
                if (post.has("likes")){
                    likes=post.getJSONObject("likes").getJSONArray("data").length();
                }
                else{
                    likes=0;
                }
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
                    Log.d("all keys", post.keys().toString());

                    tit="ganna ba";
                    desc="ganna ba oi  "+ i;
                    tp=i;
                    pubdt="danne na";
                }
                SingleRow s;
                if(post.has("picture")){
                    if (post.has("link")) {
                        s = new SingleRow(tit, desc, post.getString("picture"), images_icon[i], post.getString("id"), tp, pubdt, likes, post.getString("link"));
                    }
                    else{
                        s = new SingleRow(tit, desc, post.getString("picture"), images_icon[i], post.getString("id"), tp, pubdt, likes, null);
                    }
                }
                else{

                    if (post.has("link")) {
                        s=new SingleRow(tit,desc,null,images_icon[i],post.getString("id"),tp,pubdt,likes,post.getString("link"));
                    }
                    else{
                        s=new SingleRow(tit,desc,null,images_icon[i],post.getString("id"),tp,pubdt,likes,null);
                    }
                }

                datalist.add(s);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int location) {
        return datalist.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.post_row, null);

        if (imageLoader == null)
            imageLoader = AstroApp.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
       // TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.post_desc_textview);
        TextView likes = (TextView) convertView.findViewById(R.id.post_no_likes);
        TextView pubdate = (TextView) convertView.findViewById(R.id.post_pubdate);

        // getting movie data for the row
        SingleRow m = datalist.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.imageid, imageLoader);

        // title
       // title.setText(m.title);

        // rating
        likes.setText(String.valueOf(m.likes));

        // genre

        desc.setText(m.description);

        // release year
        pubdate.setText(m.pubdate);

        return convertView;
    }

}
