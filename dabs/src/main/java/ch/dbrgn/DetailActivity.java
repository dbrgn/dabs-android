/*
This file is part of DABS Viewer.

DABS Viewer is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

DABS Viewer is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with DABS Viewer. If not, see <http://www.gnu.org/licenses/>.
*/

package ch.dbrgn;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
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
        Log.i("onCreate");

        // Inflate layout
        setContentView(R.layout.detail);

        // Configure action bar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get the message from the intent
        Intent intent = getIntent();
        mDayType = intent.getStringExtra(HomescreenActivity.EXTRA_DAY_TYPE);
        Log.i("Initializing a DetailActivity for " + mDayType);

        // Set texts
        setTitle("DABS " + Character.toUpperCase(mDayType.charAt(0)) + mDayType.substring(1));
        final TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("Here you can see the DABS map for " + mDayType + ". " +
                         "Double-tap or pinch the map to zoom.");

        // Get and configure WebView
        this.mWebview = (WebView) findViewById(R.id.map_view);
        this.mWebview.setInitialScale(getMapScale());
        this.mWebview.setBackgroundColor(0x00000000);
        this.mWebview.getSettings().setBuiltInZoomControls(true); // Show zoom controls
        this.mWebview.getSettings().setUseWideViewPort(true); // Enable zooming out as much as possible

        if (savedInstanceState != null) {
            this.mWebview.restoreState(savedInstanceState);
            setLoadingPanelVisibility(false);
        } else {
            this.mWebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onLoadResource(WebView view, String url) {
                    setLoadingPanelVisibility(true);
                    Log.i("Updating content for " + mDayType + "...");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    setLoadingPanelVisibility(false);
                    Log.i("Updating content for " + mDayType + " done.");
                }
            });

            updateContent();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save state of webview
        mWebview.saveState(outState);
    }

    /**
     * Show or hide loading panel.
     * @param visible If set to true, a loading animation will be shown instead of the content.
     */
    private void setLoadingPanelVisibility(boolean visible) {
        if (visible) {
            findViewById(R.id.map_view).setVisibility(View.GONE);
            findViewById(R.id.text_view).setVisibility(View.GONE);
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.map_view).setVisibility(View.VISIBLE);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }


    /*** ACTION BAR ***/

    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_actions, menu);
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


    /*** Utils ***/

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
        // Get display width
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        // Calculate map scale
        Double val = (double) point.x / (double) MAP_WIDTH;
        val = val * 100d;
        return val.intValue();
    }

}

