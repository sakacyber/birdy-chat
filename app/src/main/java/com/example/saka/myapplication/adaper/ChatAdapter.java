package com.example.saka.myapplication.adaper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saka.myapplication.R;
import com.example.saka.myapplication.model.Message;
import com.example.saka.myapplication.model.myFirebase;

import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private final List<Message> chatMessages;
    private Context context;

    public ChatAdapter(Context context, List<Message> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public Message getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.viewholder_message, parent, false);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message chatMessage = getItem(position);
        setAlignment(holder, chatMessage.getSenderUid());
        holder.message.setText(chatMessage.getMessage());
        return convertView;
    }

    private void setAlignment(ViewHolder holder, String senderUid) {
        if (!senderUid.equals(myFirebase.getMyUid())) {
            holder.background.setBackgroundResource(R.drawable.chat_u_bg);
            holder.message.setTextColor(Color.BLACK);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.container.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.container.setLayoutParams(lp);
        } else {
            holder.background.setBackgroundResource(R.drawable.chat_me_bg);
            holder.message.setTextColor(Color.WHITE);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.container.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.container.setLayoutParams(lp);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) v.findViewById(R.id.vh_msg_txt);
        holder.container = (LinearLayout) v.findViewById(R.id.vh_msg_container);
        holder.background = (LinearLayout) v.findViewById(R.id.vh_msg_bg);
        holder.layout = (RelativeLayout) v.findViewById(R.id.vh_msg_layout);
        holder.timestamp = (TextView) v.findViewById(R.id.vh_msg_timestamp);
        return holder;
    }

    private static class ViewHolder {
        protected RelativeLayout layout;
        private TextView message;
        private TextView timestamp;
        private LinearLayout container;
        private LinearLayout background;
    }
}
