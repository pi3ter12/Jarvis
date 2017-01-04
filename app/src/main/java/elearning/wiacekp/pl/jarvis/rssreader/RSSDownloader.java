package elearning.wiacekp.pl.jarvis.rssreader;

import java.net.URL;
import java.util.ArrayList;
import android.os.AsyncTask;

import elearning.wiacekp.pl.jarvis.rssreader.RSSDownloadedItem;

public class RSSDownloader extends AsyncTask<ArrayList<String>, Void, ArrayList<RSSDownloadedItem>> {

	@Override
	protected ArrayList<RSSDownloadedItem> doInBackground(ArrayList<String>... params) {
        ArrayList<RSSDownloadedItem> out = new ArrayList<>();
        for(int i=0; i<params[0].size(); i++) {
            try {
                URL url = new URL(params[0].get(i));
                ArrayList<RSSItem> rssItems = RSSReader.startReader(url);
                out.add(new RSSDownloadedItem(i, rssItems, params[0].get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	        return out;
	}

}
