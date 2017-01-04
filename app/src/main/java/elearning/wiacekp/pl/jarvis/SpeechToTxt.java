package elearning.wiacekp.pl.jarvis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

import elearning.wiacekp.pl.jarvis.interfaces.RecordInterface;

public class SpeechToTxt implements android.speech.RecognitionListener, RecordInterface
{
    private SpeechRecognizer speech=null;
    private Intent recognizerIntent;

    private Context ctx;
    private Activity act;
    private TextIsRecognize tir;

    public SpeechToTxt(Context ctx, Activity act){
        this.ctx=ctx;
        this.act=act;
        tir=(TextIsRecognize)this.act;

        speech= SpeechRecognizer.createSpeechRecognizer(ctx);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "pl-PL");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, act.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    @Override
    public void onReadyForSpeech(Bundle p1)
    {
    }

    @Override
    public void onBeginningOfSpeech()
    {
    }

    @Override
    public void onRmsChanged(float p1)
    {
    }

    @Override
    public void onBufferReceived(byte[] p1)
    {
    }

    @Override
    public void onEndOfSpeech()
    {
    }

    @Override
    public void onError(int p1)
    {
        tir.CallbackTextError(getErrorText(p1));

    }

    @Override
    public void onResults(Bundle p1)
    {
        ArrayList<String>matches=p1.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        tir.CallbackText(matches);
    }

    @Override
    public void onPartialResults(Bundle p1)
    {
    }

    @Override
    public void onEvent(int p1, Bundle p2)
    {
    }



    @Override
    public void buttonIsClicked(boolean onClick)
    {
        if(onClick){
            speech.startListening(recognizerIntent);
        }else{
            speech.stopListening();
        }
    }

    public interface TextIsRecognize{
        public void CallbackText(ArrayList<String> text);
        public void CallbackTextError(String text);
    }


    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
