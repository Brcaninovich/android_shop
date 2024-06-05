package com.brcaninovich.projekat.MessageActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.brcaninovich.projekat.MainApplication.ui.messages.Chat;
import com.brcaninovich.projekat.MessageActivity.Objects.Message;
import com.brcaninovich.projekat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private Button sendMessageButton;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private FirebaseFirestore db;
    private String currentUserId;
    private String chatId;
    private String receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendMessageButton = findViewById(R.id.sendMessageButton);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chatId = getIntent().getStringExtra("chatId");
        receiverId = getIntent().getStringExtra("receiverId");

        if (chatId == null) {
            throw new IllegalArgumentException("Chat ID must not be null");
        }

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUserId);
        messagesRecyclerView.setAdapter(messageAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMessages();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadMessages() {
        DocumentReference chatRef = db.collection("chats").document(chatId);
        chatRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Chat chat = snapshot.toObject(Chat.class);
                    if (chat != null) {
                        messageList.clear();
                        messageList.addAll(chat.getMessages());
                        messageAdapter.notifyDataSetChanged();
                        if (!messageList.isEmpty()) {
                            messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                }
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString();
        if (!messageText.isEmpty()) {
            String artikalId = getIntent().getStringExtra("artikalId");
            String nazivArtikla = getIntent().getStringExtra("nazivArtikla");

            Message message = new Message(
                    currentUserId,
                    receiverId,
                    messageText,
                    System.currentTimeMillis(),
                    artikalId,
                    nazivArtikla
            );

            DocumentReference chatRef = db.collection("chats").document(chatId);
            chatRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Chat chat = document.toObject(Chat.class);
                        if (chat != null) {
                            chat.getMessages().add(message);
                            chat.setLastMessage(messageText);
                            chat.setLastMessageTimestamp(System.currentTimeMillis());
                            chatRef.set(chat);
                        }
                    } else {
                        List<Message> messages = new ArrayList<>();
                        messages.add(message);
                        List<String> participantIds = new ArrayList<>();
                        participantIds.add(currentUserId);
                        participantIds.add(receiverId);

                        Chat chat = new Chat();
                        chat.setParticipantIds(participantIds);
                        chat.setLastMessage(messageText);
                        chat.setLastMessageTimestamp(System.currentTimeMillis());
                        chat.setArtikalId(artikalId);
                        chat.setNazivArtikla(nazivArtikla);
                        chat.setMessages(messages);

                        chatRef.set(chat);
                    }

                    messageEditText.setText("");
                }
            });
        }
    }
}
