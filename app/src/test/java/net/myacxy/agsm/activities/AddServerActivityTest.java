package net.myacxy.agsm.activities;

import net.myacxy.agsm.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "app/src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk = 21)
public class AddServerActivityTest {

    private AddServerActivity activity;

    @Before
    public void setUp() {
        // start activity
        activity = Robolectric.buildActivity(AddServerActivity_.class)
                .create()
                .start()
                .resume()
                .get();
        assertNotNull(activity);
    }

    @Test
    public void checkViews() {
        assertNotNull(activity.doneButton);
        assertNotNull(activity.addressTextView);
        assertNotNull(activity.portTextView);
        assertNotNull(activity.queryPortTextView);
    }
} // AddServerActivityTest
