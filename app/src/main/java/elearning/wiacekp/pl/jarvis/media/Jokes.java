package elearning.wiacekp.pl.jarvis.media;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import elearning.wiacekp.pl.jarvis.R;

/**
 * Created by Piotrek on 2016-04-04.
 */
public class Jokes {
    private Context ctx;
    private Activity act;

    private SharedPreferences sharedpreferences;

    private ArrayList<JokeList> jokeLists = new ArrayList<>();

    private int lastJoke=0;

    public Jokes(Activity act, Context ctx){
        this.act=act;
        this.ctx=ctx;

        Thread thread  = new Thread(new getJokes());
        thread.start();

        sharedpreferences = ctx.getSharedPreferences("JokesPreference", Context.MODE_PRIVATE);
        lastJoke = sharedpreferences.getInt("lastJoke", 0);
    }

    public String nextJoke(){
        if(jokeLists.size()==0){
            return ctx.getResources().getString(R.string.loading_jokes);
        }
        String jokes = jokeLists.get(lastJoke).getJoke();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(lastJoke+1<jokeLists.size()-1) {
            editor.putInt("lastJoke", lastJoke + 1);
            lastJoke++;
        }else {
            editor.putInt("lastJoke", 0);
            lastJoke=0;
        }
        editor.commit();
        return jokes;
    }

    private class getJokes implements Runnable{
        @Override
        public void run() {
            String json="";
            try {
                Scanner in = new Scanner(ctx.getAssets().open("jokes.json"));
                do {
                    json+=in.nextLine();
                } while (in.hasNextLine());
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject;
                String id, joke;
                for(int i=0; i<jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    id=jsonObject.getString("id");
                    joke=jsonObject.getString("joke");
                    jokeLists.add(new JokeList(Integer.parseInt(id), joke));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
