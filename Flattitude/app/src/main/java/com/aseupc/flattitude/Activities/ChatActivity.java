package com.aseupc.flattitude.Activities;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.aseupc.flattitude.ChatArrayAdapter;
import com.aseupc.flattitude.Models.ChatMessage;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.synchronization.JabberSmackAPI;

import org.jivesoftware.smack.packet.Message;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ChatArrayAdapter adapter;
    private ListView messagesList;
    private EditText messageTextField;
    private Button sendButton;
    private JabberSmackAPI chatSmack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);

        sendButton = (Button) findViewById(R.id.chat_send_button);
        messagesList = (ListView) findViewById(R.id.chat_list_view);
        adapter = new ChatArrayAdapter(getApplicationContext(), R.layout.chat);
        messageTextField = (EditText) findViewById(R.id.chat_message_input);
        messageTextField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendChatMessage();
            }
        });

        messagesList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesList.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                messagesList.setSelection(adapter.getCount() - 1);
            }
        });

        chatSmack = IDs.getInstance(getApplicationContext()).getSmackChat();
        chatSmack.getListener().activateChatWindow(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean sendChatMessage() {
        JabberSmackAPI smackChat = IDs.getInstance(getApplicationContext()).getSmackChat();

        smackChat.sendGroupMessage(messageTextField.getText().toString());

        adapter.add(new ChatMessage(messageTextField.getText().toString()));

        messageTextField.setText("");
        return true;
    }

    //This method has to be called when the ChatActivity is created, in order to update the List of Messages.
    public void updateMessages(List<Message> oldMessages) {

        for (Message message : oldMessages) {
            String receiver = message.getFrom().substring(message.getFrom().indexOf("/") + 1);

            if (chatSmack.getUserName().equalsIgnoreCase(receiver))
                adapter.add(new ChatMessage(receiver + ": " + message.getBody(), false));
            else
                adapter.add(new ChatMessage(receiver + ": " + message.getBody(), false));
        }


    }

    //In case the ChatActivity is created, the listener sends the message back to here.
    public void showMessage(Message message) {
        String receiver = message.getFrom().substring(message.getFrom().indexOf("/") + 1);

        if (chatSmack.getUserName().equalsIgnoreCase(receiver))
            adapter.add(new ChatMessage(receiver + ": " + message.getBody(), false));
        else
            adapter.add(new ChatMessage(receiver + ": " + message.getBody(), false));

    }
}

