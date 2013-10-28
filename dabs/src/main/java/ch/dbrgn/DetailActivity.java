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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import de.akquinet.android.androlog.Log;

import java.util.Date;


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
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    new CheckTimeoutTask().execute(mWebview);
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


    /*** ACTION BAR ***/

    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    private void updateContent() {
        mWebview.loadUrl(getMapURL());
    }

    /**
     * Show or hide loading panel.
     * @param visible If set to true, a loading animation will be shown instead of the content.
     */
    private void setLoadingPanelVisibility(boolean visible) {
        if (visible) {
            findViewById(R.id.map_view).setVisibility(View.GONE);
            findViewById(R.id.text_view).setVisibility(View.GONE);
            findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.map_view).setVisibility(View.VISIBLE);
            findViewById(R.id.loading_panel).setVisibility(View.GONE);
        }
    }


    /*** UTILS ***/

    /**
     * Return the map URL String.
     */
    private String getMapURL() {
        return Settings.BASE_URL + "/" + mDayType + "/map/";
    }

    /**
     * Return the text URL String.
     */
    private String getTextURL() {
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


    /*** AsyncTasks ***/

    class CheckTimeoutTask extends AsyncTask<WebView, Void, Boolean> {

        private WebView webView;

        @Override
        protected Boolean doInBackground(WebView... params) {
            Log.i("CheckTimeoutTask started...");
            this.webView = params[0];
            long startTime = System.currentTimeMillis();
            long elapsedTime;
            while (true) {
                elapsedTime = (new Date()).getTime() - startTime;
                if (webView.getProgress() == 100) {
                    Log.i("Page finished loading.");
                    return true;
                } else if (elapsedTime > Settings.CONTENT_LOAD_TIMEOUT_SECONDS * 1000) {
                    Log.i("Page loading timeout reached.");
                    return false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean loadSucceeded) {
            Log.i("CheckTimeoutTask finishing...");
            if (!loadSucceeded) {
                // Stop loading of webview
                this.webView.stopLoading();
                findViewById(R.id.black_panel).setVisibility(View.VISIBLE);
                // Show alert dialog
                new AlertDialog.Builder(DetailActivity.this)
                    .setMessage(R.string.timeout_message)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            setLoadingPanelVisibility(true);
                            findViewById(R.id.black_panel).setVisibility(View.GONE);
                            updateContent();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DetailActivity.this.finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            DetailActivity.this.finish();
                        }
                    })
                    .show();
            }
        }
    }

}