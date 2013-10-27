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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.Button;
import de.akquinet.android.androlog.Log;

public class HomescreenActivity extends Activity {

    public final static String EXTRA_DAY_TYPE = "ch.dbrgn.dabs.DAY_TYPE";
    private SharedPreferences mPrefs;
    private final static String DISCLAIMER_SHOWN_PREF = "disclaimerShown";


    /*** ACTIVITY LIFECYCLE ***/

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes the logging
        Log.init();
        Log.i("onCreate");

        // Get shared preferences
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean disclaimerShown = mPrefs.getBoolean(DISCLAIMER_SHOWN_PREF, false);

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

        // Show disclaimer on first start
        if (!disclaimerShown) {
            String disclaimerTitle = getResources().getString(R.string.disclaimer_title);
            String disclaimerText = getResources().getString(R.string.disclaimer_text);
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(disclaimerTitle).setMessage(disclaimerText).setPositiveButton(
                    R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(DISCLAIMER_SHOWN_PREF, true);
            editor.commit(); // Very important to save the preference
        }
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