package elearning.wiacekp.pl.jarvis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.ArrayList;

import elearning.wiacekp.pl.jarvis.commands.CommandsList;
import elearning.wiacekp.pl.jarvis.helpclasses.Helper;

public class HelpActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        webView = (WebView)this.findViewById(R.id.help_activity_webview);

        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        String[] description = this.getResources().getStringArray(R.array.help);

        String html  = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1250\">"+
                "<title></title></head><body>";
        Helper helper = new Helper(this);
        ArrayList<CommandsList> commandsLists = helper.getCommandList();
        for(int i=0; i<commandsLists.size(); i++){
            html+="<b>"+commandsLists.get(i).getCommand()+"</b> - "+description[commandsLists.get(i).getCommandNumber()-1]+"<br>";
            Log.d("HelpActivity", "<b>"+commandsLists.get(i).getCommand()+"</b> - "+description[commandsLists.get(i).getCommandNumber()-1]+"<br><br>");
        }

        html+="</body></html>";
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
    }
}
