package com.example.daybuddy.chatgpt;

import android.content.ActivityNotFoundException;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Locale;
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
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private TextToSpeech textToSpeech;


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



        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                }
            } else {
                Log.e("TTS", "Initialization failed");
            }
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




    private void speakResponse(String response) {
        if (textToSpeech != null && !TextUtils.isEmpty(response)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }



    public void startVoiceInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                String userVoiceInput = result.get(0);
                // Process the userVoiceInput (send it to ChatGPT or handle as needed)
                if (!userVoiceInput.isEmpty()) {
                    addMessageToChat(new ChatMessage(userVoiceInput, true));
                    // Send the voice input to ChatGPT
                    askChatGpt(userVoiceInput);
                }
            }
        }
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
                "gpt-4o",
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
                    speakResponse(generatedText);
                } else {
                    // Handle API error
                    addMessageToChat(new ChatMessage("API error", false));
                }
            }
            @Override
            public void onFailure(Call<OpenAIResponseModel> call, Throwable t) {
                // Handle network or request failure
                addMessageToChat(new ChatMessage("Internet connection error", false));
            }
        });
    }


    @Override
    protected void onDestroy() {
        // Release resources like TextToSpeech engine
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
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