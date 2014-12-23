package antton.paul.ttibir.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import antton.paul.ttibir.R;

/**
 * Created by Paul's on 23-Dec-14.
 */
public class NewMessageActivity extends Activity {
    EditText userInput;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_dialog);

        userInput = (EditText) findViewById(R.id.editTextDialogUserInput);

    }


    public void sendMessage(View view)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("message",userInput.getText().toString());

        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
