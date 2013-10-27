package ch.dbrgn;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;
import de.akquinet.android.androlog.Log;

public class AboutActivity extends Activity {

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes the logging
        Log.init();
        Log.i("onCreate");

        // Inflate layout
        setContentView(R.layout.about);

        // Set title
        setTitle(getString(R.string.about_title));

        // Add links
        linkify((TextView) findViewById(R.id.about_part1));
        linkify((TextView) findViewById(R.id.about_part2));
    }

    private static void linkify(TextView textViewWithLinks) {
        Linkify.addLinks(textViewWithLinks, Linkify.ALL);
    }

}