package antton.paul.ttibir.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import antton.paul.ttibir.R;

/**
 * Created by Paul's on 23-Dec-14.
 */


public class ViewTextMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text_message);

        TextView sender = (TextView) findViewById(R.id.senderTextView);

        TextView message = (TextView) findViewById( R.id.viewMessageTextView);

        String textMessage = getIntent().getStringExtra("textmessage");
        String messageSender = getIntent().getStringExtra("messagesender");

        sender.append(messageSender);
        message.setText(textMessage);

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                finish();
            }
        },10*1000);
    }
}
