package dk.lasse_it.smartcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Musicplayer_web extends Activity {
    Globals g = Globals.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcplayer_web);
        WebView wv = (WebView) findViewById(R.id.mcplayer_webview);
        WebSettings wvs = (WebSettings) wv.getSettings();
        wv.setWebViewClient(new WebViewClient());
        wvs.setJavaScriptEnabled(true);
        wv.loadUrl("http://"+g.getAlarmserverip()+":6680/mopify");
    }
}
