package elearning.wiacekp.pl.jarvis;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import elearning.wiacekp.pl.jarvis.logic.MainLogic;
import elearning.wiacekp.pl.jarvis.rssreader.SimpleWebView;

public class MainActivity extends Activity implements
        TextToSpeech.OnInitListener,
        SpeechToTxt.TextIsRecognize,
        MainLogic.MLogicListener {

    private static final int REQUEST_CODE = 100;

    private TextToSpeech tts;
    private SpeechToTxt stt;
    private MainLogic ml;

    private HashMap<String, String> map = new HashMap<String, String>();

    private ImageButton ib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ib = (ImageButton) this.findViewById(R.id.microphone);
        ib.setOnTouchListener(new microphoneOnTouchListener());

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

            tts = new TextToSpeech(this, this);
            tts.setLanguage(new Locale("pl"));

            stt = new SpeechToTxt(this, this);

            ml = new MainLogic(this, this);

            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "BookSpeek");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
        }
    }

    public class microphoneOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                clickMicrophone(true);
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                clickMicrophone(false);
                return true;
            }
            return false;
        }
    }

    public void clickMicrophone(boolean onClick){
        stt.buttonIsClicked(onClick);
        ml.buttonIsClicked(onClick);
        if (onClick) {
            tellText("", false);
        }
    }

    public void clickHelp(View v) {
        Intent intent = new Intent(this, HelpActivity.class);
        this.startActivity(intent);
    }

    public void clickSettings(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onInit(int p1) {
        tellText(this.getResources().getString(R.string.application_started), false);
        ml.updateRSSFiles();
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                ml.booksIsDone();
            }

            @Override
            public void onError(String utteranceId) {
            }
        });
    }

    @Override
    public void CallbackText(ArrayList<String> text) {
        String out = "";
        for (int i = 0; i < text.size(); i++) {
            out += text.get(i) + "\n";
        }
        ml.setInput(text);
    }

    @Override
    public void CallbackTextError(String text) {
        tellText("", false);
    }


    @Override
    public void tellText(String text, boolean book) {
        Log.d("MAtellText", text);
        if (book) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void addText(String text, boolean book) {
        Log.d("MAaddText", text);
        if (book) {
            tts.speak(text, TextToSpeech.QUEUE_ADD, map);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }
}
