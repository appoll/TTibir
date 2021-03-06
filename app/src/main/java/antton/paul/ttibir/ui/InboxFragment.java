package antton.paul.ttibir.ui;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import antton.paul.ttibir.adapters.MessageAdapter;
import antton.paul.ttibir.utils.ParseConstants;
import antton.paul.ttibir.R;
// because i'm using the viewpager
/**
 * Created by Paul's on 25-Nov-14.
 */
public class InboxFragment extends ListFragment {

    protected List<ParseObject> mMessages;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        mSwipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);

            mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
            mSwipeRefreshLayout.setColorSchemeColors(
                    R.color.swipeRefresh1,
                    R.color.swipeRefresh2,
                    R.color.swipeRefresh3,
                    R.color.swipeRefresh4
                    );
        return rootView;
    }

    public void onResume(){
        // query the new message class that has been created in parse
        // get messages that have the logged in user in the recipients list
        super.onResume();

        //   getActivity().setProgressBarIndeterminateVisibility(true);

        retrieveMessages();

    }

    private void retrieveMessages() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
               // getActivity().setProgressBarIndeterminateVisibility(false);

                if (mSwipeRefreshLayout.isRefreshing())
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

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

        if (messageType.equals(ParseConstants.TYPE_IMAGE)){
            // view the image
            ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
            Uri fileUri = Uri.parse(file.getUrl());

            Intent intent = new Intent (getActivity(), ViewImageActivity.class);

            intent.setData(fileUri);
            startActivity(intent);
        }
        else if (messageType.equals(ParseConstants.TYPE_VIDEO))
        {
            // view the video
            ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
            Uri fileUri = Uri.parse(file.getUrl());

            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri,"video/*");
            startActivity(intent);
        }

        else if (messageType.equals(ParseConstants.TYPE_TEXT))
        {
            Intent intent = new Intent (getActivity(), ViewTextMessageActivity.class);

            intent.putExtra("textmessage", message.getString(ParseConstants.KEY_MESSAGE));
            intent.putExtra("messagesender", message.getString(ParseConstants.KEY_SENDER_NAME));
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

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {
            retrieveMessages();
        }
    };
}
