package ch.dbrgn;

import android.app.*;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import ch.dbrgn.fragments.DayFragment;
import ch.dbrgn.fragments.TodayFragment;
import ch.dbrgn.fragments.TomorrowFragment;
import de.akquinet.android.androlog.Log;

import java.util.ArrayList;

public class MainActivity extends Activity {

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes the logging
        Log.init();

        // Log a message (only on dev platform)
        Log.i("onCreate");

        // Setup action bar for tabs
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        // Create tabs
        Tab tabToday = actionBar.newTab()
                .setText(R.string.tab_today)
                .setTabListener(new TabListener<TodayFragment>(
                        this, "today", TodayFragment.class));
        Tab tabTomorrow = actionBar.newTab()
                .setText(R.string.tab_tomorrow)
                .setTabListener(new TabListener<TomorrowFragment>(
                        this, "tomorrow", TomorrowFragment.class));

        // Add tabs to action bar
        actionBar.addTab(tabToday);
        actionBar.addTab(tabTomorrow);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // TODO
                Log.i("Refreshed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private DayFragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /** Constructor used each time a new tab is created.
         * @param activity  The host Activity, used to instantiate the fragment
         * @param tag  The identifier tag for the fragment
         * @param clz  The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        /* The following are each of the ActionBar.TabListener callbacks */
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = (DayFragment) Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
            mFragment.updateContent();
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }

}

