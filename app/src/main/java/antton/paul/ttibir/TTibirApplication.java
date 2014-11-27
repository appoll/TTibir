package antton.paul.ttibir;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

import antton.paul.ttibir.utils.ParseConstants;

/**
 * Created by Paul's on 25-Nov-14.
 */
public class TTibirApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "wcVmVWTEHrSCuZ4e08AfLpuKkTpp0vAKVkthI1Lb", "uZvQa4jHNUMldnHVP7kPiRzK7Ldf87kYPPskpjZk");

    }


    public static void updateParseInstallation (ParseUser user)
    {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }

}
