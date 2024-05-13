package com.example.daybuddy.chatgpt;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.daybuddy.R;

public class ChatGPTActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    LinearLayout popup_menu;
    LinearLayout blur_layout;
    public boolean is_popped_up;
    public String responsemessage = null;

    public boolean flag = true;

    Button approve_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_gptactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        recyclerView = findViewById(R.id.recycler_view_chat);
        chatAdapter = new ChatAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);
        // Set the custom item decoration to add spacing between chat bubbles
        int spacingBetweenBubbles = getResources().getDimensionPixelSize(R.dimen.spacing_between_bubbles);
        recyclerView.addItemDecoration(new ChatItemDecoration(spacingBetweenBubbles));
        ImageButton sendButton = findViewById(R.id.generate_button);
        EditText inputEditText = findViewById(R.id.edit_text_input);

        sendButton.setOnClickListener(v -> {
            String userMessage = inputEditText.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                addMessageToChat(new ChatMessage( userMessage, true));
                // ask chat gpt
                askChatGpt(userMessage);
                inputEditText.setText("");

            }
        });
    }

    public void ShowPopup(View view){
        if (!is_popped_up){
            popup_menu.setVisibility(View.VISIBLE);
            blur_layout.setVisibility(View.VISIBLE);
            is_popped_up = true;
        }else {
            popup_menu.setVisibility(View.GONE);
            blur_layout.setVisibility(View.GONE);
            is_popped_up = false;
        }

    }

    public void main_layout_touch(View view){
        if (is_popped_up){
            popup_menu.setVisibility(View.GONE);
            blur_layout.setVisibility(View.GONE);
            is_popped_up = false;
        }
    }


//    public void Anim_UPnDOWN(Button button){
//
//        Handler handler = new Handler();
//
//        Runnable UP = new Runnable() {
//            @Override
//            public void run() {
//                button.animate().translationY(-25).setDuration(500);
//            }
//        };
//
//        Runnable DOWN = new Runnable() {
//            @Override
//            public void run() {
//                button.animate().translationY(25).setDuration(500);
//            }
//        };
//
//        Runnable FLAG = new Runnable() {
//            @Override
//            public void run() {
//                flag = true;
//            }
//        };
//
//        Runnable all = new Runnable() {
//            @Override
//            public void run() {
//
//                flag = false;
//
//
//                handler.postDelayed(UP, 0);
//                handler.postDelayed(DOWN, 500);
//                handler.postDelayed(UP, 1000);
//                handler.postDelayed(DOWN, 1500);
//                handler.postDelayed(UP, 2000);
//                handler.postDelayed(DOWN, 2500);
//                handler.postDelayed(UP, 3000);
//                handler.postDelayed(DOWN, 3500);
//
//                handler.postDelayed(FLAG,4000);
//
//
//            }
//        };
//
//        if(flag) {
//
//
//            handler.postDelayed(all, 100);
//
//
//        }
//    }



















    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //CHAT GPT CODE
    // Method to add a new message to the chat list
    private void addMessageToChat(ChatMessage chatMessage) {
        chatAdapter.addMessage(chatMessage);
        recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }
    // interact with chat gpt api
    List<ChatMessage> chatHistory = new ArrayList<>();
    private void askChatGpt(String userPrompt) {
        // Create the Retrofit client
        chatHistory.add(new ChatMessage(userPrompt, true));
        OpenAIAPIClient.OpenAIAPIService apiService = OpenAIAPIClient.create();
        // Create the request model
        Message message = new Message("user", userPrompt);
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        OpenAIRequestModel requestModel = new OpenAIRequestModel(
                "gpt-4-turbo",
                getMessagesForRequest(chatHistory),
                0.7f);
        // Make the API request
        Call<OpenAIResponseModel> call = apiService.getCompletion(requestModel);
        call.enqueue(new Callback<OpenAIResponseModel>() {
            @Override
            public void onResponse(Call<OpenAIResponseModel> call, Response<OpenAIResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OpenAIResponseModel responseBody = response.body();
                    String generatedText = responseBody.getChoices()[0].getMessage().getContent();

                    responsemessage = generatedText;
                    addMessageToChat(new ChatMessage(generatedText, false));
                } else {
                    // Handle API error
                    addMessageToChat(new ChatMessage("API error", false));
                }
            }
            @Override
            public void onFailure(Call<OpenAIResponseModel> call, Throwable t) {
                // Handle network or request failure
                addMessageToChat(new ChatMessage("API onFailure: "+t.getMessage(), false));
            }
        });
    }

    private List<Message> getMessagesForRequest(List<ChatMessage> chatHistory) {
        List<Message> messageList = new ArrayList<>();
        for (ChatMessage chatMessage : chatHistory) {
            messageList.add(new Message(chatMessage.isMe() ? "user" : "bot", chatMessage.getMessage()));
        }
        return messageList;
    }

    public void Back(View view) {

        finish();

    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



}