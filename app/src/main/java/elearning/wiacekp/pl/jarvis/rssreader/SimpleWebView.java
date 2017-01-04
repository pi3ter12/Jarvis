package elearning.wiacekp.pl.jarvis.rssreader;

import android.os.AsyncTask;

import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;

/**
 * Created by Piotrek on 2016-04-02.
 */
public class SimpleWebView extends AsyncTask<String, Void, String[]>{ //todo: modify this
        @Override
        protected String[] doInBackground(String... params) {
            JResult res = null;
            try {
                HtmlFetcher fetcher;
                fetcher = new HtmlFetcher();
                res = fetcher.fetchAndExtract(params[0], 5000, true);

                String[] out = new String[2];
                out[1] = res.getText();
                out[0] = res.getTitle();
                return out;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

}
