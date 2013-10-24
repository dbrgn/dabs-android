package ch.dbrgn.test;

import android.test.ActivityInstrumentationTestCase2;
import ch.dbrgn.*;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<DetailActivity> {

    public HelloAndroidActivityTest() {
        super(DetailActivity.class);
    }

    public void testActivity() {
        DetailActivity activity = getActivity();
        assertNotNull(activity);
    }
}

