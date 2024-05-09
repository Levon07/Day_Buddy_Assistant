package com.example.daybuddy.chatgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daybuddy.R;

import java.util.List;
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatMessage> chatMessages;
    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).isUser() ? 0 : 1;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View userMessageView = inflater.inflate(R.layout.item_chat_bubble_user, parent, false);
            return new UserMessageViewHolder(userMessageView);
        } else {
            View aiMessageView = inflater.inflate(R.layout.item_chat_bubble_ai, parent, false);
            return new AiMessageViewHolder(aiMessageView);
        }
    }
    // ViewHolder class for user messages
    private static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        UserMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_view_message);
        }
        void bind(ChatMessage message) {
            messageTextView.setText(message.getMessage());
        }
    }
    // ViewHolder class for AI-generated messages
    private static class AiMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        AiMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_view_message);
        }
        void bind(ChatMessage message) {
            messageTextView.setText(message.getMessage());
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (holder.getItemViewType() == 0) {
            ((UserMessageViewHolder) holder).bind(chatMessage);
        } else {
            ((AiMessageViewHolder) holder).bind(chatMessage);
        }
    }
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }
    // Add the addMessage method to add new ChatMessage objects to the adapter
    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        notifyItemInserted(chatMessages.size() - 1);
    }
}