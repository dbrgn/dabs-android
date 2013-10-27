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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
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