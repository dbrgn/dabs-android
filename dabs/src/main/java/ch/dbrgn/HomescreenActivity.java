package ch.dbrgn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.akquinet.android.androlog.Log;

public class HomescreenActivity extends Activity {

    public final static String EXTRA_DAY_TYPE = "ch.dbrgn.dabs.DAY_TYPE";

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
        setContentView(R.layout.homescreen);

        // Bind click listeners to buttons
        final Button btnToday = (Button) findViewById(R.id.btn_today);
        final Button btnTomorrow = (Button) findViewById(R.id.btn_tomorrow);
        btnToday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("onClick today");
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(EXTRA_DAY_TYPE, "today");
                startActivity(intent);
            }
        });
        btnTomorrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("onClick today");
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(EXTRA_DAY_TYPE, "tomorrow");
                startActivity(intent);
            }
        });
    }

}