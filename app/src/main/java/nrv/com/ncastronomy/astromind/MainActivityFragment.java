package nrv.com.ncastronomy.astromind;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    AccessTokenTracker tracker;
    ProfileTracker profileTracker;
    TextView textView;
    Button btn;
    CallbackManager callbackManager;
    FacebookCallback<LoginResult> callback=new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken=loginResult.getAccessToken();
            Profile profile=Profile.getCurrentProfile();

            validchaeck(profile);


        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        tracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken_old, AccessToken accessToken1_new) {

            }
        };
        profileTracker =new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile_old, Profile profile1_new) {
                Profile profile=Profile.getCurrentProfile();
                validchaeck(profile);

            }
        };
        tracker.startTracking();
        profileTracker.startTracking();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view=inflater.inflate(R.layout.fragment_main, container, false);
        textView = (TextView) view.findViewById(R.id.dataview);
        btn=(Button)view.findViewById(R.id.button);
        view.setBackgroundColor(Color.TRANSPARENT);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//manage_pages
                Bundle para = new Bundle();
                para.putString("fields", "picture,icon");//https://developers.facebook.com/docs/graph-api/reference/v2.4/post
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "1000407163303601/feed",//"/1030599366951047",  //?fields=id,name,likes,link,icon,picture
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback(

                        ) {
                            public void onCompleted(GraphResponse response) {
                                /*Log.d("Response", response.getError().toString());
                                Toast.makeText(getActivity().getApplicationContext(), response.getError().toString(), Toast.LENGTH_LONG).show();
                                textView.setText(response.getError().toString());*/
            /* handle the result */


                                if (response != null) {
                                    Log.d("Response", response.getRawResponse());
                                    Toast.makeText(getActivity().getApplicationContext(), response.getRawResponse(), Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d("Response", "Null");
                                    Toast.makeText(getActivity().getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                ).executeAsync();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile=Profile.getCurrentProfile();
        validchaeck(profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        tracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton=(LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_posts"));//manage_pages,"user_friends"
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void validchaeck(Profile profile){
        if (profile!=null){
            textView.setText("Welcome to Astromind "+profile.getName());
            Intent home=new Intent(getActivity(),HomeActivity.class);
            startActivity(home);
        }
        else {
            textView.setText("Welcome to Astromind");
        }
    }

}
