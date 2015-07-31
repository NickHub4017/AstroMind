package nrv.com.ncastronomy.astromind;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by NRV on 7/30/2015.
 */
public class AstroApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PrintKey();
    }
    public void PrintKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "ncastronomy.com.nrv.astromind",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
}
