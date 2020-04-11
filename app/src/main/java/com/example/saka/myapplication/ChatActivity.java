package com.example.saka.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.saka.myapplication.adaper.ChatAdapter;
import com.example.saka.myapplication.model.myConstant;
import com.example.saka.myapplication.model.Message;
import com.example.saka.myapplication.model.myFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtMessage;
    private ListView messageContainer;

    private ChatAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Message> chatHistory = new ArrayList<>();
    private String myUid = myFirebase.getMyUid();
    private String chatWith = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageContainer = (ListView) findViewById(R.id.chat_messageContainer);
        edtMessage = (EditText) findViewById(R.id.chat_edt_msg);
        ImageButton btnSend = (ImageButton) findViewById(R.id.chat_btn_send);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        messageContainer.setDivider(null);
        chatWith = getIntent().getStringExtra("chatWith");
        setTitle(myFirebase.getUser(chatWith).getName());

        adapter = new ChatAdapter(getApplicationContext(), chatHistory);
        messageContainer.setAdapter(adapter);
        btnSend.setOnClickListener(this);

        databaseReference.child(myConstant.USERS).child(myUid).child("zChat").child(chatWith)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        loadConversation(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void scroll() {
        messageContainer.setSelection(messageContainer.getLastVisiblePosition());
    }

    private void loadConversation(DataSnapshot dataSnapshot) {
        List<Message> tempMessages = new ArrayList<>();
        for (Object object : dataSnapshot.getChildren()) {
            Message msg = ((DataSnapshot)object).getValue(Message.class);
            tempMessages.add(msg);
        }
        chatHistory.clear();
        chatHistory.addAll(tempMessages);
        adapter.notifyDataSetChanged();
        tempMessages.clear();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.chat_btn_send) {
            String messageText = edtMessage.getText().toString();
            if (TextUtils.isEmpty(messageText)) {
                return;
            }
            sendMessage(messageText);
        }
    }

    private void sendMessage(String messageText) {
        String key = myFirebase.getNewPushKey();
        Message message = new Message();
        message.setId(key);
        message.setMessage(messageText);
        message.setTimeStamp(SimpleDateFormat.getDateTimeInstance().format(new Date()));
        message.setSenderUid(myUid);

        databaseReference.child(myConstant.USERS).child(myUid).child("zChat")
                .child(chatWith).child(key).setValue(message);

        databaseReference.child(myConstant.USERS).child(chatWith).child("zChat")
                .child(myUid).child(key).setValue(message);

        edtMessage.setText("");
        scroll();
    }
}
