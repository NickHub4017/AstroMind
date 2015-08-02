package nrv.com.ncastronomy.astromind;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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


    class getPostData extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("hi", "HiNetBroadcast");
            if (intent.getStringExtra("request").equals("allpostids")) {
                Bundle bun = new Bundle();
                bun.putString("fields", "likes,message,picture,story,created_time,link");
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
                                    Intent in = new Intent("Get.Store.Intent");
                                    //in.putExtra("Store_QTY",100.0);
                                    in.putExtra("resfor", "postids");
                                    in.putExtra("error", "ok");
                                    String tm=response.getRawResponse();
                                   // Log.d("story count",tm.indexOf("story")+"");

                                    in.putExtra("result",tm);
                                    sendBroadcast(in);

                                    Log.d("service", ""+response.getRawResponse().length());
                                    //postlists.setAdapter(new PostViewAdapter(HomeActivity.this, response.getJSONObject()));
                                    //Toast.makeText(getApplicationContext(),response.getRawResponse(),Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    //postlists.setAdapter(new PostViewAdapter(HomeActivity.this,response.getJSONObject()));
                                }
                            }
                        }
                ).executeAsync();


            }
            else if (intent.getStringExtra("request").equals("getimages")) {
                if(intent.getStringExtra("imglink")!=null) {
                    Log.d("RequestImage", intent.getStringExtra("imglink"));
                    new FetchImageAsyncTask().execute(intent.getStringExtra("imglink"), intent.getStringExtra("id"));
                }
            }
        }
    }

    class FetchImageAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            /*ImageView p=(ImageView)getView().findViewById(R.id.img_place);
            p.setMaxWidth(l.getWidth());
            File dir=new File("sdcard/Jrider/");
            if(!dir.exists()){
                dir.mkdirs();
            }
            File img=new File("sdcard/Jrider/" + x.getPlaceName() + ".jpg");
            if(img.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile("sdcard/Jrider/" + x.getPlaceName() + ".jpg");
                p.setImageBitmap(bitmap);
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Sorry No Image available", Toast.LENGTH_LONG).show();
                p.setImageResource(R.drawable.noimage);
            }//p.setIma*/
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File dir=new File("sdcard/tempFb/");
            if(!dir.exists()){
                dir.mkdirs();
            }
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            String u;
            HttpURLConnection connection = null;
            try {
                Log.d("Image", sUrl[0]);
                if (!sUrl[0].startsWith("http")) {
                    u = "http://" + sUrl[0];
                }
                else{
                    u= sUrl[0];
                }

                Log.d("Image", u);
                URL url = new URL(u);
                //Log.d("Image", "thumbs1.ebaystatic.com//m//mOR0AuMSi9LLnSjryx8wE3Q//140.jpg");
                Log.d("Image", "con");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                Log.d("Image", "if");
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                Log.d("Image", "save");
                if (sUrl[1]!=null) {
                    output = new FileOutputStream("sdcard/tempFb/"+sUrl[1]+".jpg");
                }
                Log.d("Image", "byte");
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    //if (fileLength > 0) // only if total length is known
                    //publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                Log.d("Image", e.toString());
                e.getStackTrace();
                e.printStackTrace();
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;

        }
    }
}
