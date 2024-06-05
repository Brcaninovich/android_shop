package com.brcaninovich.projekat.MainApplication.ui.messages;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessagesViewModel extends ViewModel {

    private MutableLiveData<List<Chat>> chatListLiveData;

    public MessagesViewModel() {
        chatListLiveData = new MutableLiveData<>();
        loadChats();
    }

    public LiveData<List<Chat>> getChatListLiveData() {
        return chatListLiveData;
    }

    private void loadChats() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats")
                .whereArrayContains("participantIds", currentUserId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("MessagesViewModel2", "Error loading chats", e);
                        return;
                    }
                    List<Chat> chatList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Chat chat = doc.toObject(Chat.class);
                        chatList.add(chat);
                        Log.d("MessagesViewModel2", "Chat loaded: " + chat.getNazivArtikla());
                    }
                    chatListLiveData.setValue(chatList);
                });
    }
}
