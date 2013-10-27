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