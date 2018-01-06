package de.thral.draegermanObservation;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.thral.draegermanObservation.business.Draegerman;
import de.thral.draegermanObservation.business.EventType;
import de.thral.draegermanObservation.business.OperatingTime;
import de.thral.draegermanObservation.business.Order;
import de.thral.draegermanObservation.business.Squad;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("de.thral.atemschutzueberwachung", appContext.getPackageName());
    }
}
