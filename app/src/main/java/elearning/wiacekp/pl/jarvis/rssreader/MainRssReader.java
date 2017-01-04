package elearning.wiacekp.pl.jarvis.rssreader;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Scanner;

import elearning.wiacekp.pl.jarvis.commands.CommandsList;

/**
 * Created by Piotrek on 2016-04-03.
 */
public class MainRssReader {
    private Activity act;
    private Context ctx;
    private ArrayList<RSSDownloadedItem> feeds = new ArrayList<>();
    private ArrayList<String> subscriptions = new ArrayList<>();
    private ArrayList<String> subscriptionsTitle = new ArrayList<>();

    private int selectedSubscription = 0;

    public int getSelectedArticle() {
        return selectedArticle;
    }

    public void setSelectedArticle(int selectedArticle) {
        this.selectedArticle = selectedArticle;
    }

    private int selectedArticle = 0;

    public int getSelectedSubscription() {
        return selectedSubscription;
    }

    public void setSelectedSubscription(int selectedSubscription) {
        this.selectedSubscription = selectedSubscription;
    }

    public ArrayList<String> getSubscriptionsTitle() {
        return subscriptionsTitle;
    }

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    public MainRssReader(Activity act, Context ctx) {
        this.act = act;
        this.ctx = ctx;
        generateSubscriptions();
    }

    public void updateList() {
        feeds.clear();
        new RSSDownloader() {
            @Override
            protected void onPostExecute(ArrayList<RSSDownloadedItem> rssDownloadedItems) {
                feeds = rssDownloadedItems;
            }
        }.execute(subscriptions);
    }


    public ArrayList<RSSDownloadedItem> getFeeds() {
        return feeds;
    }

    private void generateSubscriptions() {
        String json = "";
        try {
            Scanner in = new Scanner(ctx.getAssets().open("feedly.opml"));
            do {
                json += in.nextLine();
            } while (in.hasNextLine());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        subscriptions.clear();
        ArrayList<String> out = normalizeText(json);
        for (int i = 0; i < out.size(); i++) {
            if (searchHelper(out.get(i), "xmlUrl")) {
                subscriptions.add(getLinkCode(out.get(i)));
                subscriptionsTitle.add(getTitleCode(out.get(i)));

                Log.d("RSSSubscription", getTitleCode(out.get(i)) + " - " + getLinkCode(out.get(i)));
            }
        }
    }

    private ArrayList<String> normalizeText(String text) { //todo: Gsoup
        ArrayList<String> out = new ArrayList<String>();
        String line = "";

        for (int i = 0; i < text.length(); i++) {
            if (i == text.length() - 1) {
                out.add(line);
                line = "";
            }
            if (text.charAt(i) == '<') {
                out.add(line);
                line = "";
            }
            line += text.charAt(i);
        }

        return out;
    }

    private boolean searchHelper(String tekst, String str) { //todo: change this method
        boolean end = false;
        int licznik = 0;

        for (int i = 0; i < tekst.length(); i++) {
            if (tekst.charAt(i) == str.charAt(0)) {
                if (i + str.length() <= tekst.length()) {
                    licznik = 1;
                    for (int j = 1, k = i + 1; j < str.length(); j++, k++) {
                        if (tekst.charAt(k) == str.charAt(j)) {
                            licznik++;
                        }
                    }
                }
                if (licznik == str.length()) {
                    i = tekst.length();
                    end = true;
                }
            }

        }
        return end;
    }

    private String getTitleCode(String input) { //todo: change this method
        String out = "";
        if (searchHelper(input, "title")) {
            for (int i = 0; i < input.length() - 7; i++) {
                if ((input.charAt(i) == 't') &&
                        (input.charAt(i + 1) == 'i') &&
                        (input.charAt(i + 2) == 't') &&
                        (input.charAt(i + 3) == 'l') &&
                        (input.charAt(i + 4) == 'e')) {

                    for (int j = i + 7; input.charAt(j) != '\"'; j++) {
                        out += input.charAt(j);
                    }
                    break;
                }
            }
        }
        return out;
    }

    private String getLinkCode(String input) { //todo: change this method
        String out = "";
        if (searchHelper(input, "title")) {
            for (int i = 0; i < input.length() - 8; i++) {
                if ((input.charAt(i) == 'x') &&
                        (input.charAt(i + 1) == 'm') &&
                        (input.charAt(i + 2) == 'l') &&
                        (input.charAt(i + 3) == 'U') &&
                        (input.charAt(i + 4) == 'r') &&
                        (input.charAt(i + 5) == 'l')) {

                    for (int j = i + 8; input.charAt(j) != '\"'; j++) {
                        out += input.charAt(j);
                    }
                    break;
                }
            }
        }
        return out;
    }

}
