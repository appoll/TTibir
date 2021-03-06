package antton.paul.ttibir.utils;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

import antton.paul.ttibir.ui.MainActivity;

/**
 * Created by Paul's on 28-Nov-14.
 */
public class Receiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}