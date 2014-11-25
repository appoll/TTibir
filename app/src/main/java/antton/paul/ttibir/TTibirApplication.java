package antton.paul.ttibir;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Paul's on 25-Nov-14.
 */
public class TTibirApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "wcVmVWTEHrSCuZ4e08AfLpuKkTpp0vAKVkthI1Lb", "uZvQa4jHNUMldnHVP7kPiRzK7Ldf87kYPPskpjZk");

    }

}
