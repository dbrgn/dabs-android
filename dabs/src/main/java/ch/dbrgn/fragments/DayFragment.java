package ch.dbrgn.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import ch.dbrgn.R;
import ch.dbrgn.Settings;
import de.akquinet.android.androlog.Log;

public abstract class DayFragment extends Fragment implements IDayFragment {

    protected WebView webview;
    protected Activity activity;
    protected final int MAP_WIDTH = 1920;


    /*** ACTIVITY LIFECYCLE ***/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get activity
        this.activity = getActivity();

        // Inflate view
        final int viewResID = getResources().getIdentifier("fragment_" + getDayText(), "layout", activity.getPackageName());
        View view = inflater.inflate(viewResID, container, false);

        // Get WebView
        this.webview = (WebView) view.findViewById(R.id.map_view);
        this.webview.setInitialScale(getMapScale());
        this.webview.setBackgroundColor(0x00000000);
        this.webview.getSettings().setBuiltInZoomControls(true); // Show zoom controls
        this.webview.getSettings().setUseWideViewPort(true); // Enable zooming out as much as possible

        // Return fragment view
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        updateContent();
    }


    /*** DATA UPDATES ***/

    /**
     * Update content in a background thread
     */
    public void updateContent() {
        UpdateContent updater = new UpdateContent();
        updater.execute();
        Log.i("updating content");
    }

    protected class UpdateContent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("Updating content for " + getDayText() + "...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
            webview.loadUrl(getMapURL());
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