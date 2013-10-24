package ch.dbrgn;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import de.akquinet.android.androlog.Log;

public class DetailActivity extends Activity {

    private WebView mWebview;
    private final int MAP_WIDTH = 1920;
    private String mDayType;

    /*** ACTIVITY LIFECYCLE ***/

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes the logging
        Log.init();

        // Log a message (only on dev platform)
        Log.i("onCreate");

        // Inflate layout
        setContentView(R.layout.detail);

        // Get the message from the intent
        Intent intent = getIntent();
        mDayType = intent.getStringExtra(HomescreenActivity.EXTRA_DAY_TYPE);
        Log.i("Initializing a DetailActivity for " + mDayType);

        // Set texts
        final TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("Here you can see the DABS map for " + mDayType + ". " +
                         "Double-tap or pinch the map to zoom.");

        // Get and configure WebView
        this.mWebview = (WebView) findViewById(R.id.map_view);
        if (savedInstanceState != null) {
            this.mWebview.restoreState(savedInstanceState);
        } else {
            this.mWebview.setInitialScale(getMapScale());
            this.mWebview.setBackgroundColor(0x00000000);
            this.mWebview.getSettings().setBuiltInZoomControls(true); // Show zoom controls
            this.mWebview.getSettings().setUseWideViewPort(true); // Enable zooming out as much as possible
            this.mWebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onLoadResource(WebView view, String url) {
                    // Hide content
                    findViewById(R.id.map_view).setVisibility(View.GONE);
                    findViewById(R.id.text_view).setVisibility(View.GONE);
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    Log.i("Updating content for " + mDayType + "...");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // Show content
                    findViewById(R.id.text_view).setVisibility(View.VISIBLE);
                    findViewById(R.id.map_view).setVisibility(View.VISIBLE);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    Log.i("Updating content for " + mDayType + " done.");
                }
            });
        }

        // Update data
        updateContent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save state of webview
        mWebview.saveState(outState);
    }


    /*** ACTION BAR ***/

    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateContent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*** DATA UPDATES ***/

    /**
     * Update content in a background thread.
     */
    public void updateContent() {
        mWebview.loadUrl(getMapURL());
    }


    /*** URL MANAGEMENT ***/

    /**
     * Return the map URL String.
     */
    protected String getMapURL() {
        return Settings.BASE_URL + "/" + mDayType + "/map/";
    }

    /**
     * Return the text URL String.
     */
    protected String getTextURL() {
        return Settings.BASE_URL + "/" + mDayType + "/text/";
    }

    /**
     * Get scale factor for map
     */
    private int getMapScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final int width = display.getWidth();
        Double val = (double) width / (double) MAP_WIDTH;
        val = val * 100d;
        return val.intValue();
    }

}

