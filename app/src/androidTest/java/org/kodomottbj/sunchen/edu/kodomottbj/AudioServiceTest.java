package org.kodomottbj.sunchen.edu.kodomottbj;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kodomottbj.sunchen.edu.kodomottbj.util.AudioService;

import java.util.concurrent.TimeoutException;


/**
 * Created by SunChen on 16/8/21.
 */

@MediumTest
@RunWith(AndroidJUnit4.class)
public class AudioServiceTest  {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();
    //   new ServiceTestRule();


    @Test
    public void testWithBoundService() throws TimeoutException {
        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(), AudioService.class);


        // Bind the service and grab a reference to the binder.
        IBinder binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call public methods on the binder directly.
        AudioService service = ((AudioService.LocalBinder) binder).getService();

        // Verify that the service is working correctly.
        //assertThat(service.PlayMp3(/sdcard/kodomoTTBJ/);, is(any(Integer.class)));
        Context curcontext = InstrumentationRegistry.getTargetContext();
       // Log.d("msg", curcontext.getFilesDir().getAbsolutePath());
       // service.PlayMp3(curcontext.getFilesDir().getPath()+"/assets/audioYB1_01.mp3");
    }
}
