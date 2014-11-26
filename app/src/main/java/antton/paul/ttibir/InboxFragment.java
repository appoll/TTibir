package antton.paul.ttibir;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
// because i'm using the viewpager
/**
 * Created by Paul's on 25-Nov-14.
 */
public class InboxFragment extends ListFragment {

    protected List<ParseObject> mMessages;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        return rootView;
    }

    public void onResume(){
        // query the new message class that has been created in parse
        // get messages that have the logged in user in the recipients list
        super.onResume();

        getActivity().setProgressBarIndeterminateVisibility(true);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (e == null)
                {
                    // we found messages
                    mMessages = messages;

                    String[] usernames = new String[mMessages.size()];

                    int i = 0;
                    for (ParseObject message : mMessages) {
                        usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;

                    }
/*
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            usernames
                    );
*/
                    if (getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);

                        setListAdapter(adapter);
                    }
                    else
                    {
                        // refill the adapter
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);
                    }
                }
            }
        });

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());

        if (messageType.equals(ParseConstants.TYPE_IMAGE)){
            // view the image
            Intent intent = new Intent (getActivity(), ViewImageActivity.class);

            intent.setData(fileUri);
            startActivity(intent);
        }
        else
        {
            // view the video
            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri,"video/*");
            startActivity(intent);
        }

        // Delete the message
        // check the recipients. delete one by one until list of recipients is empty.
        // when list is empty, delete message from parse
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);

        if (ids.size() == 1)
        {
            // last recipient - delete message from back-end
            message.deleteInBackground();
        }
        else
        {
            // remove the recipient and save
            ids.remove(ParseUser.getCurrentUser().getObjectId());

            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
            message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
            message.saveInBackground();
            
        }

    }
}
