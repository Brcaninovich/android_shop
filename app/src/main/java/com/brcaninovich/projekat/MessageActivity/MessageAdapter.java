package com.brcaninovich.projekat.MessageActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brcaninovich.projekat.MessageActivity.Objects.Message;
import com.brcaninovich.projekat.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private String currentUserId;

    // Konstante za tipove view-a
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessageAdapter(List<Message> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder za poslane poruke
    private static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageBody;

        public SentMessageViewHolder(View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.text_message_body_sent);
        }

        void bind(Message message) {
            messageBody.setText(message.getMessage());
        }
    }

    // ViewHolder za primljene poruke
    private static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageBody;

        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.text_message_body_received);
        }

        void bind(Message message) {
            messageBody.setText(message.getMessage());
        }
    }
}

