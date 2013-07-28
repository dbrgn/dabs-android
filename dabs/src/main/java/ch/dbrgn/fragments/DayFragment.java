package ch.dbrgn.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ch.dbrgn.R;
import ch.dbrgn.Settings;
import de.akquinet.android.androlog.Log;

public abstract class DayFragment extends Fragment implements IDayFragment {

    protected View view;
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
        view = inflater.inflate(viewResID, container, false);
        Log.i("Setting this.view to " + view.toString());

        // Get WebView
        this.webview = (WebView) this.view.findViewById(R.id.map_view);
        this.webview.setInitialScale(getMapScale());
        this.webview.setBackgroundColor(0x00000000);
        this.webview.getSettings().setBuiltInZoomControls(true); // Show zoom controls
        this.webview.getSettings().setUseWideViewPort(true); // Enable zooming out as much as possible

        // Update data
        updateContent();

        // Return fragment view
        return this.view;
    }


    /*** DATA UPDATES ***/

    /**
     * Update content in a background thread.
     */
    public void updateContent() {
        if (this.view != null) {
            (new UpdateContent()).execute();
        } else {
            throw new RuntimeException("Fragment view object is null.");
        }
    }

    protected class UpdateContent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("Updating content for " + getDayText() + "...");
            // Hide content
            if (view == null) {
                throw new RuntimeException("Fragment view object is null.");
            }
            view.findViewById(R.id.map_view).setVisibility(View.GONE);
            view.findViewById(R.id.text_view).setVisibility(View.GONE);
            view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO preload image to cache
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
            // Show content
            if (view == null) {
                throw new RuntimeException("Fragment view object is null.");
            }
            webview.loadUrl(getMapURL());
            view.findViewById(R.id.text_view).setVisibility(View.VISIBLE);
            view.findViewById(R.id.map_view).setVisibility(View.VISIBLE);
            view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
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