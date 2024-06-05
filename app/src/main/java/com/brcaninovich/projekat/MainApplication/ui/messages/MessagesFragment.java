package com.brcaninovich.projekat.MainApplication.ui.messages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.brcaninovich.projekat.R;
import java.util.List;

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;
    private RecyclerView recyclerViewChats;
    private ChatAdapter chatAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerViewChats = root.findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));

        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        messagesViewModel.getChatListLiveData().observe(getViewLifecycleOwner(), chatList -> {
            if (chatList != null) {
                setupRecyclerView(chatList);
            }
        });

        return root;
    }

    private void setupRecyclerView(List<Chat> chatList) {
        chatAdapter = new ChatAdapter(chatList, getContext());
        recyclerViewChats.setAdapter(chatAdapter);
    }
}
