package ch.dbrgn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import de.akquinet.android.androlog.Log;

import java.lang.reflect.Field;

public class HomescreenActivity extends Activity {

    public final static String EXTRA_DAY_TYPE = "ch.dbrgn.dabs.DAY_TYPE";

    /*** ACTIVITY LIFECYCLE ***/

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes the logging
        Log.init();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}