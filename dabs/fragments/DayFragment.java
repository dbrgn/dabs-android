package ch.dbrgn.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ch.dbrgn.R;
import ch.dbrgn.Settings;
import de.akquinet.android.androlog.Log;

public abstract class DayFragment extends Fragment implements IDayFragment {

    protected View frameView;
    protected WebView webview;
    protected Activity activity;
    protected final int MAP_WIDTH = 1920;

    /*** ACTIVITY LIFECYCLE ***/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView");

        // Get activity
        this.activity = getActivity();

        // Inflate view
        final int viewResID = getResources().getIdentifier("fragment_" + getDayText(), "layout", activity.getPackageName());
        frameView = inflater.inflate(viewResID, container, false);
        Log.i("Setting this.frameView to " + frameView.toString());

        // Get and configure WebView
        this.webview = (WebView) this.frameView.findViewById(R.id.map_view);
        if (savedInstanceState != null) {
            this.webview.restoreState(savedInstanceState);
        } else {
            this.webview.setInitialScale(getMapScale());
            this.webview.setBackgroundColor(0x00000000);
            this.webview.getSettings().setBuiltInZoomControls(true); // Show zoom controls
            this.webview.getSettings().setUseWideViewPort(true); // Enable zooming out as much as possible
            this.webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onLoadResource(WebView view, String url) {
                    Log.i("Updating content for " + getDayText() + "...");
                    // Hide content
                    if (frameView == null) {
                        Log.e("Fragment view object is null.");
                    } else {
                        frameView.findViewById(R.id.map_view).setVisibility(View.GONE);
                        frameView.findViewById(R.id.text_view).setVisibility(View.GONE);
                        frameView.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // Show content
                    if (frameView == null) {
                        Log.e("Fragment view object is null.");
                    } else {
                        frameView.findViewById(R.id.text_view).setVisibility(View.VISIBLE);
                        frameView.findViewById(R.id.map_view).setVisibility(View.VISIBLE);
                        frameView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    }
                }
            });
        }

        // Update data
        updateContent();

        // Return fragment view
        return this.frameView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save state of webview
        this.webview.saveState(outState);
    }

    /*** DATA UPDATES ***/

    /**
     * Update content in a background thread.
     */
    public void updateContent() {
        webview.loadUrl(getMapURL());
    }


    /*** URL MANAGEMENT ***/

    /**
     * Return the map URL String.
     */
    protected String getMapURL() {
        return Settings.BASE_URL + "/" + getDayText() + "/map/";
    }

    /**
     * Return the text URL String.
     */
    protected String getTextURL() {
        return Settings.BASE_URL + "/" + getDayText() + "/text/";
    }

    /**
     * Get scale factor for map
     */
    private int getMapScale(){
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final int width = display.getWidth();
        Double val = (double) width / (double) MAP_WIDTH;
        val = val * 100d;
        return val.intValue();
    }

}