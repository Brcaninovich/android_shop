package com.brcaninovich.projekat.MainApplication.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.brcaninovich.projekat.MainApplication.ui.messages.Chat;
import com.brcaninovich.projekat.MessageActivity.MessageActivity;
import com.brcaninovich.projekat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.artikalNameTextView.setText(chat.getNazivArtikla());
        holder.lastMessageTextView.setText(chat.getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("chatId", generateChatId(chat));
            intent.putExtra("receiverId", getReceiverId(chat));
            intent.putExtra("artikalId", chat.getArtikalId());
            intent.putExtra("nazivArtikla", chat.getNazivArtikla());
            context.startActivity(intent);
        });
    }

    private String generateChatId(Chat chat) {
        List<String> participantIds = chat.getParticipantIds();
        Collections.sort(participantIds);
        return participantIds.get(0) + "_" + participantIds.get(1);
    }

    private String getReceiverId(Chat chat) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for (String participantId : chat.getParticipantIds()) {
            if (!participantId.equals(currentUserId)) {
                return participantId;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView artikalNameTextView;
        TextView lastMessageTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            artikalNameTextView = itemView.findViewById(R.id.artikalName);
            lastMessageTextView = itemView.findViewById(R.id.lastMessage);
        }
    }
}
