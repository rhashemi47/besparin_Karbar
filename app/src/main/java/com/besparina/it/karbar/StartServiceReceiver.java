package com.besparina.it.karbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by hashemi on 10/15/2018.
 */

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, ServiceGetServiceSaved.class));
            context.startForegroundService(new Intent(context, ServiceGetLocation.class));
            context.startForegroundService(new Intent(context, ServiceGetSliderPic.class));
            context.startForegroundService(new Intent(context, ServiceSyncMessage.class));
            context.startForegroundService(new Intent(context, ServiceGetServicesAndServiceDetails.class));
            context.startForegroundService(new Intent(context, ServiceGetPerFactor.class));
            context.startForegroundService(new Intent(context, ServiceGetServiceVisit.class));
            context.startForegroundService(new Intent(context, ServiceGetStateAndCity.class));
            context.startForegroundService(new Intent(context, ServiceGetUserServiceStartDate.class));
        } else {
            context.startService(new Intent(context, ServiceGetServiceSaved.class));
            context.startService(new Intent(context, ServiceGetLocation.class));
            context.startService(new Intent(context, ServiceGetSliderPic.class));
            context.startService(new Intent(context, ServiceSyncMessage.class));
            context.startService(new Intent(context, ServiceGetServicesAndServiceDetails.class));
            context.startService(new Intent(context, ServiceGetPerFactor.class));
            context.startService(new Intent(context, ServiceGetServiceVisit.class));
            context.startService(new Intent(context, ServiceGetStateAndCity.class));
            context.startService(new Intent(context, ServiceGetUserServiceStartDate.class));
        }
    }
}
