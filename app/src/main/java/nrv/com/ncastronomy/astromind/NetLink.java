package nrv.com.ncastronomy.astromind;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

/**
 * Created by NRV on 8/1/2015.
 */
public class NetLink extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter movementFilter;
        movementFilter = new IntentFilter("Req.Store.Intent");
        getPostData getpostdata = new getPostData();
        registerReceiver(getpostdata, movementFilter);
        Log.d("hi","HiNet");

        return START_STICKY;
    }


    class getPostData extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("hi","HiNetBroadcast");
            Bundle bun=new Bundle();
            bun.putString("fields","likes,message,picture");
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/1000407163303601/feed",
                    bun,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
            /* handle the result */

                            try {
                                response.getRawResponse();
                                Intent in=new Intent("Get.Store.Intent");
                                //in.putExtra("Store_QTY",100.0);
                                in.putExtra("resfor","postids");
                                in.putExtra("error","ok");
                                in.putExtra("result",response.getRawResponse());
                                sendBroadcast(in);

                                Log.d("service", response.getRawResponse());
                                //postlists.setAdapter(new PostViewAdapter(HomeActivity.this, response.getJSONObject()));
                                //Toast.makeText(getApplicationContext(),response.getRawResponse(),Toast.LENGTH_LONG).show();

                            }catch (Exception e){
                                //postlists.setAdapter(new PostViewAdapter(HomeActivity.this,response.getJSONObject()));
                            }
                        }
                    }
            ).executeAsync();


        }
    }
}
