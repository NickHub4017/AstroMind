package nrv.com.ncastronomy.astromind;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

/**
 * Created by NRV on 8/2/2015.
 */
class CustAdapter extends BaseAdapter {
    String menuItems[];
    View row=null;
    int images[]={R.drawable.prof,R.drawable.posts,R.drawable.events,R.drawable.compass,R.drawable.bookmark,R.drawable.logout};
    NetworkImageView profileimage;
    TextView profname;
    Context context;
    ImageLoader imageLoader = AstroApp.getInstance().getImageLoader();
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
    public View getView(int position, final View convertView, ViewGroup parent) {
        if (imageLoader == null)
            imageLoader = AstroApp.getInstance().getImageLoader();



        if(position!=0) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.custom_row, parent, false);



            }
            else {
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
            profileimage = (NetworkImageView) row.findViewById(R.id.thumbnailprof);
            profname=(TextView)row.findViewById(R.id.profname);
            Bundle b=new Bundle();
            b.putString("fields","picture.width(160),name");
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me",
                    b,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {

                            try {

                                String lnk=response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                                String myname=response.getJSONObject().getString("name");
                                profname.setText(myname);
                                if(profileimage==null){
                                    Log.d("hhehe", "null");
                                }
                                Log.d("hhehe", lnk);
                                profileimage.setImageUrl(lnk, imageLoader);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

            /* handle the result */
                        }
                    }
            ).executeAsync();

/*
            //TextView tv = (TextView) row.findViewById(R.id.textView);
            ImageView imtv = (ImageView) row.findViewById(R.id.profilepic);
            //tv.setText(menuItems[position]);
            imtv.setImageResource(images[position]);
            imtv.setMaxWidth(200);
*/
            return row;
        }
    }

}
