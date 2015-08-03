package nrv.com.ncastronomy.astromind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by NRV on 8/2/2015.
 */
class CustAdapter extends BaseAdapter {
    String menuItems[];
    int images[]={R.drawable.prof,R.drawable.posts,R.drawable.events,R.drawable.compass,R.drawable.bookmark,R.drawable.logout};

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
