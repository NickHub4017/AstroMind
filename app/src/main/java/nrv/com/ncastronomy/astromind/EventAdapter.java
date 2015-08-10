package nrv.com.ncastronomy.astromind;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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

/**
 * Created by NRV on 8/9/2015.
 */
public class EventAdapter extends BaseAdapter {

    JSONArray eventarray;
    private Activity activity;
    private LayoutInflater inflater;

    ImageLoader imageLoader = AstroApp.getInstance().getImageLoader();
    public EventAdapter(JSONArray eventarray_inp,Activity act_inp) {
        this.activity = act_inp;
        this.eventarray = eventarray_inp;
    }

    @Override
    public int getCount() {
        return eventarray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return eventarray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
            convertView = inflater.inflate(R.layout.events_row, null);



        if (imageLoader == null) {
            imageLoader = AstroApp.getInstance().getImageLoader();
        }
        NetworkImageView eventthubnail = (NetworkImageView) convertView.findViewById(R.id.eventphoto);
        TextView texteventname=(TextView)convertView.findViewById(R.id.texteventname);
        TextView texttimeanddate=(TextView)convertView.findViewById(R.id.texttimeanddate);
        TextView textvenue=(TextView)convertView.findViewById(R.id.textvenue);
        TextView texteventdescrption=(TextView)convertView.findViewById(R.id.texteventdescrption);

        try {
            JSONObject aevent= (JSONObject) eventarray.get(position);
            Log.d("Data", aevent.toString());
            try {
                eventthubnail.setImageUrl(aevent.getJSONObject("cover").getString("source"), imageLoader);
            }
            catch (JSONException e){
                eventthubnail.setDefaultImageResId(R.drawable.mainimage);
            }
            texteventname.setText(aevent.getString("name"));
            texttimeanddate.setText(aevent.getString("start_time"));
            texteventdescrption.setText(aevent.getString("description"));
            textvenue.setText(aevent.getJSONObject("place").getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
