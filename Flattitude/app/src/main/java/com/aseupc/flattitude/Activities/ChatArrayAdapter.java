package com.aseupc.flattitude.Activities;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseupc.flattitude.Models.ChatMessage;
import com.aseupc.flattitude.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vavou on 13/12/2015.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private List<ChatMessage> messages = new ArrayList<ChatMessage>();
    private LinearLayout layout;

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }



    public void add(ChatMessage chatMessage) {
        messages.add(chatMessage);

        super.add(chatMessage);
    }

    public int getCount() {
        return this.messages.size();
    }

    public ChatMessage getItem(int index) {
        return messages.get(index);
    }

    public View getView(int position, View ConvertView, ViewGroup parent) {
        View v = ConvertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.chat, parent, false);
        }
        layout = (LinearLayout) v.findViewById(R.id.messageLayout);

        ChatMessage message = getItem(position);
        chatText = (TextView) v.findViewById(R.id.messageView);
        chatText.setText(message.getMessage());

        layout.setGravity(message.isLeftSide() ? Gravity.LEFT : Gravity.RIGHT);
        chatText.setBackgroundResource(message.isLeftSide()
                ? R.drawable.received_message_chat_background
                : R.drawable.sent_message_chat_background);
        chatText.setTextColor(Color.BLACK);

      //  R.color.red_btn_bg_color
        //chatText.setBackgroundColor(message.isLeftSide() ? Color.parseColor("#bfd4c1") : Color.parseColor("#f1AAAA"));

        return v;
    }
}