package com.navigatevr.androidwebvr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    private WebView wv;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv = (WebView) findViewById(R.id.webView);
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());

        wv.addJavascriptInterface(new WebVRInterface(this), "Navigator");
        wv.addJavascriptInterface(new AndroidInterface(this), "Android");

        WebSettings websettings = wv.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setAllowUniversalAccessFromFileURLs(true);
        websettings.setMediaPlaybackRequiresUserGesture(false);

        String site = "http://threejs.org";
        Uri uri = Uri.parse(site);
        //wv.loadUrl(site);
        wv.loadUrl("file:///android_asset/index.html");

        setupEvents();
    }

    private void setupEvents() {
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            wv.loadUrl("http://dev.quasi.co/cardboard-vision/#default");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    WebVR interface functions
    */
    public class WebVRInterface {
        Context mContext;

        WebVRInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public String getVRDevices() {
            return "{\"devices\": []}";
        }

        @JavascriptInterface
        public String getState() {
            String x = String.format("%.3f", Math.random());
            String y = String.format("%.3f", Math.random());
            String z = String.format("%.3f", Math.random());
            String w = String.format("%.3f", Math.random());
            return "{\"hmd\": { \"orientation\": ["+x+","+y+","+z+","+w+"]}}";
        }

        @JavascriptInterface
        public int answer() {
            return 42;
        }
    }

    /*
    Android interface
    */
    public class AndroidInterface {
        Context mContext;

        AndroidInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
