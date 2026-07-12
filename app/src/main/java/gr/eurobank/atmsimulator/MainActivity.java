package gr.eurobank.atmsimulator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private WebView webView;
    private TextToSpeech tts;
    private volatile boolean ttsReady = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Native Android text-to-speech engine -- this is what actually gives Aris a voice,
        // since Android WebView does not implement the browser's speechSynthesis API.
        tts = new TextToSpeech(this, this);

        webView = findViewById(R.id.atmWebView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Expose speak() to the page's JavaScript as window.AndroidTTS.speak(text)
        webView.addJavascriptInterface(new AndroidTTSBridge(), "AndroidTTS");

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(new Locale("el", "GR"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Greek voice pack isn't installed on this device -- fall back to whatever is
                // available rather than staying silent. (Android Settings > System > Languages >
                // Text-to-speech output lets the user install the Greek voice for the real thing.)
                tts.setLanguage(Locale.getDefault());
            }
            tts.setSpeechRate(0.9f);
            ttsReady = true;
        }
    }

    /**
     * JavaScript bridge: the web page calls window.AndroidTTS.speak("...") and this
     * plays it through Android's own TTS engine, independent of WebView's (missing)
     * speechSynthesis support.
     */
    private class AndroidTTSBridge {
        @JavascriptInterface
        public void speak(String text) {
            if (!ttsReady || tts == null || text == null || text.isEmpty()) return;
            runOnUiThread(() -> tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "atmUtterance"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
