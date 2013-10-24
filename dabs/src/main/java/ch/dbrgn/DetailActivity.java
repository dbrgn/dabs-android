package ch.dbrgn;

import android.app.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import de.akquinet.android.androlog.Log;

public class DetailActivity extends Activity {

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

        // Set texts
        final TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("FOO");
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

}

